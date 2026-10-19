package pl.bumbox.autospeaker;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.graphics.Path;
import android.graphics.Rect;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import java.text.Normalizer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Locale;

public final class AutoSpeakerService extends AccessibilityService implements SensorEventListener {
    private static final String TAG = "AutoGlosnik";
    private static final long DEBOUNCE_MS = 35L;
    private static final long[] RETRY_DELAYS_MS = {0L, 45L, 110L, 220L, 450L, 850L};
    private static final int MAX_LATE_RETRIES = 2;
    private static final long LATE_RETRY_DELAY_MS = 600L;
    private static final long TOUCH_EXPLORATION_ACTIVATION_DELAY_MS = 250L;
    private static final long SAMSUNG_TOUCH_EXPLORATION_ACTIVATION_DELAY_MS = 700L;
    private static final String GOOGLE_DIALER = "com.google.android.dialer";
    private static final String AOSP_DIALER = "com.android.dialer";
    private static final String SAMSUNG_DIALER = "com.samsung.android.dialer";
    private static final String SAMSUNG_INCALL_UI = "com.samsung.android.incallui";
    private static final float GOOGLE_SPEAKER_BUTTON_X_RATIO = 0.6125f;
    private static final float GOOGLE_SPEAKER_BUTTON_Y_RATIO = 0.7671f;
    private static final float SAMSUNG_SPEAKER_BUTTON_X_RATIO = 0.2250f;
    private static final float SAMSUNG_SPEAKER_BUTTON_Y_RATIO = 0.7312f;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private AudioManager audioManager;
    private TelecomManager telecomManager;
    private SharedPreferences preferences;

    private Boolean pendingNear;
    private Boolean stableNear;
    private Boolean lastAppliedSpeaker;
    private boolean lastKnownInCall;
    private boolean directRouteRequested;
    private boolean coordinateGesturePending;
    private Boolean stabilizationTarget;
    private int routeGeneration;
    private int lateRetryCount;
    private long lastCallUiSeen;

    private final Runnable debounceRunnable = () -> {
        if (pendingNear == null) return;
        if (pendingNear.equals(stableNear)) return;
        stableNear = pendingNear;
        lateRetryCount = 0;
        requestRouteForProximity(stableNear);
    };

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo info = getServiceInfo();
        if (info != null) {
            info.flags |= AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
                    | AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;
            setServiceInfo(info);
        }

        preferences = getSharedPreferences(MainActivity.PREFS, MODE_PRIVATE);
        audioManager = getSystemService(AudioManager.class);
        telecomManager = getSystemService(TelecomManager.class);
        sensorManager = getSystemService(SensorManager.class);
        if (sensorManager != null) {
            proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            if (proximitySensor != null) {
                sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_GAME);
            }
        }
        reportStatus(getString(R.string.status_service_running));
        Log.i(TAG, "Usługa uruchomiona");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (!isEnabledByUser() || event == null) return;
        CharSequence packageName = event.getPackageName();
        if (isSupportedCallUiPackage(packageName)) {
            AccessibilityNodeInfo routeButton = findRouteButton();
            if (routeButton != null) {
                lastCallUiSeen = SystemClock.elapsedRealtime();
            }
            if (stableNear != null && isCallActive()) {
                requestRouteForProximity(stableNear);
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event == null || event.sensor.getType() != Sensor.TYPE_PROXIMITY
                || event.values.length == 0 || proximitySensor == null) return;

        float threshold = Math.min(proximitySensor.getMaximumRange(), 5.0f);
        boolean near = event.values[0] < threshold;
        if (pendingNear != null && pendingNear == near) return;
        pendingNear = near;
        Log.d(TAG, "Czujnik: " + (near ? "NEAR" : "FAR")
                + ", wartość=" + event.values[0] + ", próg=" + threshold);
        handler.removeCallbacks(debounceRunnable);
        handler.postDelayed(debounceRunnable, DEBOUNCE_MS);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Binary near/far readings are sufficient; accuracy changes need no action.
    }

    @Override
    public void onInterrupt() {
        Log.i(TAG, "Usługa przerwana przez system");
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        if (sensorManager != null) sensorManager.unregisterListener(this);
        clearDirectAudioRequest();
        super.onDestroy();
    }

    private void requestRouteForProximity(boolean near) {
        if (!isEnabledByUser()) return;
        boolean wantsSpeaker = !near;
        boolean inCall = isCallActive();
        if (!inCall) {
            clearDirectAudioRequest();
            routeGeneration++;
            stabilizationTarget = null;
            if (lastKnownInCall) reportStatus(getString(R.string.status_call_ended));
            lastKnownInCall = false;
            lastAppliedSpeaker = null;
            lateRetryCount = 0;
            coordinateGesturePending = false;
            return;
        }
        if (!lastKnownInCall) {
            lastKnownInCall = true;
            lastAppliedSpeaker = null;
            stabilizationTarget = null;
            lateRetryCount = 0;
        }

        if (isExternalAudioRoute()) {
            clearDirectAudioRequest();
            routeGeneration++;
            stabilizationTarget = null;
            reportStatus(getString(R.string.status_external_audio));
            lastAppliedSpeaker = null;
            return;
        }

        if (stabilizationTarget != null && stabilizationTarget == wantsSpeaker) return;

        Boolean actualSpeaker = currentSpeakerState();
        if (actualSpeaker != null && actualSpeaker == wantsSpeaker && !directRouteRequested) {
            lastAppliedSpeaker = wantsSpeaker;
            stabilizationTarget = wantsSpeaker;
            return;
        }
        if (actualSpeaker == null && lastAppliedSpeaker != null && lastAppliedSpeaker == wantsSpeaker
                && !directRouteRequested) {
            stabilizationTarget = wantsSpeaker;
            return;
        }

        stabilizationTarget = wantsSpeaker;
        coordinateGesturePending = false;
        int generation = ++routeGeneration;
        tryDirectAudioRoute(wantsSpeaker);

        for (int i = 0; i < RETRY_DELAYS_MS.length; i++) {
            long delay = RETRY_DELAYS_MS[i];
            boolean firstAttempt = i == 0;
            boolean lastAttempt = i == RETRY_DELAYS_MS.length - 1;
            handler.postDelayed(() -> attemptRouteChange(
                    wantsSpeaker, firstAttempt, lastAttempt, generation), delay);
        }
    }

    private void attemptRouteChange(
            boolean wantsSpeaker, boolean firstAttempt, boolean lastAttempt, int generation) {
        if (generation != routeGeneration || !isEnabledByUser()
                || !isCallActive() || isExternalAudioRoute()
                || coordinateGesturePending) return;

        Boolean actualSpeaker = currentSpeakerState();
        if (actualSpeaker != null && actualSpeaker == wantsSpeaker) {
            if (!directRouteRequested) lastAppliedSpeaker = wantsSpeaker;
            if (lastAttempt) scheduleFinalConfirmation(wantsSpeaker, generation);
            return;
        }

        if (actualSpeaker == null && lastAppliedSpeaker != null && lastAppliedSpeaker == wantsSpeaker
                && !directRouteRequested) return;

        // Google Telefon potrafi nadpisać pierwszą zmianę podczas zestawiania rozmowy.
        // Ponawiamy standardowe żądanie audio na każdym etapie stabilizacji.
        tryDirectAudioRoute(wantsSpeaker);
        actualSpeaker = currentSpeakerState();
        if (actualSpeaker != null && actualSpeaker == wantsSpeaker) {
            if (lastAttempt) scheduleFinalConfirmation(wantsSpeaker, generation);
            return;
        }

        AccessibilityNodeInfo button = findRouteButton();
        if (button == null) {
            if (firstAttempt) {
                if (telecomManager != null) {
                    try {
                        telecomManager.showInCallScreen(false);
                    } catch (SecurityException ignored) {
                        // Permission status is shown in the main activity.
                    }
                }
                scheduleCoordinateRouteGesture(wantsSpeaker, generation);
            }
            if (lastAttempt) {
                AccessibilityNodeInfo activeRoot = getRootInActiveWindow();
                Log.w(TAG, "Nie znaleziono przycisku trasy. windows=" + getWindows().size()
                        + ", activePackage=" + (activeRoot == null
                        ? "null" : activeRoot.getPackageName()));
                if (!dispatchCoordinateRouteGesture(wantsSpeaker, generation)) {
                    scheduleFinalConfirmation(wantsSpeaker, generation);
                }
            }
            return;
        }

        AccessibilityNodeInfo clickable = actionableClickNode(button);
        Boolean nodeState = inferSpeakerState(button);
        if (nodeState == null && clickable != null && clickable != button) {
            nodeState = inferSpeakerState(clickable);
        }
        if (nodeState != null && nodeState == wantsSpeaker) {
            if (lastAttempt) scheduleFinalConfirmation(wantsSpeaker, generation);
            return;
        }
        if (performNodeClick(clickable, wantsSpeaker, generation, "etykieta")) {
            return;
        } else if (lastAttempt) {
            if (!dispatchCoordinateRouteGesture(wantsSpeaker, generation)) {
                scheduleFinalConfirmation(wantsSpeaker, generation);
            }
        }
    }

    private void scheduleCoordinateRouteGesture(boolean wantsSpeaker, int generation) {
        handler.postDelayed(() -> {
            if (generation != routeGeneration || coordinateGesturePending
                    || !isCallActive() || isExternalAudioRoute()) return;
            Boolean actualSpeaker = currentSpeakerState();
            if (actualSpeaker != null && actualSpeaker == wantsSpeaker) return;

            AccessibilityNodeInfo button = findRouteButton();
            if (button != null) {
                attemptRouteChange(wantsSpeaker, false, false, generation);
                return;
            }
            dispatchCoordinateRouteGesture(wantsSpeaker, generation);
        }, 0L);
    }
    private boolean dispatchCoordinateRouteGesture(boolean wantsSpeaker, int generation) {
        if (!isCallActive() || coordinateGesturePending) return false;
        AccessibilityNodeInfo callRoot = findCallUiRoot();
        if (callRoot == null) {
            return false;
        }

        Rect bounds = new Rect();
        callRoot.getBoundsInScreen(bounds);
        if (bounds.isEmpty() || bounds.width() >= bounds.height()) {
            Log.w(TAG, "Gest wspolrzednych pominiety: nieobslugiwany uklad " + bounds);
            return false;
        }

        CharSequence callPackage = callRoot.getPackageName();
        float xRatio;
        float yRatio;
        if (GOOGLE_DIALER.contentEquals(callPackage)) {
            xRatio = GOOGLE_SPEAKER_BUTTON_X_RATIO;
            yRatio = GOOGLE_SPEAKER_BUTTON_Y_RATIO;
        } else if (SAMSUNG_INCALL_UI.contentEquals(callPackage)) {
            // Samsung SM-S931B / One UI 8.5: fourth_button, środek [243,1711]
            // przy bazowym ekranie 1080x2340. Ułamki zachowują się przy skalowaniu.
            xRatio = SAMSUNG_SPEAKER_BUTTON_X_RATIO;
            yRatio = SAMSUNG_SPEAKER_BUTTON_Y_RATIO;
        } else {
            Log.w(TAG, "Gest współrzędnych pominięty dla pakietu "
                    + callPackage);
            return false;
        }
        float x = bounds.left + bounds.width() * xRatio;
        float y = bounds.top + bounds.height() * yRatio;

        // ACTION_CLICK omija TouchExplorera, więc nie jest zamieniany przez TalkBack
        // w ustawienie fokusu. Wyszukiwanie po położeniu obsługuje też przyciski bez
        // etykiety, przez które wcześniej kod wpadał w awaryjny gest ekranowy.
        AccessibilityNodeInfo coordinateNode = findActionableNodeAtPoint(
                callRoot, Math.round(x), Math.round(y));
        if (performNodeClick(coordinateNode, wantsSpeaker, generation, "położenie")) {
            handler.postDelayed(
                    () -> scheduleFinalConfirmation(wantsSpeaker, generation), 250L);
            return true;
        }

        AccessibilityManager accessibilityManager =
                getSystemService(AccessibilityManager.class);
        boolean touchExploration = accessibilityManager != null
                && accessibilityManager.isTouchExplorationEnabled();
        if (touchExploration) {
            coordinateGesturePending = true;
            boolean focusDispatched = dispatchGesture(
                    singleTapGesture(x, y), new GestureResultCallback() {
                        @Override
                        public void onCompleted(GestureDescription gestureDescription) {
                            if (generation != routeGeneration || !isCallActive()) {
                                coordinateGesturePending = false;
                                return;
                            }
                            Log.i(TAG, "Ustawiono fokus CSR na przycisku Glosnik.");
                            long activationDelay = SAMSUNG_INCALL_UI.contentEquals(callPackage)
                                    ? SAMSUNG_TOUCH_EXPLORATION_ACTIVATION_DELAY_MS
                                    : TOUCH_EXPLORATION_ACTIVATION_DELAY_MS;
                            handler.postDelayed(() -> dispatchCsrActivationGesture(
                                    wantsSpeaker, generation, x, y), activationDelay);
                        }

                        @Override
                        public void onCancelled(GestureDescription gestureDescription) {
                            coordinateGesturePending = false;
                            Log.w(TAG, "Gest ustawienia fokusu CSR zostal anulowany.");
                            scheduleFinalConfirmation(wantsSpeaker, generation);
                        }
                    }, handler);
            coordinateGesturePending = focusDispatched;
            Log.i(TAG, "Zlecono ustawienie fokusu CSR: " + focusDispatched
                    + ", x=" + x + ", y=" + y);
            return focusDispatched;
        }

        coordinateGesturePending = true;
        boolean dispatched = dispatchGesture(singleTapGesture(x, y), new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                coordinateGesturePending = false;
                if (generation != routeGeneration || !isCallActive()) return;
                clearDirectAudioRequest();
                lastAppliedSpeaker = wantsSpeaker;
                Log.i(TAG, "Wykonano gest przycisku Glosnik: x=" + x + ", y=" + y);
                handler.postDelayed(
                        () -> scheduleFinalConfirmation(wantsSpeaker, generation), 250L);
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                coordinateGesturePending = false;
                Log.w(TAG, "Gest przycisku Glosnik zostal anulowany.");
                scheduleFinalConfirmation(wantsSpeaker, generation);
            }
        }, handler);
        coordinateGesturePending = dispatched;
        Log.i(TAG, "Zlecono gest przycisku Glosnik: " + dispatched
                + ", x=" + x + ", y=" + y);
        return dispatched;
    }

    private GestureDescription singleTapGesture(float x, float y) {
        Path tap = new Path();
        tap.moveTo(x, y);
        return new GestureDescription.Builder()
                .addStroke(new GestureDescription.StrokeDescription(tap, 0L, 40L))
                .build();
    }

    private void dispatchCsrActivationGesture(
            boolean wantsSpeaker, int generation, float x, float y) {
        if (generation != routeGeneration || !isCallActive() || isExternalAudioRoute()) {
            coordinateGesturePending = false;
            return;
        }

        Path firstTap = new Path();
        firstTap.moveTo(x, y);
        Path secondTap = new Path();
        secondTap.moveTo(x, y);
        GestureDescription activationGesture = new GestureDescription.Builder()
                .addStroke(new GestureDescription.StrokeDescription(firstTap, 0L, 40L))
                .addStroke(new GestureDescription.StrokeDescription(secondTap, 100L, 40L))
                .build();

        boolean dispatched = dispatchGesture(activationGesture, new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                coordinateGesturePending = false;
                if (generation != routeGeneration || !isCallActive()) return;
                clearDirectAudioRequest();
                lastAppliedSpeaker = wantsSpeaker;
                Log.i(TAG, "Wykonano aktywacyjny dwuklik CSR przycisku Glosnik.");
                handler.postDelayed(
                        () -> scheduleFinalConfirmation(wantsSpeaker, generation), 250L);
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                coordinateGesturePending = false;
                Log.w(TAG, "Aktywacyjny dwuklik CSR zostal anulowany.");
                scheduleFinalConfirmation(wantsSpeaker, generation);
            }
        }, handler);
        coordinateGesturePending = dispatched;
        Log.i(TAG, "Zlecono aktywacyjny dwuklik CSR: " + dispatched
                + ", x=" + x + ", y=" + y);
        if (!dispatched) scheduleFinalConfirmation(wantsSpeaker, generation);
    }
    private void scheduleFinalConfirmation(boolean wantsSpeaker, int generation) {
        handler.postDelayed(() -> {
            if (generation != routeGeneration || !isCallActive()) return;
            Boolean actualSpeaker = currentSpeakerState();
            if (actualSpeaker != null && actualSpeaker == wantsSpeaker) {
                lateRetryCount = 0;
                reportStatus(getString(wantsSpeaker
                        ? R.string.status_speaker_stable
                        : R.string.status_earpiece_stable));
            } else {
                stabilizationTarget = null;
                reportStatus(getString(R.string.status_diagnostic_needed));
                Log.w(TAG, "Trasa nadal niezgodna. oczekiwana="
                        + (wantsSpeaker ? "speaker" : "earpiece")
                        + ", rzeczywista=" + actualSpeaker
                        + ", późnaPróba=" + lateRetryCount);
                if (lateRetryCount < MAX_LATE_RETRIES) {
                    lateRetryCount++;
                    handler.postDelayed(() -> {
                        if (generation != routeGeneration || stableNear == null
                                || !isCallActive()) return;
                        requestRouteForProximity(stableNear);
                    }, LATE_RETRY_DELAY_MS);
                } else {
                    // Zatrzymaj serię do kolejnej rzeczywistej zmiany czujnika.
                    stabilizationTarget = wantsSpeaker;
                }
            }
        }, 450L);
    }

    private boolean isCallActive() {
        if (telecomManager != null
                && checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                return telecomManager.isInCall();
            } catch (SecurityException ignored) {
                // Fall back to observing the supported Phone call UI.
            }
        }
        return SystemClock.elapsedRealtime() - lastCallUiSeen < 10_000L
                || findRouteButton() != null;
    }

    private void tryDirectAudioRoute(boolean wantsSpeaker) {
        if (audioManager == null) return;
        // Ponowne ustawienie tej samej trasy wymaga odświeżenia wcześniejszego żądania.
        if (directRouteRequested) clearDirectAudioRequest();
        int wantedType = wantsSpeaker
                ? AudioDeviceInfo.TYPE_BUILTIN_SPEAKER
                : AudioDeviceInfo.TYPE_BUILTIN_EARPIECE;
        for (AudioDeviceInfo device : audioManager.getAvailableCommunicationDevices()) {
            if (device.getType() != wantedType) continue;
            try {
                boolean accepted = audioManager.setCommunicationDevice(device);
                Log.d(TAG, "setCommunicationDevice(" + wantedType + ")=" + accepted
                        + ", active=" + audioManager.getCommunicationDevice());
                if (accepted) {
                    directRouteRequested = true;
                }
            } catch (SecurityException ignored) {
                // The narrowly scoped accessibility fallback remains available.
            }
            return;
        }
    }

    private void clearDirectAudioRequest() {
        if (!directRouteRequested || audioManager == null) return;
        try {
            audioManager.clearCommunicationDevice();
        } catch (SecurityException ignored) {
            // Nothing to clean when the platform rejected the request.
        }
        directRouteRequested = false;
    }

    private Boolean currentSpeakerState() {
        if (audioManager == null) return null;
        AudioDeviceInfo device = audioManager.getCommunicationDevice();
        if (device == null) return null;
        if (device.getType() == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER) return true;
        if (device.getType() == AudioDeviceInfo.TYPE_BUILTIN_EARPIECE) return false;
        return null;
    }

    private boolean isExternalAudioRoute() {
        if (audioManager == null) return false;
        AudioDeviceInfo device = audioManager.getCommunicationDevice();
        if (device == null) return false;
        return switch (device.getType()) {
            case AudioDeviceInfo.TYPE_BLUETOOTH_A2DP,
                    AudioDeviceInfo.TYPE_BLUETOOTH_SCO,
                    AudioDeviceInfo.TYPE_BLE_HEADSET,
                    AudioDeviceInfo.TYPE_BLE_SPEAKER,
                    AudioDeviceInfo.TYPE_HEARING_AID,
                    AudioDeviceInfo.TYPE_WIRED_HEADPHONES,
                    AudioDeviceInfo.TYPE_WIRED_HEADSET,
                    AudioDeviceInfo.TYPE_USB_HEADSET -> true;
            default -> false;
        };
    }

    private AccessibilityNodeInfo findRouteButton() {
        List<AccessibilityWindowInfo> windows = getWindows();
        for (AccessibilityWindowInfo window : windows) {
            AccessibilityNodeInfo root = window.getRoot();
            if (root == null) continue;
            if (!isSupportedCallUiPackage(root.getPackageName())) continue;
            AccessibilityNodeInfo candidate = findRouteButtonInRoot(root);
            if (candidate != null) return candidate;
        }

        AccessibilityNodeInfo activeRoot = getRootInActiveWindow();
        if (activeRoot != null && isSupportedCallUiPackage(activeRoot.getPackageName())) {
            return findRouteButtonInRoot(activeRoot);
        }
        return null;
    }

    private AccessibilityNodeInfo findCallUiRoot() {
        AccessibilityNodeInfo best = null;
        long bestArea = 0L;
        int bestPriority = -1;
        for (AccessibilityWindowInfo window : getWindows()) {
            AccessibilityNodeInfo root = window.getRoot();
            if (root == null || !isSupportedCallUiPackage(root.getPackageName())) continue;
            Rect bounds = new Rect();
            root.getBoundsInScreen(bounds);
            long area = (long) bounds.width() * bounds.height();
            int priority = callUiPriority(root.getPackageName());
            if (!bounds.isEmpty()
                    && (priority > bestPriority || (priority == bestPriority && area > bestArea))) {
                best = root;
                bestArea = area;
                bestPriority = priority;
            }
        }
        if (best != null) return best;

        AccessibilityNodeInfo activeRoot = getRootInActiveWindow();
        return activeRoot != null && isSupportedCallUiPackage(activeRoot.getPackageName())
                ? activeRoot : null;
    }

    private AccessibilityNodeInfo findActionableNodeAtPoint(
            AccessibilityNodeInfo root, int x, int y) {
        AccessibilityNodeInfo best = null;
        long bestArea = Long.MAX_VALUE;
        Deque<AccessibilityNodeInfo> queue = new ArrayDeque<>();
        queue.add(root);
        Rect bounds = new Rect();
        while (!queue.isEmpty()) {
            AccessibilityNodeInfo node = queue.removeFirst();
            node.getBoundsInScreen(bounds);
            if (node.isVisibleToUser() && bounds.contains(x, y)) {
                AccessibilityNodeInfo actionable = actionableClickNode(node);
                if (actionable != null) {
                    Rect actionableBounds = new Rect();
                    actionable.getBoundsInScreen(actionableBounds);
                    long area = (long) actionableBounds.width() * actionableBounds.height();
                    if (!actionableBounds.isEmpty() && area < bestArea) {
                        best = actionable;
                        bestArea = area;
                    }
                }
                for (int i = 0; i < node.getChildCount(); i++) {
                    AccessibilityNodeInfo child = node.getChild(i);
                    if (child != null) queue.addLast(child);
                }
            }
        }
        return best;
    }

    private AccessibilityNodeInfo findRouteButtonInRoot(AccessibilityNodeInfo root) {
        AccessibilityNodeInfo best = null;
        int bestScore = 0;
        Deque<AccessibilityNodeInfo> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            AccessibilityNodeInfo node = queue.removeFirst();
            int score = routeButtonScore(node);
            if (score > bestScore) {
                best = node;
                bestScore = score;
            }
            for (int i = 0; i < node.getChildCount(); i++) {
                AccessibilityNodeInfo child = node.getChild(i);
                if (child != null) queue.addLast(child);
            }
        }
        return bestScore >= 50 ? best : null;
    }

    private int routeButtonScore(AccessibilityNodeInfo node) {
        String id = normalize(node.getViewIdResourceName());
        String label = normalize(join(node.getText(), node.getContentDescription(), node.getStateDescription()));
        int score = 0;
        if (id.contains("audio_route") || id.contains("audioroute") || id.contains("speaker")) {
            score += 100;
        }
        if (containsSpeakerWord(label)) score += 70;
        boolean genericAudioLabel = label.equals("audio")
                || label.contains("wyjscie audio")
                || label.contains("urzadzenie audio")
                || label.contains("dzwiek")
                || label.contains("audioausgabe")
                || label.contains("audiogerat")
                || label.equals("ton")
                || label.contains("аудиовыход")
                || label.contains("аудиоустройство")
                || label.equals("звук");
        if (genericAudioLabel && (node.isClickable() || node.isCheckable())) score += 55;
        if (normalize(String.valueOf(node.getClassName())).contains("button")) score += 5;
        if (label.contains("bluetooth") || label.contains("sluchawki")
                || label.contains("headset") || label.contains("wycisz")
                || label.contains("mute") || label.contains("glosnosc")
                || label.contains("volume") || label.contains("kopfhorer")
                || label.contains("stumm") || label.contains("lautstarke")
                || label.contains("наушник") || label.contains("без звука")
                || label.contains("громкость")) {
            score -= 100;
        }
        return score;
    }

    private Boolean inferSpeakerState(AccessibilityNodeInfo node) {
        if (node.isCheckable()) return node.isChecked();
        String label = normalize(join(node.getText(), node.getContentDescription(), node.getStateDescription()));
        if (!containsSpeakerWord(label)) return null;
        if (label.contains("wylaczony") || label.contains("speaker off")) return false;
        if (label.contains("wlaczony") || label.contains("speaker on")) return true;
        if (label.contains("wylacz glos") || label.contains("turn speaker off")) return true;
        if (label.contains("wlacz glos") || label.contains("turn speaker on")) return false;
        if (label.contains("lautsprecher aus") || label.contains("динамик выключен")) return false;
        if (label.contains("lautsprecher ein") || label.contains("динамик включен")) return true;
        if (label.contains("lautsprecher ausschalten") || label.contains("выключить динамик")) return true;
        if (label.contains("lautsprecher einschalten") || label.contains("включить динамик")) return false;
        return null;
    }

    private AccessibilityNodeInfo actionableClickNode(AccessibilityNodeInfo node) {
        AccessibilityNodeInfo current = node;
        for (int i = 0; current != null && i < 6; i++) {
            if (current.isClickable()
                    || current.getActionList().contains(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK)) {
                return current;
            }
            current = current.getParent();
        }
        return null;
    }

    private boolean performNodeClick(
            AccessibilityNodeInfo node, boolean wantsSpeaker, int generation, String source) {
        if (node == null || !node.performAction(AccessibilityNodeInfo.ACTION_CLICK)) return false;
        clearDirectAudioRequest();
        lastAppliedSpeaker = wantsSpeaker;
        String status = getString(wantsSpeaker
                ? R.string.status_speaker_enabled
                : R.string.status_earpiece_enabled);
        reportStatus(status);
        Log.i(TAG, status + " Kliknięcie węzła: " + source);
        return true;
    }

    private void reportStatus(String status) {
        if (preferences != null) {
            preferences.edit().putString(MainActivity.KEY_LAST_STATUS, status).apply();
        }
    }

    private boolean isEnabledByUser() {
        return preferences == null || preferences.getBoolean(MainActivity.KEY_ENABLED, true);
    }

    private static boolean isSupportedCallUiPackage(CharSequence packageName) {
        if (packageName == null) return false;
        return GOOGLE_DIALER.contentEquals(packageName)
                || AOSP_DIALER.contentEquals(packageName)
                || SAMSUNG_DIALER.contentEquals(packageName)
                || SAMSUNG_INCALL_UI.contentEquals(packageName)
                || "com.android.systemui".contentEquals(packageName);
    }

    private static int callUiPriority(CharSequence packageName) {
        if (packageName == null) return 0;
        if (GOOGLE_DIALER.contentEquals(packageName)
                || AOSP_DIALER.contentEquals(packageName)
                || SAMSUNG_DIALER.contentEquals(packageName)
                || SAMSUNG_INCALL_UI.contentEquals(packageName)) {
            return 2;
        }
        return 0;
    }

    private static boolean containsSpeakerWord(String value) {
        return value.contains("glosnik") || value.contains("glosnomow")
                || value.contains("speaker")
                || value.contains("speakerphone")
                || value.contains("lautsprecher")
                || value.contains("freisprech")
                || value.contains("динамик")
                || value.contains("громкая связь");
    }

    private static String join(CharSequence... values) {
        StringBuilder result = new StringBuilder();
        for (CharSequence value : values) {
            if (value != null && value.length() > 0) result.append(' ').append(value);
        }
        return result.toString();
    }

    private static String normalize(String value) {
        if (value == null) return "";
        String decomposed = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");
        return decomposed.toLowerCase(Locale.ROOT);
    }

}

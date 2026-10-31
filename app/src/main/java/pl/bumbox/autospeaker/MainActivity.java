package pl.bumbox.autospeaker;

import android.Manifest;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public final class MainActivity extends Activity {
    static final String PREFS = "autospeaker";
    static final String KEY_ENABLED = "enabled";
    static final String KEY_LAST_STATUS = "last_status";
    private static final int REQUEST_PHONE = 10;
    private static final String REVOLUT_URL = "https://revolut.me/r_rosiejka";
    private static final String PAYPAL_URL = "https://paypal.me/proszczok";
    private static final String PRIVACY_URL =
            "https://github.com/proszczok2002/AutoGlosnik/blob/main/PRIVACY.md";

    private FrameLayout content;
    private Button homeTab;
    private Button supportTab;
    private TextView contentHeading;
    private TextView phoneStatus;
    private TextView deviceStatus;
    private TextView serviceStatus;
    private TextView sensorStatus;
    private TextView runtimeStatus;
    private Switch enabledSwitch;
    private UpdateManager updateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateManager = new UpdateManager(this);
        setContentView(buildShell());
        showHome();
        updateManager.checkForUpdates(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (phoneStatus != null) refreshStatus();
        if (updateManager != null) updateManager.onResume();
    }

    @Override
    protected void onDestroy() {
        if (updateManager != null) updateManager.close();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        refreshStatus();
    }

    private View buildShell() {
        LinearLayout shell = new LinearLayout(this);
        shell.setOrientation(LinearLayout.VERTICAL);
        shell.setBackgroundColor(getColor(R.color.surface));

        content = new FrameLayout(this);
        shell.addView(content, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));

        View divider = new View(this);
        divider.setBackgroundColor(getColor(R.color.divider));
        shell.addView(divider, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(1)));

        LinearLayout navigation = new LinearLayout(this);
        navigation.setOrientation(LinearLayout.HORIZONTAL);
        navigation.setGravity(Gravity.CENTER);
        navigation.setPadding(dp(12), dp(10), dp(12), dp(12));
        navigation.setBackgroundColor(getColor(R.color.surface_nav));

        homeTab = navigationButton(R.string.tab_home);
        supportTab = navigationButton(R.string.tab_support);
        homeTab.setId(View.generateViewId());
        supportTab.setId(View.generateViewId());
        homeTab.setAccessibilityTraversalBefore(supportTab.getId());
        homeTab.setOnClickListener(v -> showHome());
        supportTab.setOnClickListener(v -> showSupport());
        LinearLayout.LayoutParams homeParams = new LinearLayout.LayoutParams(0, dp(64), 1f);
        homeParams.setMarginEnd(dp(6));
        navigation.addView(homeTab, homeParams);
        LinearLayout.LayoutParams supportParams = new LinearLayout.LayoutParams(0, dp(64), 1f);
        supportParams.setMarginStart(dp(6));
        navigation.addView(supportTab, supportParams);
        shell.addView(navigation);
        return shell;
    }

    private void showHome() {
        content.removeAllViews();
        content.addView(buildHomeContent());
        content.setAccessibilityPaneTitle(getString(R.string.tab_home));
        supportTab.setAccessibilityTraversalBefore(contentHeading.getId());
        updateTabStyles(true);
        refreshStatus();
    }

    private void showSupport() {
        content.removeAllViews();
        content.addView(buildSupportContent());
        content.setAccessibilityPaneTitle(getString(R.string.tab_support));
        supportTab.setAccessibilityTraversalBefore(contentHeading.getId());
        updateTabStyles(false);
    }

    private View buildHomeContent() {
        int pad = dp(20);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(pad, dp(28), pad, dp(28));
        root.setBackgroundColor(getColor(R.color.surface));

        contentHeading = text(getString(R.string.app_name), 30, true, R.color.text_primary);
        contentHeading.setId(View.generateViewId());
        contentHeading.setAccessibilityHeading(true);
        root.addView(contentHeading);

        TextView lead = text(getString(R.string.home_lead), 17, false, R.color.text_secondary);
        lead.setPadding(0, dp(8), 0, dp(22));
        root.addView(lead);

        enabledSwitch = new Switch(this);
        enabledSwitch.setText(R.string.automatic_switching);
        enabledSwitch.setTextSize(18);
        enabledSwitch.setTextColor(getColor(R.color.text_primary));
        enabledSwitch.setPadding(0, dp(10), 0, dp(18));
        SharedPreferences preferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        enabledSwitch.setChecked(preferences.getBoolean(KEY_ENABLED, true));
        enabledSwitch.setOnCheckedChangeListener((button, checked) ->
                preferences.edit().putBoolean(KEY_ENABLED, checked).apply());
        root.addView(enabledSwitch);

        root.addView(sectionTitle(R.string.configuration_status));
        deviceStatus = statusRow(root);
        phoneStatus = statusRow(root);
        serviceStatus = statusRow(root);
        sensorStatus = statusRow(root);

        Button phoneButton = primaryButton(R.string.grant_phone_permission);
        phoneButton.setOnClickListener(v -> requestPhonePermission());
        root.addView(phoneButton);

        Button accessibilityButton = primaryButton(R.string.enable_accessibility_service);
        accessibilityButton.setOnClickListener(v ->
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));
        root.addView(accessibilityButton);

        TextView restricted = text(
                getString(R.string.restricted_settings_hint), 14, false, R.color.warning);
        restricted.setPadding(0, dp(10), 0, dp(4));
        root.addView(restricted);

        Button batteryButton = secondaryButton(R.string.battery_settings);
        batteryButton.setOnClickListener(v -> openBatterySettings());
        root.addView(batteryButton);

        root.addView(sectionTitle(R.string.last_event));
        runtimeStatus = statusRow(root);

        root.addView(sectionTitle(R.string.how_to_test));
        root.addView(text(getString(R.string.test_instructions),
                16, false, R.color.text_secondary));

        TextView privacy = text(
                getString(R.string.privacy_summary), 14, false, R.color.text_secondary);
        privacy.setPadding(0, dp(22), 0, 0);
        root.addView(privacy);

        ScrollView scroll = new ScrollView(this);
        scroll.addView(root);
        return scroll;
    }

    private View buildSupportContent() {
        int pad = dp(20);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(pad, dp(28), pad, dp(28));
        root.setBackgroundColor(getColor(R.color.surface));

        contentHeading = text(getString(R.string.support_title), 30, true, R.color.text_primary);
        contentHeading.setId(View.generateViewId());
        contentHeading.setAccessibilityHeading(true);
        root.addView(contentHeading);
        TextView lead = text(getString(R.string.support_lead), 17, false, R.color.text_secondary);
        lead.setPadding(0, dp(8), 0, dp(18));
        root.addView(lead);

        LinearLayout supportCard = card();
        supportCard.addView(text(getString(R.string.revolut_title), 20, true, R.color.text_primary));
        supportCard.addView(text("revolut.me/r_rosiejka", 15, false, R.color.text_secondary));
        Button revolut = primaryButton(R.string.open_revolut);
        revolut.setOnClickListener(v -> openExternal(REVOLUT_URL));
        supportCard.addView(revolut);

        TextView spacer = new TextView(this);
        spacer.setHeight(dp(16));
        supportCard.addView(spacer);

        supportCard.addView(text(getString(R.string.paypal_title), 20, true, R.color.text_primary));
        supportCard.addView(text("paypal.me/proszczok", 15, false, R.color.text_secondary));
        Button paypal = primaryButton(R.string.open_paypal);
        paypal.setOnClickListener(v -> openExternal(PAYPAL_URL));
        supportCard.addView(paypal);
        root.addView(supportCard);

        root.addView(sectionTitle(R.string.updates_title));
        root.addView(text(getString(R.string.current_version,
                updateManager.currentVersionName()), 16, true, R.color.text_primary));
        TextView updateStatus = text(getString(R.string.update_ready_to_check),
                15, false, R.color.text_secondary);
        updateStatus.setPadding(0, dp(8), 0, dp(2));
        root.addView(updateStatus);
        updateManager.bindStatus(updateStatus);

        Button check = primaryButton(R.string.check_for_updates);
        check.setOnClickListener(v -> updateManager.checkForUpdates(true));
        root.addView(check);

        Button privacy = secondaryButton(R.string.privacy_policy);
        privacy.setOnClickListener(v -> openExternal(PRIVACY_URL));
        root.addView(privacy);

        TextView notice = text(getString(R.string.support_notice),
                13, false, R.color.text_secondary);
        notice.setPadding(0, dp(18), 0, 0);
        root.addView(notice);

        ScrollView scroll = new ScrollView(this);
        scroll.addView(root);
        return scroll;
    }

    private LinearLayout card() {
        LinearLayout result = new LinearLayout(this);
        result.setOrientation(LinearLayout.VERTICAL);
        result.setPadding(dp(18), dp(18), dp(18), dp(18));
        GradientDrawable background = new GradientDrawable();
        background.setColor(getColor(R.color.surface_card));
        background.setCornerRadius(dp(18));
        background.setStroke(dp(1), getColor(R.color.divider));
        result.setBackground(background);
        return result;
    }

    private TextView sectionTitle(int stringId) {
        TextView view = text(getString(stringId), 19, true, R.color.text_primary);
        view.setPadding(0, dp(18), 0, dp(8));
        return view;
    }

    private TextView statusRow(LinearLayout parent) {
        TextView view = text("", 16, false, R.color.text_secondary);
        view.setPadding(0, dp(5), 0, dp(5));
        parent.addView(view);
        return view;
    }

    private Button primaryButton(int stringId) {
        Button result = baseButton(stringId);
        GradientDrawable background = new GradientDrawable();
        background.setColor(getColor(R.color.brand));
        background.setCornerRadius(dp(14));
        result.setBackground(background);
        result.setTextColor(Color.WHITE);
        return result;
    }

    private Button secondaryButton(int stringId) {
        Button result = baseButton(stringId);
        GradientDrawable background = new GradientDrawable();
        background.setColor(getColor(R.color.surface_card));
        background.setCornerRadius(dp(14));
        background.setStroke(dp(1), getColor(R.color.divider));
        result.setBackground(background);
        result.setTextColor(getColor(R.color.text_primary));
        return result;
    }

    private Button baseButton(int stringId) {
        Button result = new Button(this);
        result.setText(stringId);
        result.setAllCaps(false);
        result.setTextSize(16);
        result.setTypeface(result.getTypeface(), Typeface.BOLD);
        result.setPadding(dp(14), dp(12), dp(14), dp(12));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = dp(12);
        result.setLayoutParams(params);
        return result;
    }

    private Button navigationButton(int stringId) {
        Button result = new Button(this);
        result.setText(stringId);
        result.setAllCaps(false);
        result.setTextSize(17);
        result.setTypeface(result.getTypeface(), Typeface.BOLD);
        result.setPadding(dp(10), dp(8), dp(10), dp(8));
        result.setMinimumHeight(dp(64));
        return result;
    }

    private void updateTabStyles(boolean homeSelected) {
        styleTab(homeTab, homeSelected);
        styleTab(supportTab, !homeSelected);
        homeTab.setSelected(homeSelected);
        supportTab.setSelected(!homeSelected);
    }

    private void styleTab(Button tab, boolean selected) {
        GradientDrawable background = new GradientDrawable();
        background.setColor(getColor(selected ? R.color.tab_selected : R.color.tab_unselected));
        background.setCornerRadius(dp(16));
        background.setStroke(dp(2), getColor(selected ? R.color.tab_selected_border
                : R.color.tab_unselected_border));
        tab.setBackground(background);
        tab.setTextColor(getColor(R.color.text_primary));
        tab.setStateDescription(getString(selected ? R.string.tab_selected : R.string.tab_not_selected));
    }

    private TextView text(String value, int sp, boolean bold, int color) {
        TextView result = new TextView(this);
        result.setText(value);
        result.setTextSize(sp);
        result.setTextColor(getColor(color));
        result.setLineSpacing(0, 1.12f);
        if (bold) result.setTypeface(result.getTypeface(), Typeface.BOLD);
        return result;
    }

    private void refreshStatus() {
        if (deviceStatus == null || enabledSwitch == null) return;
        String manufacturer = Build.MANUFACTURER == null ? "Android" : Build.MANUFACTURER;
        String model = Build.MODEL == null ? "" : Build.MODEL;
        TelecomManager telecomManager = getSystemService(TelecomManager.class);
        String dialerPackage = telecomManager == null ? null : telecomManager.getDefaultDialerPackage();
        deviceStatus.setText(getString(R.string.detected_device,
                friendlyManufacturer(manufacturer), model, friendlyDialer(dialerPackage)));
        deviceStatus.setTextColor(getColor(isSupportedDialer(dialerPackage)
                ? R.color.success : R.color.warning));

        boolean phoneGranted = checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED;
        setStatus(phoneStatus, getString(R.string.phone_permission), phoneGranted);
        setStatus(serviceStatus, getString(R.string.autospeaker_service), isOurServiceEnabled());

        SensorManager manager = getSystemService(SensorManager.class);
        boolean hasSensor = manager != null
                && manager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null;
        sensorStatus.setText(hasSensor
                ? getString(R.string.sensor_available)
                : getString(R.string.sensor_missing));
        sensorStatus.setTextColor(getColor(hasSensor ? R.color.success : R.color.warning));
        enabledSwitch.setEnabled(hasSensor);

        String lastStatus = getSharedPreferences(PREFS, MODE_PRIVATE)
                .getString(KEY_LAST_STATUS, getString(R.string.no_events_yet));
        runtimeStatus.setText(lastStatus);
        runtimeStatus.setTextColor(getColor(R.color.text_secondary));
    }

    private void setStatus(TextView target, String name, boolean ok) {
        target.setText(getString(ok ? R.string.status_ready : R.string.status_needs_setup, name));
        target.setTextColor(getColor(ok ? R.color.success : R.color.warning));
    }

    private void requestPhonePermission() {
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE);
        } else {
            refreshStatus();
        }
    }

    private boolean isOurServiceEnabled() {
        android.view.accessibility.AccessibilityManager manager =
                getSystemService(android.view.accessibility.AccessibilityManager.class);
        if (manager == null) return false;
        List<AccessibilityServiceInfo> enabled = manager.getEnabledAccessibilityServiceList(
                AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
        String expected = getPackageName() + "/" + AutoSpeakerService.class.getName();
        for (AccessibilityServiceInfo info : enabled) {
            if (info.getResolveInfo() == null || info.getResolveInfo().serviceInfo == null) continue;
            String actual = info.getResolveInfo().serviceInfo.packageName + "/"
                    + info.getResolveInfo().serviceInfo.name;
            if (TextUtils.equals(expected, actual)) return true;
        }
        return false;
    }

    private void openBatterySettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Exception ignored) {
            startActivity(new Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS));
        }
    }

    private void openExternal(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception exception) {
            Toast.makeText(this, R.string.cannot_open_link, Toast.LENGTH_LONG).show();
        }
    }

    private static String friendlyManufacturer(String manufacturer) {
        if (manufacturer == null || manufacturer.isBlank()) return "Android";
        String lower = manufacturer.toLowerCase(Locale.ROOT);
        if (lower.equals("samsung")) return "Samsung";
        if (lower.equals("google")) return "Google";
        if (lower.equals("xiaomi")) return "Xiaomi";
        return Character.toUpperCase(manufacturer.charAt(0)) + manufacturer.substring(1);
    }

    private String friendlyDialer(String packageName) {
        if (packageName == null) return getString(R.string.dialer_not_detected);
        if (packageName.equals("com.samsung.android.dialer")) {
            return getString(R.string.dialer_samsung);
        }
        if (packageName.equals("com.google.android.dialer")) {
            return getString(R.string.dialer_google);
        }
        if (packageName.equals("com.android.dialer")) {
            return getString(R.string.dialer_android);
        }
        return getString(R.string.dialer_other, packageName);
    }

    private static boolean isSupportedDialer(String packageName) {
        return "com.samsung.android.dialer".equals(packageName)
                || "com.google.android.dialer".equals(packageName)
                || "com.android.dialer".equals(packageName);
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}

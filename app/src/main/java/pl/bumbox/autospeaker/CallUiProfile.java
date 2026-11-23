package pl.bumbox.autospeaker;

final class CallUiProfile {
    static final String GOOGLE = "com.google.android.dialer";
    static final String AOSP = "com.android.dialer";
    static final String SAMSUNG = "com.samsung.android.dialer";
    static final String SAMSUNG_INCALL = "com.samsung.android.incallui";
    private static final String SYSTEM_UI = "com.android.systemui";

    enum Mode { GOOGLE_PHONE, AOSP_PHONE, SAMSUNG_PHONE, NONE }

    private CallUiProfile() { }

    static Mode select(String defaultDialer, CharSequence observedCallUi) {
        if (defaultDialer != null) return modeForPackage(defaultDialer);
        return modeForPackage(observedCallUi);
    }

    static Mode modeForPackage(CharSequence packageName) {
        if (packageName == null) return Mode.NONE;
        if (GOOGLE.contentEquals(packageName)) return Mode.GOOGLE_PHONE;
        if (AOSP.contentEquals(packageName)) return Mode.AOSP_PHONE;
        if (SAMSUNG.contentEquals(packageName)
                || SAMSUNG_INCALL.contentEquals(packageName)) {
            return Mode.SAMSUNG_PHONE;
        }
        return Mode.NONE;
    }

    static boolean acceptsEvent(Mode mode, CharSequence packageName) {
        if (packageName == null) return false;
        return switch (mode) {
            case GOOGLE_PHONE -> GOOGLE.contentEquals(packageName);
            case AOSP_PHONE -> AOSP.contentEquals(packageName);
            case SAMSUNG_PHONE -> SAMSUNG.contentEquals(packageName)
                    || SAMSUNG_INCALL.contentEquals(packageName);
            case NONE -> false;
        };
    }

    static boolean usesVerifiedSpeakerButton(Mode mode, String model, String device) {
        return mode == Mode.SAMSUNG_PHONE && ("dm1q".equalsIgnoreCase(device)
                || (model != null && model.regionMatches(true, 0, "SM-S911", 0, 7)));
    }

    static boolean isVerifiedSpeakerControl(String id, String label) {
        String details = id + " " + label;
        if (details.contains("mute") || details.contains("microphone")
                || details.contains("mikrofon") || details.contains("wycisz")
                || details.contains("stumm") || details.contains("микрофон")
                || details.contains("без звука")) return false;
        return id.contains("speaker") || containsSpeakerWord(label, false);
    }

    static boolean containsSpeakerWord(String value, boolean legacyGoogle) {
        return value.contains("glosnik") || value.contains("glosnomow")
                || value.contains("speaker")
                || value.contains("speakerphone")
                || (!legacyGoogle && (value.contains("lautsprecher")
                || value.contains("freisprech")
                || value.contains("динамик")
                || value.contains("громкая связь")));
    }

    static Boolean speakerStateFromLabel(String label) {
        if (!containsSpeakerWord(label, false)) return null;

        // An action describes what will happen on click, not the current state.
        if (label.contains("wylacz glos") || label.contains("turn speaker off")
                || label.contains("lautsprecher ausschalten") || label.contains("выключить динамик")) {
            return true;
        }
        if (label.contains("wlacz glos") || label.contains("turn speaker on")
                || label.contains("lautsprecher einschalten") || label.contains("включить динамик")) {
            return false;
        }
        if (label.contains("wylaczony") || label.contains("speaker off")
                || label.contains("lautsprecher aus") || label.contains("динамик выключен")) {
            return false;
        }
        if (label.contains("wlaczony") || label.contains("speaker on")
                || label.contains("lautsprecher ein") || label.contains("динамик включен")) {
            return true;
        }
        return null;
    }

    static boolean acceptsWindow(Mode mode, CharSequence packageName) {
        if (packageName == null) return false;
        return acceptsEvent(mode, packageName)
                || (mode != Mode.NONE && SYSTEM_UI.contentEquals(packageName));
    }

    static int windowPriority(Mode mode, CharSequence packageName) {
        if (!acceptsEvent(mode, packageName)) return 0;
        return mode == Mode.GOOGLE_PHONE ? 1 : 2;
    }
}

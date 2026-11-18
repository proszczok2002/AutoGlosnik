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

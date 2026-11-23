package pl.bumbox.autospeaker;

final class GalaxyS23CallProfile {
    private GalaxyS23CallProfile() { }

    static boolean matches(CallUiProfile.Mode mode, String model, String device) {
        return mode == CallUiProfile.Mode.SAMSUNG_PHONE && ("dm1q".equalsIgnoreCase(device)
                || (model != null && model.regionMatches(true, 0, "SM-S911", 0, 7)));
    }

    static boolean isSpeakerControl(String id, String label) {
        String details = id + " " + label;
        if (details.contains("mute") || details.contains("microphone")
                || details.contains("mikrofon") || details.contains("wycisz")
                || details.contains("stumm") || details.contains("микрофон")
                || details.contains("без звука")) return false;
        return id.contains("speaker") || CallUiProfile.containsSpeakerWord(label, false);
    }

    static Boolean speakerStateFromLabel(String label) {
        if (!CallUiProfile.containsSpeakerWord(label, false)) return null;

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
}

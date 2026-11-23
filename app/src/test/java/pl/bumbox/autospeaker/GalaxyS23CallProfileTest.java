package pl.bumbox.autospeaker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public final class GalaxyS23CallProfileTest {
    @Test
    public void verifiedSpeakerButtonIsLimitedToGalaxyS23WithSamsungPhone() {
        CallUiProfile.Mode samsung = CallUiProfile.Mode.SAMSUNG_PHONE;
        assertTrue(GalaxyS23CallProfile.matches(samsung, "SM-S911B", null));
        assertTrue(GalaxyS23CallProfile.matches(samsung, "SC-51D", "dm1q"));
        assertFalse(GalaxyS23CallProfile.matches(samsung, "SM-S931B", null));
        assertFalse(GalaxyS23CallProfile.matches(samsung, null, null));
        assertFalse(GalaxyS23CallProfile.matches(
                CallUiProfile.Mode.GOOGLE_PHONE, "SM-S911B", "dm1q"));
        assertFalse(GalaxyS23CallProfile.matches(
                CallUiProfile.Mode.AOSP_PHONE, "SM-S911B", "dm1q"));
    }

    @Test
    public void verifiedSpeakerButtonRejectsMicrophoneAndPositionalControls() {
        assertTrue(GalaxyS23CallProfile.isSpeakerControl("fourth_button", "glosnik"));
        assertTrue(GalaxyS23CallProfile.isSpeakerControl("speaker_button", ""));
        assertTrue(GalaxyS23CallProfile.isSpeakerControl("", "lautsprecher ausschalten"));
        assertFalse(GalaxyS23CallProfile.isSpeakerControl("fourth_button", ""));
        assertFalse(GalaxyS23CallProfile.isSpeakerControl("audio_route", "dzwiek"));
        assertFalse(GalaxyS23CallProfile.isSpeakerControl("mute_button", "glosnik"));
        assertFalse(GalaxyS23CallProfile.isSpeakerControl("speaker_button", "wycisz mikrofon"));
        assertFalse(GalaxyS23CallProfile.isSpeakerControl("speaker_button", "микрофон"));
    }

    @Test
    public void speakerActionsDescribeTheOppositeOfTheCurrentState() {
        assertEquals(Boolean.TRUE, GalaxyS23CallProfile.speakerStateFromLabel("turn speaker off"));
        assertEquals(Boolean.FALSE, GalaxyS23CallProfile.speakerStateFromLabel("turn speaker on"));
        assertEquals(Boolean.TRUE, GalaxyS23CallProfile.speakerStateFromLabel("lautsprecher ausschalten"));
        assertEquals(Boolean.FALSE, GalaxyS23CallProfile.speakerStateFromLabel("lautsprecher einschalten"));
        assertEquals(Boolean.FALSE, GalaxyS23CallProfile.speakerStateFromLabel("speaker off"));
        assertEquals(Boolean.TRUE, GalaxyS23CallProfile.speakerStateFromLabel("speaker on"));
    }
}

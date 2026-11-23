package pl.bumbox.autospeaker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public final class CallUiProfileTest {
    @Test
    public void googlePhoneIsSelectedByDefaultDialerOnAnyManufacturer() {
        CallUiProfile.Mode mode = CallUiProfile.select(CallUiProfile.GOOGLE, CallUiProfile.SAMSUNG);
        assertEquals(CallUiProfile.Mode.GOOGLE_PHONE, mode);
        assertTrue(CallUiProfile.acceptsEvent(mode, CallUiProfile.GOOGLE));
        assertFalse(CallUiProfile.acceptsEvent(mode, CallUiProfile.SAMSUNG));
        assertTrue(CallUiProfile.acceptsWindow(mode, "com.android.systemui"));
        assertEquals(1, CallUiProfile.windowPriority(mode, CallUiProfile.GOOGLE));
    }

    @Test
    public void samsungKeepsItsOwnInCallUi() {
        CallUiProfile.Mode mode = CallUiProfile.select(CallUiProfile.SAMSUNG, CallUiProfile.GOOGLE);
        assertEquals(CallUiProfile.Mode.SAMSUNG_PHONE, mode);
        assertTrue(CallUiProfile.acceptsEvent(mode, CallUiProfile.SAMSUNG));
        assertTrue(CallUiProfile.acceptsEvent(mode, CallUiProfile.SAMSUNG_INCALL));
        assertFalse(CallUiProfile.acceptsWindow(mode, CallUiProfile.GOOGLE));
        assertEquals(2, CallUiProfile.windowPriority(mode, CallUiProfile.SAMSUNG_INCALL));
    }

    @Test
    public void verifiedSpeakerButtonIsLimitedToGalaxyS23WithSamsungPhone() {
        CallUiProfile.Mode samsung = CallUiProfile.Mode.SAMSUNG_PHONE;
        assertTrue(CallUiProfile.usesVerifiedSpeakerButton(samsung, "SM-S911B", null));
        assertTrue(CallUiProfile.usesVerifiedSpeakerButton(samsung, "SC-51D", "dm1q"));
        assertFalse(CallUiProfile.usesVerifiedSpeakerButton(samsung, "SM-S931B", null));
        assertFalse(CallUiProfile.usesVerifiedSpeakerButton(samsung, null, null));
        assertFalse(CallUiProfile.usesVerifiedSpeakerButton(
                CallUiProfile.Mode.GOOGLE_PHONE, "SM-S911B", "dm1q"));
    }

    @Test
    public void verifiedSpeakerButtonRejectsMicrophoneAndPositionalControls() {
        assertTrue(CallUiProfile.isVerifiedSpeakerControl("fourth_button", "glosnik"));
        assertTrue(CallUiProfile.isVerifiedSpeakerControl("speaker_button", ""));
        assertTrue(CallUiProfile.isVerifiedSpeakerControl("", "lautsprecher ausschalten"));
        assertFalse(CallUiProfile.isVerifiedSpeakerControl("fourth_button", ""));
        assertFalse(CallUiProfile.isVerifiedSpeakerControl("audio_route", "dzwiek"));
        assertFalse(CallUiProfile.isVerifiedSpeakerControl("mute_button", "glosnik"));
        assertFalse(CallUiProfile.isVerifiedSpeakerControl("speaker_button", "wycisz mikrofon"));
        assertFalse(CallUiProfile.isVerifiedSpeakerControl("speaker_button", "микрофон"));
    }

    @Test
    public void speakerActionsDescribeTheOppositeOfTheCurrentState() {
        assertEquals(Boolean.TRUE, CallUiProfile.speakerStateFromLabel("turn speaker off"));
        assertEquals(Boolean.FALSE, CallUiProfile.speakerStateFromLabel("turn speaker on"));
        assertEquals(Boolean.TRUE, CallUiProfile.speakerStateFromLabel("lautsprecher ausschalten"));
        assertEquals(Boolean.FALSE, CallUiProfile.speakerStateFromLabel("lautsprecher einschalten"));
        assertEquals(Boolean.FALSE, CallUiProfile.speakerStateFromLabel("speaker off"));
        assertEquals(Boolean.TRUE, CallUiProfile.speakerStateFromLabel("speaker on"));
    }

    @Test
    public void observedCallUiIsFallbackOnlyWhenDefaultIsUnknown() {
        assertEquals(CallUiProfile.Mode.GOOGLE_PHONE,
                CallUiProfile.select(null, CallUiProfile.GOOGLE));
        assertEquals(CallUiProfile.Mode.SAMSUNG_PHONE,
                CallUiProfile.select(null, CallUiProfile.SAMSUNG_INCALL));
        assertEquals(CallUiProfile.Mode.NONE,
                CallUiProfile.select("example.unsupported.dialer", CallUiProfile.GOOGLE));
        assertFalse(CallUiProfile.acceptsEvent(CallUiProfile.Mode.NONE, null));
    }

    @Test
    public void aospDialerDoesNotSelectOtherDialers() {
        CallUiProfile.Mode mode = CallUiProfile.select(CallUiProfile.AOSP, CallUiProfile.GOOGLE);
        assertEquals(CallUiProfile.Mode.AOSP_PHONE, mode);
        assertTrue(CallUiProfile.acceptsEvent(mode, CallUiProfile.AOSP));
        assertFalse(CallUiProfile.acceptsEvent(mode, CallUiProfile.GOOGLE));
        assertFalse(CallUiProfile.acceptsEvent(mode, CallUiProfile.SAMSUNG_INCALL));
    }
}

package talkingclock;

import talkingclock.internal.EnglishTimeConverter;
import talkingclock.internal.OSXVoiceService;

public class TalkingClock {

    public static void main(String[] args) {
        final EnglishTimeConverter converter = new EnglishTimeConverter();
        final OSXVoiceService voiceService = new OSXVoiceService();

        voiceService.textToVoice(converter.convertToEnglish("01:00"));
    }
}

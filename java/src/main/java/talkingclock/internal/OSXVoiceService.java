package talkingclock.internal;

import java.io.IOException;

public class OSXVoiceService {

    public void textToVoice(String text) {
        try {
            Runtime.getRuntime().exec(String.format("say --voice=Alex '%s'", text));
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute", e);
        }
    }
}

package marsrover.internal;

import com.google.common.collect.Maps;

import java.util.Map;

public enum Bearing {
    NORTH("N"),
    EAST("E"),
    SOUTH("S"),
    WEST("W");

    private final String characterCode;

    Bearing(String characterCode) {
        this.characterCode = characterCode;
    }

    public String getCharacterCode() {
        return characterCode;
    }

    private static final Map<String, Bearing> characterCodeToBearing = Maps.newHashMap();

    static {
        for (Bearing bearing : Bearing.values()) {
            characterCodeToBearing.put(bearing.characterCode, bearing);
        }
    }

    public static Bearing fromCharacterCode(String characterCode) {
        if (characterCodeToBearing.containsKey(characterCode)) {
            return characterCodeToBearing.get(characterCode);
        } else {
            throw new IllegalArgumentException("Illegal bearing code " + characterCode);
        }
    }
}

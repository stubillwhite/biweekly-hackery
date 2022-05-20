package marsrover.internal;

import com.google.common.collect.Maps;

import java.util.Map;

public enum Instruction {
    ROTATE_LEFT("L"),
    ROTATE_RIGHT("R"),
    MOVE_FORWARD("M");

    private final String characterCode;

    Instruction(String characterCode) {
        this.characterCode = characterCode;
    }

    private static final Map<String, Instruction> characterCodeToInstruction = Maps.newHashMap();

    static {
        for (Instruction instruction : Instruction.values()) {
            characterCodeToInstruction.put(instruction.characterCode, instruction);
        }
    }

    public static Instruction fromCharacterCode(String characterCode) {
        if (characterCodeToInstruction.containsKey(characterCode)) {
            return characterCodeToInstruction.get(characterCode);
        }
        else {
            throw new IllegalArgumentException("Illegal instruction code " + characterCode);
        }
    }
}

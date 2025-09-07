package isel.meic.thesis.proto.dataTypes.enums;

public enum Type {
    NORMAL,
    AZUL,
    VERDE,
    EXPRESSO;

    public static Type fromInt(int priorityInput) {
        switch (priorityInput) {
            case 1:
                return EXPRESSO;
            case 2:
                return VERDE;
            case 3:
                return AZUL;
            case 4:
                return NORMAL;
            default:
                // Handle invalid input, you might throw an IllegalArgumentException
                // or return a default value like NORMAL, depending on your needs.
                System.err.println("Invalid priority input: " + priorityInput + ". Defaulting to NORMAL.");
                return NORMAL;
        }
    }
}

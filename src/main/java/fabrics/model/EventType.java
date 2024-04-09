package fabrics.model;

public enum EventType {
    START_PRODUCTION(1),
    FINISH_PRODUCTION(-1);

    private final int code;

    EventType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

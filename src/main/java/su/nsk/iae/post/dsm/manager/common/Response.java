package su.nsk.iae.post.dsm.manager.common;

public class Response {

    private final ResponseType type;
    private final Object value;

    public Response(ResponseType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public ResponseType getType() {
        return this.type;
    }

    public Object getValue() {
        return value;
    }

    public enum ResponseType {
        FREE_PORT
    }
}
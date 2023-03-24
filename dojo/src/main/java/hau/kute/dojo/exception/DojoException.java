package hau.kute.dojo.exception;

public class DojoException extends RuntimeException {

    private String code;
    private String message;
    private Object[] params;

    public DojoException() {
        super();
    }

    public DojoException(String code, String message, Object... params) {
        super(message);
        this.code = code;
        this.message = message;
        this.params = params;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Object[] getParams() {
        return params;
    }
}

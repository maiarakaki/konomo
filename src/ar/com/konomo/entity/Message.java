package ar.com.konomo.entity;

public class Message { private int code;
    private  Object body;
    private String message;

    public Message(int code, String message, Object body) {
        this.code = code;
        this.message = message;
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public Object getBody() {
        return body;
    }

    public String getMessage() {
        return message;
    }
}
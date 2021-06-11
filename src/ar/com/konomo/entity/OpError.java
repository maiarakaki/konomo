package ar.com.konomo.entity;
import java.util.HashMap;

public class OpError {
    private HashMap<Integer, String> errors;

    public HashMap<Integer, String> getErrors() {
        if (errors == null) {
            errors = new HashMap<>();
        }
        return errors;
    }

    public void add(int code, String message) {
        getErrors().put(code, message);
    }

    public void add(OpError opError) {
        getErrors().putAll(opError.getErrors());
    }

}

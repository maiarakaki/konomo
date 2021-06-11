package ar.com.konomo.entity;
import java.util.ArrayList;
import java.util.List;

public class OpError {
    private List<String> errorss;

    public List <String> getErrors(){
        if (errorss == null) {
            errorss = new ArrayList<>();
        }
        return errorss;
    }

    public void add(String message) {
        if (errorss == null) {
            errorss = new ArrayList<>();
        }
        errorss.add(message);
    }

    public void addAll(OpError opError) {
        if (errorss == null) {
            errorss = new ArrayList<>();
        }
        errorss.addAll(opError.getErrors());
    }



}

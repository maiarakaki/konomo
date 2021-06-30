package ar.com.konomo.errorHandling;

public class ServerRunningException extends Exception{

    public ServerRunningException(String message){
        super(message);
    }
}

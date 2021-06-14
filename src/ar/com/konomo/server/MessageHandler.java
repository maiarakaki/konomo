package ar.com.konomo.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class MessageHandler implements HttpHandler {
    private String message = "Bienvenido";

    public void handle(HttpExchange t) throws IOException {
        String response = message;
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
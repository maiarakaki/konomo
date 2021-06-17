package ar.com.konomo.server.handlers;

import ar.com.konomo.server.Converter;
import ar.com.konomo.server.Message;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HandshakeHandler implements HttpHandler {
    private static final int OK = 200;
    private static boolean connected = false;
    private static String ip;


    @Override
    public void handle(HttpExchange t) throws IOException {

        String address = t.getRemoteAddress().getHostName();
        ip = address;
        System.out.println("Conexión establecida con "+ address + "!");
        Message message = new Message(200, "Ok", null);
        String json = Converter.toJson(message);

        sendResponse(OK, json, t);
        connected = true;
    }

    public static boolean isConnected(){
        return connected;
    }

    public void sendResponse(int statusCode, String response, HttpExchange exchange) {
        try {
            exchange.sendResponseHeaders(statusCode, response.length());
            OutputStream outStream = exchange.getResponseBody();
            outStream.write(response.getBytes(StandardCharsets.UTF_8));
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getIp(){
        return ip;
    }

}


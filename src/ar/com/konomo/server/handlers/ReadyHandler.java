package ar.com.konomo.server.handlers;

import ar.com.konomo.display.Initializer;
import ar.com.konomo.server.Delivery;
import ar.com.konomo.server.logic.Game;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ReadyHandler implements HttpHandler {
    private static final int OK = 200;
    private volatile static boolean ready = false;


    @Override
    public void handle(HttpExchange t) throws IOException {

        if (ready) Game.switchARoo();
        ready = true;
        sendResponse(OK, "", t);
    }

    public static boolean isReady(){
        return ready;
    }

    public static void setReady(boolean ready) {
        ReadyHandler.ready = ready;
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




}
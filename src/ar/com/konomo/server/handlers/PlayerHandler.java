package ar.com.konomo.server.handlers;

import ar.com.konomo.managers.GM;
import ar.com.konomo.server.Converter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class PlayerHandler implements HttpHandler {
    private final int OK = 200;
    private final int NOPE = 999;
    private GM manager;

    @Override
    public void handle(HttpExchange t) throws IOException {

        String playerName = Converter.fromJson(t.getRequestBody(), String.class);

        manager.getPlayer2().setName(playerName);

        sendResponse(OK, "OK", t);
    }

    public void sendResponse(int statusCode, String response, HttpExchange exchange) {
        try {
            byte[] bs = response.getBytes("UTF-8");
            exchange.sendResponseHeaders(statusCode, bs.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bs);
            os.flush();
            os.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public PlayerHandler(GM manager) {
        this.manager = manager;
    }
}

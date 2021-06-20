package ar.com.konomo.server.handlers;

import ar.com.konomo.enums.GameState;
import ar.com.konomo.server.logic.Game;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class GameStateHandler implements HttpHandler {
    private static final int OK = 200;



    @Override
    public void handle(HttpExchange t) throws IOException {

        sendResponse(OK, "Game Over", t);
        Game.gameState = GameState.OVER;
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
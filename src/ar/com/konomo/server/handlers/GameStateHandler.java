package ar.com.konomo.server.handlers;

import ar.com.konomo.entity.Message;
import ar.com.konomo.entity.Shinobi;
import ar.com.konomo.enums.GameState;
import ar.com.konomo.managers.GM;
import ar.com.konomo.server.Converter;
import ar.com.konomo.server.logic.Game;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GameStateHandler implements HttpHandler {
    private static final int OK = 200;
    private static GameState gameState = GameState.ON;


    @Override
    public void handle(HttpExchange t) throws IOException {

        gameState = GameState.OVER;
        ReadyHandler.setReady(true);
        sendResponse(OK, "", t);
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

    public static GameState getGameState() {
        return gameState;
    }

    public static void setGameState(GameState gameState) {
        GameStateHandler.gameState = gameState;
    }
}
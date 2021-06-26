package ar.com.konomo.server.handlers;

import ar.com.konomo.entity.Intention;
import ar.com.konomo.managers.GM;
import ar.com.konomo.server.Converter;
import ar.com.konomo.server.IntentionPack;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ActionHandler implements HttpHandler {
    private GM manager;
    private final int OK = 200;

    @Override
    public void handle(HttpExchange t) throws IOException {
        IntentionPack clientIntentionPack =  Converter.fromJson(t.getRequestBody(), IntentionPack.class);
        Map<Integer, Intention> clientIntentions = new HashMap<>();
        Integer i = 0;
        for (Intention intention: clientIntentionPack.getIntentions()
        ) {
            clientIntentions.put(i, intention);
            i++;
        }

        manager.updateBoards(manager.getPlayer2(), clientIntentions, manager.getPlayer1().getLocalBoard());
        clientIntentionPack.setKnownBoard(manager.getPlayer2().getEnemyBoard());

        String json = Converter.toJson(clientIntentionPack);
        sendResponse(OK, json, t);

    }


    public void sendResponse(int statusCode, String response, HttpExchange exchange) {
        try {

            exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
            OutputStream outStream = exchange.getResponseBody();
            outStream.write(response.getBytes(StandardCharsets.UTF_8));
            outStream.close();




        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ActionHandler(GM manager) {
        this.manager = manager;
    }
}

package ar.com.konomo.server.handlers;

import ar.com.konomo.entity.Intention;
import ar.com.konomo.entity.OpError;
import ar.com.konomo.entity.Shinobi;
import ar.com.konomo.managers.GM;
import ar.com.konomo.server.Converter;
import ar.com.konomo.entity.IntentionPack;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntentionHandler implements HttpHandler {
    private GM manager;
    private final int OK = 200;

    @Override
    public void handle(HttpExchange t) throws IOException {
        manager.getErrors().clear();
        IntentionPack clientIntentionPack =  Converter.fromJson(t.getRequestBody(), IntentionPack.class);

        boolean allGood = manager.validate(clientIntentionPack.getIntentions(), manager.getPlayer2());

        OpError errors = manager.getErrors();


        clientIntentionPack.setErrors(errors);
        clientIntentionPack.setAllGood(allGood);

        String json = Converter.toJson(clientIntentionPack);

        if (allGood) {
            Map <Integer, Intention> map =  mapIntentions(clientIntentionPack.getIntentions(), manager.getPlayer2().getMyNinjas());
            manager.updateBoards(manager.getPlayer2(), map, manager.getPlayer1().getLocalBoard());
        }

        sendResponse(OK, json, t);
    }

    //TODO check if this madafacka breaks
    private Map<Integer, Intention> mapIntentions(List<Intention> cilentIntentions, List<Shinobi> ninjas){
        Map <Integer, Intention> map = new HashMap<>();
        int i=0;

        for (Shinobi ninja: ninjas
             ) {
            if (ninja.isAlive()){
                map.put(i, cilentIntentions.get(i));
                i++;
            }
        }
        return map;
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

    public IntentionHandler(GM manager) {
        this.manager = manager;
    }
}
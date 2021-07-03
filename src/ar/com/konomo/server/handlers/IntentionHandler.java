package ar.com.konomo.server.handlers;

import ar.com.konomo.entity.OpError;
import ar.com.konomo.managers.GM;
import ar.com.konomo.server.Converter;
import ar.com.konomo.entity.IntentionPack;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class IntentionHandler implements HttpHandler {
    private GM manager;
    private final int OK = 200;

    @Override
    public void handle(HttpExchange t) throws IOException {
//mepa q esto puede no ser necesario si el manager permamentemente se está guardando esta info al validar las intenciones...
        manager.getErrors().clear();
        IntentionPack clientIntentionPack =  Converter.fromJson(t.getRequestBody(), IntentionPack.class);
        //Map<Integer, Intention> intentions = clientIntentionPack.getIntentions();
        //List<Shinobi> clientNinjas = clientIntentionPack.getNinjaList();



        boolean allGood = manager.validate(clientIntentionPack.getIntentions(), manager.getPlayer2());

        OpError errors = manager.getErrors();


        clientIntentionPack.setErrors(errors);
        clientIntentionPack.setAllGood(allGood);

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

    public IntentionHandler(GM manager) {
        this.manager = manager;
    }
}
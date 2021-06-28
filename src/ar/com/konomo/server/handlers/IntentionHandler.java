package ar.com.konomo.server.handlers;

import ar.com.konomo.entity.Intention;
import ar.com.konomo.entity.OpError;
import ar.com.konomo.entity.Shinobi;
import ar.com.konomo.managers.GM;
import ar.com.konomo.server.Converter;
import ar.com.konomo.server.IntentionPack;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class IntentionHandler implements HttpHandler {
    private GM manager;
    private final int OK = 200;

    @Override
    public void handle(HttpExchange t) throws IOException {
//mepa q esto puede no ser necesario si el manager permamentemente se está guardando esta info al validar las intenciones...
        IntentionPack clientIntentionPack =  Converter.fromJson(t.getRequestBody(), IntentionPack.class);
        //Map<Integer, Intention> intentions = clientIntentionPack.getIntentions();
        //List<Shinobi> clientNinjas = clientIntentionPack.getNinjaList();



        boolean allGood = manager.validate(clientIntentionPack.getIntentions());
        OpError errors = manager.getErrors();


        clientIntentionPack.setErrors(errors);
        clientIntentionPack.setAllGood(allGood);

        String json = Converter.toJson(clientIntentionPack);
        manager.getErrors().clear();

        sendResponse(OK, json, t);
    }


    public void sendResponse(int statusCode, String response, HttpExchange exchange) {
        try {
            /*exchange.sendResponseHeaders(statusCode, response.length());
            OutputStream outStream = exchange.getResponseBody();
            outStream.write(response.getBytes(StandardCharsets.UTF_8));*/
/*            byte[] bs = response.getBytes("UTF-8");
            exchange.sendResponseHeaders(statusCode, bs.length);
            OutputStream os = exchange.getResponseBody();*/
/*            outStream.flush();
            outStream.close();*/
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
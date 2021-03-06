package ar.com.konomo.server.handlers;

import ar.com.konomo.managers.GM;
import ar.com.konomo.operators.EventMessageLog;
import ar.com.konomo.server.Converter;
import ar.com.konomo.entity.Message;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class EventsHandler implements HttpHandler {
    private GM manager;
    private final int OK = 200;
    private final int NOPE = 400;

    @Override
    public void handle(HttpExchange t) throws IOException {

        EventMessageLog eventLog = manager.getEventLog();


        Message message = new Message(200, "", eventLog);
        String json = Converter.toJson(message);
        sendResponse(OK, json, t);

    }

    public EventsHandler(GM manager) {
        this.manager = manager;
    }


    public void sendResponse(int statusCode, String response, HttpExchange exchange) {
        try {
            exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
            OutputStream outStream = exchange.getResponseBody();
            outStream.write(response.getBytes(StandardCharsets.UTF_8));
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}

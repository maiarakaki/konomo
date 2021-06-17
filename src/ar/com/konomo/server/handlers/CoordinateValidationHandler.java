package ar.com.konomo.server.handlers;

import ar.com.konomo.entity.Coordinate;
import ar.com.konomo.entity.OpError;
import ar.com.konomo.entity.Player;
import ar.com.konomo.managers.GM;
import ar.com.konomo.server.Converter;
import ar.com.konomo.server.PlayerCoords;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CoordinateValidationHandler implements HttpHandler {
    private final int OK = 200;
    private final int NOPE = 400;
    private GM manager;

    @Override
    public void handle(HttpExchange t) throws IOException {
        PlayerCoords playerCoords =Converter.fromJson(t.getRequestBody(), PlayerCoords.class);
        Player player = playerCoords.getPlayer();
        List<Coordinate> coordinateList = playerCoords.getCoords();
        OpError errors = playerCoords.getErrors();

        boolean allGood = manager.validate(coordinateList, player);
        errors = manager.getErrors();
        playerCoords.setErrors(errors);

        String json = Converter.toJson(playerCoords);

        if(allGood) {sendResponse(OK, json, t);} else {
            sendResponse(NOPE, json, t);
        }

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

    public CoordinateValidationHandler(GM manager) {
        this.manager = manager;
    }
}

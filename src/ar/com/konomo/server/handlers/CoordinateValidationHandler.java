package ar.com.konomo.server.handlers;

import ar.com.konomo.entity.Coordinate;
import ar.com.konomo.entity.OpError;
import ar.com.konomo.entity.Shinobi;
import ar.com.konomo.managers.GM;
import ar.com.konomo.server.Converter;
import ar.com.konomo.entity.PlayerCoords;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class CoordinateValidationHandler implements HttpHandler {
    private final int OK = 200;
    private final int NOPE = 999;
    private GM manager;


    @Override
    public void handle(HttpExchange t) throws IOException {

        PlayerCoords playerCoords = Converter.fromJson(t.getRequestBody(), PlayerCoords.class);
        List<Coordinate> coordinateList = playerCoords.getCoords();
        OpError errors = playerCoords.getErrors();
        List <Shinobi> ninjaList = playerCoords.getNinjaList();

        //boolean allGood = manager.validate(coordinateList, ninjaList);
        boolean allGood = manager.validate(coordinateList, manager.getPlayer2());
        errors = manager.getErrors();
        playerCoords.setNinjaList(manager.getPlayer2().getMyNinjas());
        playerCoords.setErrors(errors);
        playerCoords.setAllGood(allGood);

        String json = Converter.toJson(playerCoords);
        sendResponse(OK, json, t);
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

    public CoordinateValidationHandler(GM manager) {
        this.manager = manager;
    }
}

package ar.com.konomo.server.handlers;

import ar.com.konomo.entity.Board;
import ar.com.konomo.managers.GM;
import ar.com.konomo.server.Converter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BoardHandler implements HttpHandler {
    private GM manager;
    private final int OK = 200;

    @Override
    public void handle(HttpExchange t) throws IOException {
//mepa q esto puede no ser necesario si el manager permamentemente se está guardando esta info al validar las intenciones...
        Board board = (manager.getPlayer1().getLocalBoard());

        String json = Converter.toJson(board);
        sendResponse(OK, json, t);

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

    public BoardHandler(GM manager) {
        this.manager = manager;
    }
}

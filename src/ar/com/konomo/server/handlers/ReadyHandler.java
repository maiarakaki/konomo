package ar.com.konomo.server.handlers;

import ar.com.konomo.display.Initializer;
import ar.com.konomo.server.Delivery;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class ReadyHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Initializer.setReady(true);
        Delivery.sendResponse(200, "OK", exchange);
    }


}

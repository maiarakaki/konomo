package ar.com.konomo.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {
    private MessageHandler myHandler = new MessageHandler();
    private HttpServer server;

    public Server() {

        try {
            /**
             * cambiar el ip al ip que me da hamachi en el primer argumento de InetSocketAdress
             * sino, poner acá la ipv4 de ipconfig
             */
            server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/connect", new MessageHandler());
            server.createContext("/myninja", new MessageHandler());

            server.setExecutor(null); // creates a default executor
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}


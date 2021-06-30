package ar.com.konomo.server;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;

import ar.com.konomo.errorHandling.ServerRunningException;
import ar.com.konomo.managers.GM;
import ar.com.konomo.server.handlers.*;
import com.sun.net.httpserver.HttpServer;

public class Server {
    private HttpServer server;
    private GM manager;
    public static int PORT;

    public Server(GM manager, int port) throws ServerRunningException{
        this.PORT = 8000+port;
        this.manager = manager;
        try {
            /**
             * cambiar el ip al ip que me da hamachi en el primer argumento de InetSocketAdress
             * sino, poner acá la ipv4 de ipconfig
             */

            try {
                server = HttpServer.create(new InetSocketAddress(PORT), 0);
                server.createContext("/connect", new HandshakeHandler());
                server.createContext("/ready", new ReadyHandler());
                server.createContext("/validate", new CoordinateValidationHandler(manager));
                server.createContext("/intentions", new IntentionHandler(manager));
                server.createContext("/actions", new ActionHandler(manager));
                server.createContext("/hitMe", new AttackHandler(manager));
                server.createContext("/events", new EventsHandler(manager));
                server.createContext("/player", new PlayerHandler(manager));
                server.createContext("/gameState", new GameStateHandler());

                server.setExecutor(null); // creates a default executor
                server.start();
            } catch (BindException exception) {
                throw new ServerRunningException("Server is still running!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void stop() {
        try{
            server.stop(0);
        } catch (NullPointerException ex) {
            System.out.println(ex.getMessage());
        }

    }
}
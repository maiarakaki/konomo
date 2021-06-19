package ar.com.konomo.server;

import java.lang.reflect.Type;

public class Requester {
    private static String host;

    public Message sendPost(Object object, String endpoint, Type type) {
        return Delivery.doPost(host + endpoint, object, Message.class);
    }

    public PlayerCoords sendPost(PlayerCoords object, String endpoint) {
        return Delivery.doPost(host + endpoint, object);
    }

    public Message sendGet(String endpoint, Type type) {
        return Delivery.doGet(host + endpoint, type);
    }

    public void setIp(String ip) {
        host = "http://"+ ip;
    }
}
package ar.com.konomo.server;

import ar.com.konomo.entity.Board;
import ar.com.konomo.entity.Intention;
import ar.com.konomo.entity.Shinobi;
import ar.com.konomo.operators.AttackLogger;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

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
    public AttackLogger sendGet(String endpoint) {
        return Delivery.doGet(host + endpoint);
    }

    public void setIp(String ip) {
        host = "http://"+ ip;
    }

    public String sendPost(String object, String endpoint) {
        return Delivery.doPost(host + endpoint, object);
    }

    public IntentionPack sendPost( IntentionPack object, String endpoint) {
        return Delivery.doPost(host + endpoint, object);
    }
    public Board sendPost(Board object, String endpoint) {
        return Delivery.doPost(host + endpoint, object);
    }

}
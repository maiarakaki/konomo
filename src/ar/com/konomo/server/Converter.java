package ar.com.konomo.server;

import ar.com.konomo.entity.Player;
import com.google.gson.Gson;

public class Converter {
        static Gson gson = new Gson();

        public Converter(Gson gson) {
            this.gson = gson;
        }
/*
        public static String toJson(Message message){
            return gson.toJson(message);
        }

        public static Message jsonToMessage(String json){
            return gson.fromJson(json,Message.class);
        }*/

        public static String toJson(Player player){
            return gson.toJson(player);
        }

        public static Player jsonToPlayer(String json){
            return gson.fromJson(json,Player.class);
        }
    }

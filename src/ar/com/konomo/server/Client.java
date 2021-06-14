package ar.com.konomo.server;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Client {

    private String ipOpponent="192.168.56.1";
    private int port=8000;

    public String recieveMessage(){
        try {
            URL url = new URL("http://"+ipOpponent+":"+port+"/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("accept", "application/json");
            InputStream responseStream = connection.getInputStream();

            Scanner s = new Scanner(responseStream).useDelimiter("\\A");
            String result = s.hasNext() ? s.next() : "";

            return result;

        }catch (Exception ex){
            return ex.getMessage();
        }
    }

    public String getIpOpponent() {
        return ipOpponent;
    }

    public void setIpOpponent(String ipOpponent) {
        this.ipOpponent = ipOpponent;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
package ar.com.konomo.server;

import ar.com.konomo.entity.Board;
import ar.com.konomo.entity.IntentionPack;
import ar.com.konomo.entity.Message;
import ar.com.konomo.entity.PlayerCoords;
import ar.com.konomo.operators.AttackLogger;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;


public class Delivery  {

    public static final int BAD_REQUEST = 400;
    public static final int NOT_FOUND= 404;


    public static void ok(String response, HttpExchange exchange) {
        sendResponse(200, response, exchange);
    }

    public static void badRequest(String response, HttpExchange exchange) {
        sendResponse(BAD_REQUEST, response, exchange);
    }

    public static void sendResponse(int statusCode, String response, HttpExchange exchange) {
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

    public static Message doGet(String endpoint, Type type) {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
        Message response = null;

        try {
            HttpRequest httpRequest = requestFactory.buildGetRequest(new GenericUrl(endpoint));
            HttpResponse httpResponse = httpRequest.execute();
            int responseCode = httpResponse.getStatusCode();
            Object responseBody = Converter.fromJson(httpResponse.parseAsString(),Message.class);
            response = new Message(responseCode, "", responseBody);//TODO
            httpResponse.disconnect();
        } catch (IOException e) {
            System.out.println("Connection refused");
        }

        return response;
    }

    public static Message doPost(String endpoint, Object body, Type responseType) {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
        Message response = null;
        try {
            HttpContent content = ByteArrayContent.fromString(null, Converter.toJson(body));
            HttpRequest httpRequest = requestFactory.buildPostRequest(new GenericUrl(endpoint), content);
            httpRequest.getHeaders().setContentType("application/json");
            HttpResponse httpResponse = httpRequest.execute();
            int responseCode = httpResponse.getStatusCode();
            try {
                Object responseBody = Converter.fromJson(httpResponse.parseAsString(), responseType);
                response = new Message(responseCode, "", responseBody);// TODO
            } catch (JsonSyntaxException ex) {

            }
            httpResponse.disconnect();
        } catch (HttpResponseException e) {
//            System.out.println(e.getMessage());

        } catch (IOException e) {
            System.out.println("Connection refused");
        }

        return response;
    }


    public static PlayerCoords doPost(String endpoint, PlayerCoords body) {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
        PlayerCoords response = null;
        try {
            HttpContent content = ByteArrayContent.fromString(null, Converter.toJson(body));
            HttpRequest httpRequest = requestFactory.buildPostRequest(new GenericUrl(endpoint), content);
            httpRequest.setReadTimeout(120000);
            httpRequest.getHeaders().setContentType("application/json");
            HttpResponse httpResponse = httpRequest.execute();
            int responseCode = httpResponse.getStatusCode();
            response = Converter.fromJson(httpResponse.getContent(), PlayerCoords.class);
            httpResponse.disconnect();
        } catch (HttpResponseException e) {
//            System.out.println(e.getMessage());

        } catch (IOException e) {
            System.out.println("Connection refused");
        }

        return response;
    }

    public static AttackLogger doGet(String endpoint) {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
        AttackLogger response = null;

        try {
            HttpRequest httpRequest = requestFactory.buildGetRequest(new GenericUrl(endpoint));
            httpRequest.setReadTimeout(120000);
            HttpResponse httpResponse = httpRequest.execute();

            response = Converter.fromJson(httpResponse.parseAsString(),AttackLogger.class);
            httpResponse.disconnect();
        } catch (IOException e) {
            System.out.println("Connection refused");
        }

        return response;
    }

    public static String doPost(String endpoint, String body) {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
        String response = null;
        try {
            HttpContent content = ByteArrayContent.fromString(null, Converter.toJson(body));
            HttpRequest httpRequest = requestFactory.buildPostRequest(new GenericUrl(endpoint), content);
            httpRequest.getHeaders().setContentType("application/json");
            HttpResponse httpResponse = httpRequest.execute();
            int responseCode = httpResponse.getStatusCode();
            response = Converter.fromJson(httpResponse.getContent(), String.class);
            httpResponse.disconnect();
        } catch (HttpResponseException e) {
//            System.out.println(e.getMessage());

        } catch (IOException e) {
            System.out.println("Connection refused");
        }

        return response;
    }

    public static IntentionPack doPost(String  endpoint, IntentionPack  body) {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
        IntentionPack response = null;
        try {
            HttpContent content = ByteArrayContent.fromString(null, Converter.toJson(body));
            HttpRequest httpRequest = requestFactory.buildPostRequest(new GenericUrl(endpoint), content);
            httpRequest.setReadTimeout(120000);

            httpRequest.getHeaders().setContentType("application/json");
            HttpResponse httpResponse = httpRequest.execute();
            int responseCode = httpResponse.getStatusCode();
            response = Converter.fromJson(httpResponse.getContent(), IntentionPack.class);
            httpResponse.disconnect();
        } catch (HttpResponseException e) {
//            System.out.println(e.getMessage());

        } catch (IOException e) {
            System.out.println("Connection refused");
        }

        return response;
    }

    public static Board doPost(String  endpoint, Board  body) {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
        Board response = null;
        try {
            HttpContent content = ByteArrayContent.fromString(null, Converter.toJson(body));
            HttpRequest httpRequest = requestFactory.buildPostRequest(new GenericUrl(endpoint), content);
            httpRequest.getHeaders().setContentType("application/json");
            HttpResponse httpResponse = httpRequest.execute();
            int responseCode = httpResponse.getStatusCode();
            response = Converter.fromJson(httpResponse.getContent(), Board.class);
            httpResponse.disconnect();
        } catch (HttpResponseException e) {
//            System.out.println(e.getMessage());

        } catch (IOException e) {
            System.out.println("Connection refused");
        }

        return response;
    }
}

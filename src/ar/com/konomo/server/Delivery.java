package ar.com.konomo.server;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class Delivery {

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
            Object responseBody = Converter.fromJson(httpResponse.parseAsString(),type);
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
            Object responseBody = Converter.fromJson(httpResponse.parseAsString(), responseType);
            response = new Message(responseCode, "", responseBody);// TODO
            httpResponse.disconnect();
        } catch (HttpResponseException e) {
            System.out.println(e.getMessage());
/*            MyError error = Converter.fromJson(e.getContent(), Error.class);
            try {
                response = new Message (error.getStatusCode(), error.getMessage(), error);

            }catch (Exception e2) {
                System.out.println(e2.getMessage() + "Maite tenía razón!!!");
            }
            System.out.println("sarasasasasa");*/
        } catch (IOException e) {
            System.out.println("Connection refused");
        }

        return response;
    }
}

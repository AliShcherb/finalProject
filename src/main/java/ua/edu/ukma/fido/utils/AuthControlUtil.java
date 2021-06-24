package ua.edu.ukma.fido.utils;

import com.sun.net.httpserver.HttpExchange;

import java.io.OutputStream;

public class AuthControlUtil {
    public static void sendUnauthorized(HttpExchange httpExchange) {
        try {
            String answer = "Unauthorized";
            OutputStream outputStream = httpExchange.getResponseBody();
            httpExchange.sendResponseHeaders(401, answer.length());

            outputStream.write(answer.getBytes());
            outputStream.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void sendNotFound(HttpExchange httpExchange) {
        try {
            String answer = "Not found";
            OutputStream outputStream = httpExchange.getResponseBody();
            httpExchange.sendResponseHeaders(404, answer.length());

            outputStream.write(answer.getBytes());
            outputStream.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void sendConflict(HttpExchange httpExchange) {
        try {
            String answer = "Conflict";
            OutputStream outputStream = httpExchange.getResponseBody();
            httpExchange.sendResponseHeaders(409, answer.length());

            outputStream.write(answer.getBytes());
            outputStream.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

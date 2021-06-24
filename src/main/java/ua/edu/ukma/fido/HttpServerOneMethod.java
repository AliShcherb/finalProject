package ua.edu.ukma.fido;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import ua.edu.ukma.fido.controllers.ApiGoodController;
import ua.edu.ukma.fido.controllers.LoginController;
import ua.edu.ukma.fido.views.HtmlView;
import ua.edu.ukma.fido.views.View;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServerOneMethod {

    final static int HTTP_SERVER_PORT = 8888;

    final static View VIEW = new HtmlView();

    public static void main(String[] args) {
        try {
            ApiGoodController.setView(VIEW);

            HttpServer server = HttpServer.create();

            server.bind(new InetSocketAddress(HTTP_SERVER_PORT), 0);

            HttpContext context = server.createContext(ApiGoodController.PATH); // http://localhost:8888/hello
            context.setHandler(ApiGoodController::serve);

            HttpContext context2 = server.createContext(LoginController.PATH); // http://localhost:8888/hello
            context2.setHandler(LoginController::serve);

            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
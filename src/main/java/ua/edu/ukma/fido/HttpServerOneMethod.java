package ua.edu.ukma.fido;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import ua.edu.ukma.fido.controllers.*;
import ua.edu.ukma.fido.db.DB;
import ua.edu.ukma.fido.db.Table;
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
            LoginController.setView(VIEW);
            ApiGoodId.setView(VIEW);
           GetAllProducts.setView(VIEW);
            ApiGoodName.setView(VIEW);
            InsertProdController.setView(VIEW);

            HttpServer server = HttpServer.create();

            server.bind(new InetSocketAddress(HTTP_SERVER_PORT), 0);

            HttpContext context = server.createContext(ApiGoodController.PATH); // http://localhost:8888/hello
            context.setHandler(ApiGoodController::serve);

            HttpContext context2 = server.createContext(LoginController.PATH); // http://localhost:8888/hello
            context2.setHandler(LoginController::serve);

            HttpContext context3 = server.createContext(ApiGoodId.PATH); // http://localhost:8888/hello
            context3.setHandler(ApiGoodId::serve);

            HttpContext context4 = server.createContext(GetAllProducts.PATH); // http://localhost:8888/hello
            context4.setHandler(GetAllProducts::serve);

            HttpContext context5 = server.createContext(ApiGoodName.PATH); // http://localhost:8888/hello
            context5.setHandler(ApiGoodName::serve);

            HttpContext context6 = server.createContext(InsertProdController.PATH); // http://localhost:8888/hello
            context6.setHandler((InsertProdController::serve));

            DB.connect();
            Table.create();
            Table.cleanDatabase();

            Table.insert(1, "MOLOKO",29.19,5,"dairy");
            Table.insert(2, "GRECHKA",40,100,"grain");//❤

            /*Integer idThree = Table.insert("MORKVA",10,20);
            Integer idFour = Table.insert("KOVBASKA",150,1);
            Integer idFive = Table.insert("POMIDORKA",11,220);*/

            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

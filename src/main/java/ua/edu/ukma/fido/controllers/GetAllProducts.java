package ua.edu.ukma.fido.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;

import com.sun.net.httpserver.HttpExchange;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ua.edu.ukma.fido.db.Product;
import ua.edu.ukma.fido.db.Table;
import ua.edu.ukma.fido.dto.Response;
import ua.edu.ukma.fido.models.User;
import ua.edu.ukma.fido.token.TokenHolder;
import ua.edu.ukma.fido.utils.AuthControlUtil;
import ua.edu.ukma.fido.utils.KeyUtil;
import ua.edu.ukma.fido.views.View;

public class GetAllProducts {
    private static View view;
    public static String PATH = "/api/all";

        public static void setView(View newView) {
            view = newView;
        }



        public static String generateToken(String id, String subject) {
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);

            Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(KeyUtil.getSecret()), SignatureAlgorithm.HS256.getJcaName());

            JwtBuilder builder = Jwts.builder().setId(id)
                    .setIssuedAt(now)
                    .setSubject(subject)
                    .setIssuer(KeyUtil.getIssuer())
                    .signWith(hmacKey)
                    .setExpiration(Date.from(Instant.now().plus(1L, ChronoUnit.HOURS)));

            return builder.compact();
        }

        public static void serve(HttpExchange httpExchange) throws IOException {


       if (!TokenHolder.validateToken(TokenHolder.getToken())) {
          AuthControlUtil.sendUnauthorized(httpExchange);
           return;
        }

            if("GET".equals(httpExchange.getRequestMethod())) {
               List<Product> productList =  Table.selectAll();
               List <String> strNames = productList.stream().map(new Function<Product, String>() {
                   @Override
                   public String apply(Product product) {
                       return product.getProductName()+" "+product.getPrice()+" "+product.getAmount()+" "+product.getProductCategory();
                   }
               }).collect(Collectors.toList());

              // printResultSet("All: ", p);
                String str= String.join(",",strNames);
                byte[] response = str.getBytes(StandardCharsets.UTF_8);
                httpExchange.sendResponseHeaders(200, response.length);
                httpExchange.getResponseBody()
                        .write(response);
                httpExchange.getResponseBody()
                        .flush();

            }


          /*  Response response = new Response();
            response.setTemplate("allProducts");
            response.setStatusCode(200);
            response.setHttpExchange(httpExchange);
            view.view(response);*/
        }

        private static Map<String, String> getWwwFormUrlencodedBody(HttpExchange exchange) throws IOException {
            HashMap<String, String> map = new HashMap<>();
            String body = getStringFromInputStream(exchange.getRequestBody());

            for (String parameter : body.split("&")) {
                String[] keyValue = parameter.split("=");

                if (keyValue.length != 2)
                    return null;

                map.put(keyValue[0], keyValue[1]);

            }

            return map;
        }

        private static String getStringFromInputStream(InputStream is) throws IOException {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null ) {
                sb.append(line);
            }
            return sb.toString();
        }

    public static void printResultSet(String resultSetName, ResultSet resultSet) {
        System.out.println(resultSetName + ":");
        try {
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") +  "\t" + resultSet.getString("name"));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        System.out.println();
    }
}

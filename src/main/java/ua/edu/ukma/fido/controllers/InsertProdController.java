package ua.edu.ukma.fido.controllers;

import com.sun.net.httpserver.HttpExchange;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ua.edu.ukma.fido.db.Product;
import ua.edu.ukma.fido.db.Table;
import ua.edu.ukma.fido.dto.Response;
import ua.edu.ukma.fido.token.TokenHolder;
import ua.edu.ukma.fido.utils.AuthControlUtil;
import ua.edu.ukma.fido.utils.KeyUtil;
import ua.edu.ukma.fido.views.View;

import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InsertProdController {
    private static View view;
    public static String PATH = "/api/insert/product";

    public static void setView(View newView) {
        view = newView;
    }

    public static boolean validateToken(HttpExchange httpExchange) {
        String jwt = httpExchange.getRequestHeaders().getFirst("Authorization").substring("Bearer ".length());

        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(KeyUtil.getSecret()), SignatureAlgorithm.HS256.getJcaName());
        Claims claims = Jwts.parserBuilder().setSigningKey(hmacKey).build().parseClaimsJws(jwt).getBody();

        Instant instant = claims.getExpiration().toInstant() ;
        long expirationMillis = instant.toEpochMilli() ;
        long nowMillis = System.currentTimeMillis();

        return expirationMillis > nowMillis && claims.getIssuer().equals(KeyUtil.getIssuer()) && claims.getSubject().equals("Authorization");
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

        if("POST".equals(httpExchange.getRequestMethod())) {
     Map<String, String> requestParams = getWwwFormUrlencodedBody(httpExchange);
         String name = requestParams.get("name");
        Double price = Double.parseDouble(requestParams.get("price"));  ;
       int amount = Integer.parseInt(requestParams.get("amount"));
          int category =Integer.parseInt(requestParams.get("category"));
          Table.insert(name, price,amount,category);
          List<Product> productList =  Table.selectAll();
           List <String> strNames = productList.stream().map(new Function<Product, String>() {
              @Override
           public String apply(Product product) {
                  return product.getProductName();
             }
          }).collect(Collectors.toList());

            redirect(httpExchange,"/get/table");
            return;
            // printResultSet("All: ", p);
//            String str= String.join(",",strNames);
//            byte[] response = str.getBytes(StandardCharsets.UTF_8);
//            httpExchange.sendResponseHeaders(200, response.length);
//            httpExchange.getResponseBody()
//                    .write(response);
//            httpExchange.getResponseBody()
//                    .flush();

        }
       /* if (!validateToken(httpExchange)) {
            AuthControlUtil.sendUnauthorized(httpExchange);
            return;
        }*/

        Response response = new Response();
        response.setTemplate("insertProduct");
        response.setStatusCode(200);
        response.setHttpExchange(httpExchange);
        view.view(response);
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
    private static void redirect(HttpExchange exchange, String redirectUrl) throws IOException {
        exchange.getResponseHeaders().set("Location", redirectUrl);
        exchange.sendResponseHeaders(301, -1);
        exchange.close();
    }
}

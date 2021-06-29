package ua.edu.ukma.fido.controllers;

import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import com.sun.net.httpserver.HttpExchange;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ua.edu.ukma.fido.dto.Response;
import ua.edu.ukma.fido.models.User;
import ua.edu.ukma.fido.utils.AuthControlUtil;
import ua.edu.ukma.fido.utils.KeyUtil;
import ua.edu.ukma.fido.views.View;

public class ApiGoodController {
    private static View view;
    public static String PATH = "/api/good";

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
                .setExpiration(now);

        return builder.compact();
    }

    public static void serve(HttpExchange httpExchange) {
        String token = generateToken("1", "2");
        String requestMethod = httpExchange.getRequestMethod();

     /*   if (!validateToken(httpExchange)) {
            AuthControlUtil.sendUnauthorized(httpExchange);
            return;
        }*/

        User user = new User("Trokhym", "Babych", "trosha_b");
        Response response = new Response();
        response.setTemplate("view_user");
        response.setStatusCode(200);
        response.setData(user);
        response.setHttpExchange(httpExchange);

        view.view(response);
    }
}

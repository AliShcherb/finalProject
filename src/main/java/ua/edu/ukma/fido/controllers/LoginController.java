package ua.edu.ukma.fido.controllers;

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

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

public class LoginController {
    private static View view;
    public static String PATH = "/login";

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
                .setExpiration(now);

        return builder.compact();
    }

    public static void serve(HttpExchange httpExchange) {
        String token = generateToken("1", "2");
        String requestMethod = httpExchange.getRequestMethod();

        if (!validateToken(httpExchange)) {
            AuthControlUtil.sendUnauthorized(httpExchange);
            return;
        }

        String objectId = "";
        String path = httpExchange.getRequestURI().getPath();
        String subPath = path.substring(PATH.length());
        if (!subPath.equals("")) {
            objectId = subPath;
        }

        User user = new User();
        if (!objectId.equals(""))
            user.setId(Integer.valueOf(objectId));
        user.setFirstName("Trokhym");
        user.setLastName("Babych");
        user.setInst("trosha_b");

        Response response = new Response();
        response.setTemplate("view_user");
        response.setStatusCode(200);
        response.setData(user);
        response.setHttpExchange(httpExchange);

        view.view(response);
    }
}

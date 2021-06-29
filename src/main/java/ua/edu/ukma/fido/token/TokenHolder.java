package ua.edu.ukma.fido.token;

import com.sun.net.httpserver.HttpExchange;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ua.edu.ukma.fido.utils.KeyUtil;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;

public class TokenHolder
{

    private static String TOKEN;

    public static String getToken() {
        return TOKEN;
    }

    public static void setToken(String TOKEN) {
        TokenHolder.TOKEN = TOKEN;
    }
    public static boolean validateToken(String jwt) {
       if(jwt==null)
         return   false;

        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(KeyUtil.getSecret()), SignatureAlgorithm.HS256.getJcaName());
        Claims claims = Jwts.parserBuilder().setSigningKey(hmacKey).build().parseClaimsJws(jwt).getBody();

        Instant instant = claims.getExpiration().toInstant() ;
        long expirationMillis = instant.toEpochMilli() ;
        long nowMillis = System.currentTimeMillis();

        return expirationMillis > nowMillis && claims.getIssuer().equals(KeyUtil.getIssuer()) && claims.getSubject().equals("Authorization");
    }

}

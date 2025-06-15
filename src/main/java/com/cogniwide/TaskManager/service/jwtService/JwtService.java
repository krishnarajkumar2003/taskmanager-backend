package com.cogniwide.TaskManager.service.jwtService;
import com.cogniwide.TaskManager.customException.CustomException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class JwtService {

    private final String SECRET_KEY;
    public JwtService(){
        SECRET_KEY = generateSecretKey();
    }

    private String generateSecretKey() {
       try {
           KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSha256");
           SecretKey secretKey = keyGenerator.generateKey();
           return Base64.getEncoder().encodeToString(secretKey.getEncoded());
       }catch (NoSuchAlgorithmException ex){
           throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR,"Error generating secret key: Algorithm not found.");
       }
    }

    public String generateJwtToken(String email){
        return Jwts
                .builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyStream = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyStream);
    }

    public Claims extractClaims(String token){
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
    }

    public String extractSubject(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean validateToken(String subject, String email, String token) {
        return (subject.equals(email) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
            return extractClaims(token).getExpiration().before(new Date());
    }
}
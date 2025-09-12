package it.BeGear.E_commerce.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import it.BeGear.E_commerce.Entity.Utente;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private byte[] secretKey = "5F4DCC3B5AA765D61D8327DEB882CF99B4B2D9E6B4DA2A7C1B8B92F2F8D1A1CE".getBytes(StandardCharsets.UTF_8);

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    public String generateToken(Utente utente) {
        return generateToken(new HashMap<>(), utente);
    }

    public String generateToken(Map<String, Object> extraClaims, Utente utente) {
        return buildToken(extraClaims, utente);
    }

    private String buildToken(Map<String, Object> extraClaims, Utente utente) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(utente.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 30 * 1000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

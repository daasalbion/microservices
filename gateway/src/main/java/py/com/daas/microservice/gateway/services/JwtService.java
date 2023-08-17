package py.com.daas.microservice.gateway.services;

import java.security.Key;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

    private static final String ROLES_KEY = "roles";

    private final String secretKey;

    public JwtService(@Value("${security.jwt.token.secret-key}") String secretKey) {
        this.secretKey = secretKey;
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public List<SimpleGrantedAuthority> getRoles(final String token) {
        try {
            List<Map<String, String>> roleClaims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get(ROLES_KEY, List.class);

            return roleClaims.stream()
                    .map(roleClaim -> new SimpleGrantedAuthority(roleClaim.get("authority")))
                    .toList();
        } catch (JwtException | IllegalArgumentException e) {
            // Handle exception (e.g., log error, return empty list)
            return Collections.emptyList();
        }
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

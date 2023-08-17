package py.com.daas.microservice.auth.services.impl;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import py.com.daas.microservice.auth.services.JwtService;

@Component
public class JwtServiceImpl implements JwtService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtServiceImpl.class);

    private final String secretKey;
    private final long validityInMilliseconds;

    public JwtServiceImpl(
        @Value("${security.jwt.token.secret-key}") String secretKey,
        @Value("${security.jwt.token.expiration}") long validityInMilliseconds
    ) {
        this.secretKey = secretKey;
        this.validityInMilliseconds = validityInMilliseconds;
    }

    @Override
    public Optional<String> generateToken(String username, List<String> authorities) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(ROLES_KEY, authorities);
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + validityInMilliseconds);
        try {
            return Optional.of(Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)
                    .setIssuedAt(now)
                    .setExpiration(expiresAt)
                    .signWith(getSignKey(), SignatureAlgorithm.HS256)
                    .compact());
        } catch (Exception ex) {
            LOGGER.error("User = {} token generation failed", username, ex);
            return Optional.empty();
        }
    }

    @Override
    public boolean validateToken(final String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            LOGGER.error("Token = {} generation failed", token, ex);
            return false;
        }
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

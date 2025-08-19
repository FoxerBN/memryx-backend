package sk.foxer.flashcard.domain.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sk.foxer.flashcard.domain.model.AppUser;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

/**
 * Service for generating and validating JWT tokens for authentication.
 */
@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secret;

    /**
     * Generates a JWT token for a given user with a specified expiration.
     *
     * @param user       The authenticated user
     * @param expiration How long the token should be valid
     * @return JWT token as String
     */
    public String generateToken(AppUser user, Duration expiration) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(expiration)))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Checks whether the given JWT token is valid (i.e. correctly signed and not expired).
     *
     * @param token JWT token
     * @return true if valid, false if expired or invalid
     */
    public boolean isValid(String token) {
        try {
            // If parsing succeeds and token is not expired, it's valid
            Claims claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            // Any exception (parse error, expired, invalid signature, ...) means not valid
            return false;
        }
    }

    /**
     * Extracts the username (subject) from the JWT token.
     *
     * @param token JWT token
     * @return username as String
     */
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Extracts the userId claim from the JWT token.
     *
     * @param token JWT token
     * @return userId as Long, or null if not present
     */
    public Long extractUserId(String token) {
        Object v = getClaims(token).get("userId");
        return v == null ? null : Long.valueOf(v.toString());
    }

    /**
     * Parses the JWT token and returns its claims.
     *
     * @param token JWT token
     * @return Claims object
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
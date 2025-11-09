package com.mvsaude.clinic.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

@Service
public class JwtService {

    private final String secretConfig;
    private final long expirationSeconds;

    private SecretKey signingKey;
    private Duration expiration;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration:3600}") long expirationSeconds // segundos
    ) {
        this.secretConfig = secret;
        this.expirationSeconds = expirationSeconds;
    }

    @PostConstruct
    void init() {
        byte[] keyBytes = decodeSecretFlexible(secretConfig);
        if (keyBytes.length < 32) { // 256 bits
            throw new IllegalArgumentException(
                    "jwt.secret fraco: precisa ter >= 32 bytes. " +
                            "Use Base64/Base64URL de 32 bytes (openssl rand -base64 32) " +
                            "ou um texto com >= 32 caracteres."
            );
        }
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.expiration = Duration.ofSeconds(expirationSeconds);
    }

    private static byte[] decodeSecretFlexible(String secret) {
        // 1) Tenta Base64 padrão
        try {
            return Decoders.BASE64.decode(secret);
        } catch (RuntimeException ignore) { /* não é Base64 padrão */ }

        // 2) Tenta Base64URL (tem '-' e '_')
        try {
            return Decoders.BASE64URL.decode(secret);
        } catch (RuntimeException ignore) { /* não é Base64URL */ }

        // 3) Usa como texto puro (UTF-8)
        return secret.getBytes(StandardCharsets.UTF_8);
    }

    public String generate(String username, String role) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expiration.toMillis());
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token);
    }
}

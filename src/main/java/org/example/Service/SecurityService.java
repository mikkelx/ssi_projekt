package org.example.Service;

import io.jsonwebtoken.*;
import org.example.util.Role;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

import static spark.Spark.before;
import static spark.Spark.halt;

public class SecurityService {

    private final static String issuer = "SSI-Project-App";
    private final static long ttlMillis = 3600000; //godzina

    private static SecurityService instance;

    private String apiKey;

    private SecurityService() {
        this.apiKey = this.generateApiKey();
    }

    public static SecurityService getInstance() {
        if (instance == null) {
            synchronized (UserService.class) {
                if (instance == null) {
                    instance = new SecurityService();
                }
            }
        }
        return instance;
    }

    public void registerSecurityRoutes() {
        before("/protected/*", (request, response) -> {
            String authHeader = request.headers("Authorization");
            Claims claims = validateAndDecodeJWT(authHeader);

            request.attribute("userId", claims.getId());
            request.attribute("username", claims.getSubject());
            request.attribute("role", claims.get("role", String.class));
        });

        before("/admin/*", (request, response) -> {
            String authHeader = request.headers("Authorization");
            Claims claims = validateAndDecodeJWT(authHeader);

            request.attribute("userId", claims.getId());
            request.attribute("username", claims.getSubject());
            request.attribute("role", claims.get("role", String.class));

            if(!claims.get("role", String.class).equals("ADMIN")) {
                halt(401, "Invalid role");
            }
        });
    }

    public String createJWT(String id, String subject, String role) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(apiKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);

        builder.claim("role", role);

        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        return builder.compact();
    }

    private Claims decodeJWT(String jwt) {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(apiKey))
                    .parseClaimsJws(jwt);

            return jws.getBody();
        } catch (SignatureException e) {
            throw new RuntimeException("JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.");
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Invalid JWT token.");
        }
    }

    private Claims validateAndDecodeJWT(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            halt(401, "Unauthorized");
        }

        String jwt = authHeader.substring(7);

        try {
            Claims claims = decodeJWT(jwt);
            return claims;
        } catch (RuntimeException e) {
            halt(401, "Invalid token or role");
        }

        return null;
    }

    private String generateApiKey() {
        return UUID.randomUUID().toString();
    }
}

package altera.food.ordering.config;

import altera.food.ordering.domain.dto.CategoryDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;

@Log4j2
@Component
public class JwtTokenProvider {

    private String key = "YdTbooqDbh5Q9fI3H9df2SCOBfKXPn1GS/5/NPKwPA78r5FrMLhgtpP81vKRekkXoy1VDJOtn5ncFqjkQpNUE95GshXRCruDgYo5CriK+hagYd65fBJcNHgT8uIO5GHNH9a97fvAa+Wy0LeVQK5Ub9Q/UhKT3F00iPO/5YnxIZK40ntoeCs1mjTH1jEZy5B5F3RhHNDG6S5fIBrfkEbAHKlsYHAH092T3JONlw==";

    private Long expiration = 600000L;

    private SecretKey secretKey;

    @PostConstruct
    public void setUpSecretKey() {
        try {
            secretKey = Keys.hmacShaKeyFor(key.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("Error generating JWT Secret Key : {}", e.getMessage());
            throw new RuntimeException("Error generating JWT Secret Key", e);
        }
    }

    public String generateToken(Authentication authentication) {
        String token = null;
        try {
            LocalDate now = LocalDate.now();
            ZoneId defaultZoneId = ZoneId.systemDefault();
            Claims clms = Jwts.claims().setSubject(authentication.getName());
            clms.put("data", new ObjectMapper().writeValueAsString(additionalInfo())); // additional info in JWT
            ObjectMapper mapper = new ObjectMapper();
            return Jwts.builder()
                    .setId(UUID.randomUUID().toString())
                    .setClaims(clms)
                    .setSubject(authentication.getName())
                    .setIssuer(authentication.getName())
                    .setIssuedAt(Date.from(Instant.now()))
                    .setExpiration(Date.from(now.plusDays(1).atStartOfDay(defaultZoneId).toInstant()))
                    .signWith(secretKey)
                    .compact();
        } catch (Exception e) {
            log.error("error get token" + e.getMessage());
        }
        return token;
    }

    public boolean validateToken(String token, HttpServletRequest request, String bodyRequest) {
        try {
            CategoryDto asPayload = new ObjectMapper().readValue(bodyRequest, CategoryDto.class);
            String username = getUsername(token);
            if (request.getRequestURI().equals("/api/v1/restaurant")) {
                return username.equals("admin");
            }
            if (request.getRequestURI().equals("/api/v1/category/byid")) {
                return validatePayloads(token, asPayload);
            }
//            Jwts.parserBuilder().setSigningKey(key.getBytes()).build().parseClaimsJws(token);
//            return true;
        } catch (SignatureException ex) {
            log.error("Invalid Jwt Signature: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Invalid Jwt Token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired Jwt Signature: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported Jwt Signature: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("Jwt claim string is empty: {}", ex.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private boolean validatePayloads(String token, CategoryDto asPayloads) throws JsonProcessingException {
        String data = getDataFromToken(token);
        CategoryDto parse = new ObjectMapper().readValue(data, CategoryDto.class);
        return parse.getId().equals(asPayloads.getId());
    }

    public String getUsername(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.getSubject();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(key.getBytes()).parseClaimsJws(token).getBody();
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private String getDataFromToken(String token) {
        return getClaimFromToken(token, claims -> (String) claims.get("data"));
    }

    private HashMap<String, Object> additionalInfo() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("id", 1);
        return data;
    }

}

package com.greenfox.dramacsoport.petclinicbackend.config.webtoken;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    @Value("${SECRET_KEY}")
    private static String SECRET_KEY;

//    public String extractUsername(String token) {
//        return null;
//    }
//
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts
//                .parserBuilder()
//                .setSigningKey(getSignInKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    private Key getSignInKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
}

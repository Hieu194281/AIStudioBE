package vn.dasvision.template.config.untils;

import java.io.Serializable;
import java.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil implements Serializable {
//

    private static final String SECRET_KEY = ConstantMessages.SECRET_KEY;
    private static final long JWT_EXPIRATION_TIME = ConstantMessages.TIME_EXPIRE;

    public String generateToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String generateToken(Map<String, Object> claims, Long JwtExpire) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JwtExpire);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }

    public boolean validateToken(String token)  throws Exception {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        }
//        catch (SignatureException ex) {
//            System.out.println("Invalid JWT signature");
//        }
        catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
        }  catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty.");
        }
        return false;
    }

//    public void disableJWT(String token, Blacklist blacklist) throws Exception{
//        blacklist.add(token);
//    }
}


package restfulapi.three.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import restfulapi.three.security.service.UserDetailImplementation;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.refreshExpirationMs}")
    private int refreshJwtExpirationMs;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public boolean validateJwtToken(String authToken){
        try{
            Jwts.parser().setSigningKey(jwtSecret)
            .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e){
            logger.error("Invalid JWT Signature: {}", e.getMessage());
        } catch (MalformedJwtException e){
            logger.error("Invalid JWT Token: {}", e.getMessage());
        } catch (ExpiredJwtException e){
            logger.error("JWT Token Expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e){
            logger.error("JWT Token Unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e){
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public String generateJwtToken(Authentication authentication){
        UserDetailImplementation principal = (UserDetailImplementation) authentication.getPrincipal();
        return Jwts.builder().setSubject(principal.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime()+jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
    }

    public String generateRefreshJwtToken(Authentication authentication){
        UserDetailImplementation principal = (UserDetailImplementation) authentication.getPrincipal();
        return Jwts.builder().setSubject(principal.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime()+refreshJwtExpirationMs))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
    }

    public String getUsernameFromJwtToken(String token){
        return Jwts.parser().setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
   }
}

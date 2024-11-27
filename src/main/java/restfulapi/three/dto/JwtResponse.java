package restfulapi.three.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class JwtResponse implements Serializable {

    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private String username;
    private String email;
    private String role;

    public JwtResponse( String accessToken, String refreshToken, String username, String email, String role){
        this.username = username;
        this.refreshToken = refreshToken;
        this.email = email;
        this.token = accessToken;
        this.role = role;
    }

    
}


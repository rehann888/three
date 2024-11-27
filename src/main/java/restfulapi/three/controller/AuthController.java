package restfulapi.three.controller;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import restfulapi.three.dto.JwtResponse;
import restfulapi.three.dto.LoginRequest;
import restfulapi.three.dto.RefreshTokenRequest;
import restfulapi.three.dto.SignUpRequest;
import restfulapi.three.entity.User;
import restfulapi.three.security.jwt.JwtUtil;
import restfulapi.three.security.service.UserDEtailServiceImplementation;
import restfulapi.three.security.service.UserDetailImplementation;
import restfulapi.three.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserDEtailServiceImplementation userDEtailServiceImplementation;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser (@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtil.generateJwtToken(authentication);
        String refreshToken = jwtUtil.generateRefreshJwtToken(authentication);
        UserDetailImplementation principal = (UserDetailImplementation)authentication.getPrincipal();
        return ResponseEntity.ok().body(new JwtResponse(token, refreshToken, principal.getUsername(), principal.getEmail(), principal.getRoles()));
    }

    @PostMapping("/signup")
    public User signup(@RequestBody SignUpRequest request) throws BadRequestException {
        User user = new User();
        user.setId(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setRoles("user");

        User created = userService.create(user);
        return created;
    }
    
    @PostMapping("/refreshToken")
    public ResponseEntity<JwtResponse> refreshToken(@RequestBody RefreshTokenRequest request){
        String token = request.getRefreshToken();
        boolean valid = jwtUtil.validateJwtToken(token);
        if (!valid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username =jwtUtil.getUsernameFromJwtToken(token);
        UserDetailImplementation userDetailImplementation = (UserDetailImplementation)userDEtailServiceImplementation.loadUserByUsername(username);
        Authentication authentication =new UsernamePasswordAuthenticationToken(userDetailImplementation, null, userDetailImplementation.getAuthorities());
        String newToken=jwtUtil.generateJwtToken(authentication);
        String refreshToken = jwtUtil.generateRefreshJwtToken(authentication);
        return ResponseEntity.ok(new JwtResponse(newToken, refreshToken, username, userDetailImplementation.getEmail(), userDetailImplementation.getRoles()));

    }
}

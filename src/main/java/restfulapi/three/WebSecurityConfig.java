package restfulapi.three;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import restfulapi.three.security.jwt.AuthEntryPointJwt;
import restfulapi.three.security.jwt.AuthTokenFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)

public class WebSecurityConfig {

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .cors(cors -> cors.disable()) // Nonaktifkan CORS (sesuaikan jika perlu)
        .csrf(csrf -> csrf.disable()) // Nonaktifkan CSRF
        .exceptionHandling(exceptions -> 
            exceptions.authenticationEntryPoint(unauthorizedHandler) // Atur Entry Point untuk error Unauthorized
        )
        .sessionManagement(session -> 
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Gunakan sesi stateless
        )
        .authorizeHttpRequests(authz -> 
            authz
                .requestMatchers(
                    "/auth/**",                // Izinkan semua akses untuk /auth/**
                    "/v3/api-docs/**",         // Izinkan akses ke dokumentasi API
                    "/swagger-ui/**",          // Izinkan akses ke Swagger UI
                    "/swagger-resources/**",   // Izinkan akses ke resources Swagger
                    "/webjars/**"              // Izinkan akses ke dependensi web Swagger
                ).permitAll()
                .anyRequest().authenticated() // Endpoint lain memerlukan autentikasi
        )
        .addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class); // Tambahkan filter autentikasi

    return http.build();
}

    

    @Bean
    AuthTokenFilter authTokenFilter (){
        return new AuthTokenFilter();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authentication) throws Exception{
        return authentication.getAuthenticationManager();
    }

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:3000");
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}




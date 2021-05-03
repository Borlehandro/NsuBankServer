package com.sibdever.nsu_bank_system_server.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sibdever.nsu_bank_system_server.security.CredentialsAuthenticationRequest;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

public class JwtCredentialsFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtConfig config;

    public JwtCredentialsFilter(AuthenticationManager authenticationManager, JwtConfig config) {
        this.authenticationManager = authenticationManager;
        this.config = config;
    }

    // You can use simple request arguments here
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try(var input = request.getInputStream()) {
            CredentialsAuthenticationRequest credentialsAuthenticationRequest = new ObjectMapper().readValue(input, CredentialsAuthenticationRequest.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentialsAuthenticationRequest.getUsername(), credentialsAuthenticationRequest.getPassword()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
                .signWith(config.getSecretKey())
                .compact();

        response.addHeader("Authorization", "Bearer " + token);
    }
}
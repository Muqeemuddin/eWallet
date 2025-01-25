package com.moneyfli.transaction_service.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class JWTFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Bearer token
        String authHeader = request.getHeader("Authorization");
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/moneyfli/v1/customer/validate-token";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        ResponseEntity<String> responseEntity = null;
        try{
            if(authHeader != null && authHeader.startsWith("Bearer ")) {
                headers.add("Authorization", authHeader);
                HttpEntity<String> requestEntity = new HttpEntity<>(headers);
                // Call the User-service to validate the token
                // If the token is invalid, the User-service will throw an exception

                responseEntity = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        requestEntity,
                        String.class
                );

                String body = responseEntity.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(body);
                String username = jsonNode.get("username").asText();
                String password = jsonNode.get("password").asText();
                List<GrantedAuthority> authorities = new ArrayList<>();
                jsonNode.get("authorities").forEach(authorityNode -> {
                    String authority = authorityNode.asText();
                    if (authority != null && !authority.isEmpty()) {
                        authorities.add(new SimpleGrantedAuthority(authority));
                    }
                });
                UserDetails userDetails = User.builder().username(username).password(password).roles("USER").build();

                if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    filterChain.doFilter(request, response);
                } else {
                    throw new RuntimeException("Invalid token");
                }
            }
        } catch (Exception e){
            System.out.println(responseEntity.getStatusCode());
            System.out.println(responseEntity.getBody());
            throw new RuntimeException("Invalid token");
        }

    }
}

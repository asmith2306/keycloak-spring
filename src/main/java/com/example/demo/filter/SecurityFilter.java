package com.example.demo.filter;

import jakarta.servlet.*;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        this.updateAuthorities();

        // Continue the filter chain
        chain.doFilter(request, response);
    }

    public void updateAuthorities() {
        // Get the current Authentication object from the SecurityContextHolder
        Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();

        if (currentAuthentication != null) {
            Object principal = currentAuthentication.getPrincipal();

            // Create a new collection of authorities with the current authorities plus updated roles/authorities
            List<GrantedAuthority> updatedAuthorities = new ArrayList<>(currentAuthentication.getAuthorities());
            List<String> auths = ((Jwt) principal).getClaim("authorities");
            updatedAuthorities.addAll(auths.stream().map(SimpleGrantedAuthority::new).toList());

            // Create a new Authentication object with the updated authorities
            Authentication updatedAuthentication = new UsernamePasswordAuthenticationToken(
                    currentAuthentication.getPrincipal(),
                    currentAuthentication.getCredentials(),
                    updatedAuthorities
            );

            // Set the updated Authentication object back to the SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
        }
    }
}

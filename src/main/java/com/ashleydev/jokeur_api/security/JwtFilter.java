package com.ashleydev.jokeur_api.security;

import com.ashleydev.jokeur_api.domain.services.UserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String jwt = parseJwt(request);

        // Si y'a pas de JWT, on continue la chaîne de filtres - pas de raison d'authentifier qui que ce soit.
        if (jwt == null) {
            chain.doFilter(request, response);
            return;
        }

        // Si on arrive ici, c'est qu'il y a un JWT. Donc :

        // On valide le token (peut lever une JwtValidationException)
        jwtUtil.validateJwtToken(jwt);

        // Si valide, on récupère l’email depuis le token
        String email = jwtUtil.getEmailFromToken(jwt);

        // On charge l’utilisateur depuis la base
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // On crée un objet d’authentification Spring Security
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        // Et on authentifie l'utilisateur pour cette requête HTTP
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

    // méthode utilitaire qui récupère le JWT
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}

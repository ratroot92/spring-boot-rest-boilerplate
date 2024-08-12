package com.evergreen.evergreenmedic.filters;

import com.evergreen.evergreenmedic.implementations.CustomUserDetailsServiceImpl;
import com.evergreen.evergreenmedic.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter implements Ordered {
    private final CustomUserDetailsServiceImpl customUserDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtFilter {}", request.getRequestURI());

        String authorizationHeader = request.getHeader("Authorization");
        String email = null;
        String jwtToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String tokenString = authorizationHeader.substring(7);
            jwtToken = tokenString.split(":")[0];
            email = jwtUtil.extractUsername(jwtToken);

        }
        log.info("JwtFilter : JWT Token: {}", jwtToken);
        log.info("JwtFilter : Email : {}", email);
        if (email != null && jwtToken != null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
            log.info("JwtFilter : Request {}:{}", request.getMethod(), request.getRequestURI());
            log.info("JwtFilter : userDetails : {}", userDetails);
            if (userDetails != null) {
                log.info("JwtFilter : jwtUtil.validateToken(jwtToken) : {}", jwtUtil.validateToken(jwtToken));
                if (jwtUtil.validateToken(jwtToken)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    log.info(authToken.toString());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public int getOrder() {
        return -10;
    }
}

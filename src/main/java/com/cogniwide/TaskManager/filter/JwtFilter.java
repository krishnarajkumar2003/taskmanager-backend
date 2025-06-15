package com.cogniwide.TaskManager.filter;

import com.cogniwide.TaskManager.customException.CustomException;
import com.cogniwide.TaskManager.entity.UserEntity;
import com.cogniwide.TaskManager.repositopry.userRepository.UserRepository;
import com.cogniwide.TaskManager.service.jwtService.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Service
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String headerAuthorization = request.getHeader("Authorization");
        String subject = null;
        String token = null;

        try {
            if (headerAuthorization != null && headerAuthorization.startsWith("Bearer ")) {
                token = headerAuthorization.substring(7);
                subject = jwtService.extractSubject(token);
            }

            if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserEntity user = userRepository.findByEmail(subject).orElseThrow(
                        () -> new CustomException(HttpStatus.UNAUTHORIZED, "User not found or unauthorized")
                );

                if (jwtService.validateToken(subject, user.getEmail(), token)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            response.setStatus(401);
            response.getWriter().write(
                    new ObjectMapper().writeValueAsString("Session Expired")
            );
        }
    }


}
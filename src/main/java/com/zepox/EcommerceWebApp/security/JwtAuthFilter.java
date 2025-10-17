package com.zepox.EcommerceWebApp.security;

import com.zepox.EcommerceWebApp.service.MyUserDetailsService;
import com.zepox.EcommerceWebApp.util.AuthUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final AuthUtil authUtil;
    private final ApplicationContext context;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{

//            Cookie[] cookies = request.getCookies();
//
//            if (cookies != null) {
//                for (Cookie cookie : cookies) {
//                    if ("jwt".equals(cookie.getName())) {
//                        token = cookie.getValue();
//                    }
//                }
//            }

            final String authHeader = request.getHeader("Authorization");
            String token = null;
            String username = null;

            if(authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
            }
            if(authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.split("Bearer ")[1];
                username = authUtil.getUsernameFromToken(token);
            }

            if(username!=null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);
                if(authUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
                filterChain.doFilter(request, response);
            }
        }catch(Exception ex){
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }
}

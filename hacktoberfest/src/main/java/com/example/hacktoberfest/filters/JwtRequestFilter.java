package com.example.hacktoberfest.filters;

import com.example.hacktoberfest.service.UserService;
import com.example.hacktoberfest.service.UsersDetails;
import com.example.hacktoberfest.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
  @Autowired
  private UserService userService;

  @Autowired
  private JWTUtils jwtUtils;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
      final String authorizationHeader = request.getHeader("Authorization");
      String username = null;
      String token = null;

      if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
        token = authorizationHeader.substring(7);
        username = jwtUtils.extractUsername(token);
      }
      if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
        UserDetails userDetails = this.userService.loadUserByUsername(username);
        if(jwtUtils.validateToken(token,userDetails)){
          UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
              new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities()
              );
          usernamePasswordAuthenticationToken
              .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
      }
      filterChain.doFilter(request, response);
  }
}

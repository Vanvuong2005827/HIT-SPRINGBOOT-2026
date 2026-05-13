// This filter is responsible for intercepting incoming HTTP requests and checking 
// for the presence of a JWT token in the Authorization header. If a token is found, 
// it validates the token, checks if it has been invalidated, and if valid, it sets 
// the authentication in the SecurityContext for further processing by Spring Security.

package com.example.base.security;

import com.example.base.constant.ErrorMessage;
import com.example.base.repository.InvalidatedTokenRepository;
import com.nimbusds.jwt.SignedJWT;
import com.example.base.base.RestData;
import com.example.base.base.RestStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  JwtProvider jwtProvider;

  UserDetailsServiceImpl userDetailsService;

  InvalidatedTokenRepository invalidatedTokenRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    // [1]. Get the Authorization header from the request
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      log.debug("No Bearer token found in request: {}", request.getRequestURI());
      filterChain.doFilter(request, response);
      return;
    }

    // [2]. Extract the token from the Authorization header
    String token = authHeader.substring(7);

    try {
      // [2.1]. Parse the token to extract claims and check if it has been invalidated
      SignedJWT signedJWT = SignedJWT.parse(token);

      String jwtId = signedJWT.getJWTClaimsSet().getJWTID();

      if (invalidatedTokenRepository.existsById(jwtId)) {
        sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, ErrorMessage.Auth.ERR_TOKEN_INVALIDATED);
        return;
      }
    } catch (ParseException e) {
      sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ErrorMessage.Auth.ERR_MALFORMED_TOKEN);
      return;
    }

    // [3]. Extract username from token and validate it
    String username = jwtProvider.extractUsername(token);

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);

      if (jwtProvider.isTokenValid(token, userDetails)) {
        if (!userDetails.isEnabled()) {
          sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, ErrorMessage.User.ERR_USER_DISABLE);
          return;
        }
        log.debug("Token valid for user: {}", username);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
            null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        log.info("Authenticated user: {}, Authorities: {}", username, userDetails.getAuthorities());
      } else {
        log.warn("Invalid token for user: {}", username);
      }
    }

    // [4]. Continue the filter chain
    filterChain.doFilter(request, response);
  }

  private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(status);

    RestData<Object> restData = new RestData<>(RestStatus.ERROR, message, null);

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(response.getOutputStream(), restData);
  }
}

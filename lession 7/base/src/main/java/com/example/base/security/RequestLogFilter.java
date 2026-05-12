// This class is responsible for logging incoming HTTP requests
// and their details (method and URI) for debugging and monitoring purposes.

package com.example.base.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class RequestLogFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String method = request.getMethod();
    String uri = request.getRequestURI();

    log.info("[{}] {}", method, uri);

    filterChain.doFilter(request, response);
  }
}
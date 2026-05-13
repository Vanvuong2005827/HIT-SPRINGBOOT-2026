// This class configures the security settings for the application, including CORS, CSRF, endpoint access rules, and JWT authentication.

package com.example.base.config;

import com.example.base.constant.RoleConstant;
import com.example.base.security.JwtAuthenticationFilter;
import com.example.base.security.RequestLogFilter;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SecurityConfig {
  // Define endpoint patterns for different access levels (public, user, admin,
  // and open API)
  @Value("${security.public-endpoints}")
  String[] PUBLIC_END_POINT;

  @Value("${security.user-endpoints}")
  String[] USER_END_POINT;

  @Value("${security.admin-endpoints}")
  String[] ADMIN_END_POINT;

  @Value("${security.swagger-endpoints}")
  String[] OPEN_API;

  final JwtAuthenticationFilter jwtAuthenticationFilter;
  final RequestLogFilter requestLogFilter;
  final com.example.base.security.RestAuthenticationEntryPoint restAuthenticationEntryPoint;

  public SecurityConfig(
      JwtAuthenticationFilter jwtAuthenticationFilter,
      com.example.base.security.RequestLogFilter requestLogFilter,
      com.example.base.security.RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.requestLogFilter = requestLogFilter;
    this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
  }

  // Main method to configure the security filter chain, defining CORS, CSRF,
  // endpoint access rules, and adding custom filters for logging and JWT
  // authentication.
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // Enable CORS with default configuration
    http.cors(Customizer.withDefaults());

    // Disable CSRF protection since we are using JWT for stateless authentication
    http.csrf(AbstractHttpConfigurer::disable);

    // Define access rules for different endpoint patterns based on user roles and
    http.authorizeHttpRequests(request -> request
        // Allow all OPTIONS requests for CORS preflight
        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
        // Define access rules for public
        .requestMatchers(PUBLIC_END_POINT).permitAll()
        // Define access rules for user endpoints (requires USER or ADMIN role)
        .requestMatchers(USER_END_POINT).hasAnyAuthority(RoleConstant.USER, RoleConstant.ADMIN)
        // Define access rules for admin endpoints (requires ADMIN role)
        .requestMatchers(ADMIN_END_POINT).hasAuthority(RoleConstant.ADMIN)
        // Define access rules for open API endpoints (permit all)
        .requestMatchers(OPEN_API).permitAll()
        .anyRequest().authenticated());

    // Add custom filters for logging requests
    http.addFilterBefore(requestLogFilter, UsernamePasswordAuthenticationFilter.class);
    // Add the JWT authentication filter
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    http.exceptionHandling(exception -> exception
        .authenticationEntryPoint(restAuthenticationEntryPoint));

    http.sessionManagement(session -> session
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }

  // Configure CORS settings to allow requests from the frontend application and
  // specify allowed methods and headers.
  @Bean
  public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
    org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
    configuration.setAllowedOrigins(java.util.List.of("http://localhost:5173"));
    configuration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
    configuration.setAllowedHeaders(
        java.util.List.of("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);
    org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(com.example.base.constant.CommonConstant.BCRYPT_STRENGTH);
  }
}
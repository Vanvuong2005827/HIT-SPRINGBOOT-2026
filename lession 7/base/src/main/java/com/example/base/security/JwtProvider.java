// This class is responsible for generating and validating JWT tokens. 
// It uses the Nimbus JOSE + JWT library to create and sign JWTs, and 
// the JJWT library to parse and validate them. The class provides methods 
// to generate a token for a given user, extract claims from a token, 
// and validate a token against user details.

package com.example.base.security;

import com.example.base.domain.entity.User;
import com.example.base.exception.InternalServerException;
import com.example.base.repository.InvalidatedTokenRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Component
@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtProvider {

  InvalidatedTokenRepository invalidatedTokenRepository;

  @NonFinal
  @Value("${jwt.secret}")
  String secretKey;

  // Generate a JWT token for the given user with the specified expiration time
  public String generateToken(User user, long expirationTime) {
    try {
      // Create JWT claims with user information and authorities
      JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
          // Set the subject to the username
          .subject(user.getUsername())
          // Set the issue time to the current time
          .issueTime(new Date())
          // Set the expiration time based on the provided expirationTime
          .expirationTime(new Date(System.currentTimeMillis() + expirationTime))
          .jwtID(UUID.randomUUID().toString())
          // Add custom claims for authorities, userId, and email,..
          .claim("authorities", List.of("ROLE_" + user.getRole().name()))
          .claim("userId", user.getId())
          .claim("email", user.getEmail())
          .build();

      // Create a signed JWT using the claims and sign it with the secret key
      SignedJWT signedJWT = new SignedJWT(
          new JWSHeader(JWSAlgorithm.HS512),
          claimsSet);

      // Sign the JWT
      signedJWT.sign(new MACSigner(secretKey.getBytes()));

      return signedJWT.serialize();

    } catch (JOSEException e) {
      throw new InternalServerException("Error while signing JWT");
    }
  }

  // Get the signing key for JWT operations
  private Key getSignInKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  // Extract the username (subject) from the JWT token
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  // Extract the expiration date from the JWT token
  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  // Extract a specific claim from the JWT token using a claims resolver function
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    final String jwtId = extractTokenId(token);
    boolean isInvalidated = invalidatedTokenRepository.existsById(jwtId);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token) && !isInvalidated;
  }

  public boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public String extractTokenId(String token) {
    return extractClaim(token, Claims::getId);
  }

  // Extract all claims from the JWT token using the secret key for validation
  private Claims extractAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}

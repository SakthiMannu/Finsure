package com.finsure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

	@Value("${app.jwt.secret}")
	private String secret;

	private SecretKey key;

	@PostConstruct
	private void init() {
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
	}

	public String generateToken(String email, String role, Long memberId) {
		
		return Jwts.builder()
				.subject(email)
				.claim("role", role)
				.claim("memberId", memberId != null ? memberId.toString() : "")
				.expiration(new Date(System.currentTimeMillis() + 999999999))
				.signWith(key)
				.compact();
	}

}
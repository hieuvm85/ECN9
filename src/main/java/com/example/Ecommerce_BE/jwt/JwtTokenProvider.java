package com.example.Ecommerce_BE.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.Ecommerce_BE.security.CustomerUserDetail;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {

	@Value("${key.jwt.secret}")
	private String JWT_SECRET;
	@Value("${key.jwt.expiration}")
	private int JWT_EXPIRATION;
	
	
	// tao ra token tu thong tin user
	public String genarateToken(CustomerUserDetail customerUserDetail) {
		Date now = new Date();
		Date dateExpried= new Date(now.getTime() + JWT_EXPIRATION);
		// tao chuoi jsontoken tu username
		return Jwts.builder()
				.setSubject(customerUserDetail.getUsername())
				.setIssuedAt(now)
				.setExpiration(dateExpried)
				.signWith(SignatureAlgorithm.HS512, JWT_SECRET)
				.compact();
	}
	
	// entrycry
	public String getUsernameByJWT(String token) {
		Claims claims =Jwts.parser().setSigningKey(JWT_SECRET)
							.parseClaimsJws(token).getBody();
		return claims.getSubject();
	}
	
	// validate thong tin chuoi jwt
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(JWT_SECRET)
			.parseClaimsJws(token).getBody();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
}

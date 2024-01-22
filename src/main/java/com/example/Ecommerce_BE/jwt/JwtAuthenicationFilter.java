package com.example.Ecommerce_BE.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.Ecommerce_BE.security.CustomerUserDetailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenicationFilter extends OncePerRequestFilter{

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	CustomerUserDetailService customerUserDetailService;
	
	private String getJwtToken(HttpServletRequest request) {
		String bearerToken= request.getHeader("Authorization");
		// kiem tra xem hearder  Authorization co thong tin jwt hay khong
		if( StringUtils.hasText(bearerToken)&& bearerToken.startsWith("Bearer "))
		{
			return bearerToken.substring(7);
		}
		return null;
	}
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			String jwt = getJwtToken(request);
			if(StringUtils.hasText(jwt)&& jwtTokenProvider.validateToken(jwt)) {
				//lay chuoi username tu token
				String username=jwtTokenProvider.getUsernameByJWT(jwt);
				UserDetails userDetails =customerUserDetailService.loadUserByUsername(username);
				if(userDetails !=null) {
					UsernamePasswordAuthenticationToken authenication = 
							new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					authenication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenication);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("fail on set user authenication",e);
		}
		filterChain.doFilter(request, response);
	}
}


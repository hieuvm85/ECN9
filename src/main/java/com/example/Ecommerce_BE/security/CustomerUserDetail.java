package com.example.Ecommerce_BE.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.Ecommerce_BE.model.entity.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class CustomerUserDetail implements UserDetails{

	private int id;
	private String username;
	@JsonIgnore
	private String password;
	private String email;
	private String phone;
	private boolean status;
	
	private Collection<? extends GrantedAuthority> authorities;
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return this.authorities;
	}
	// map thong tin users sang customerUserDetail
	public static CustomerUserDetail mapUserToUserDetail(Users user) {
//		List<GrantedAuthority> listAuthorities= new ArrayList<>();
//		for(Roles roles : user.getListRoles())
//		{
//			SimpleGrantedAuthority sga= new SimpleGrantedAuthority(roles.getRoleName().name());
//			listAuthorities.add(sga);
//		}
		// lay cac quyen tu doi tuong user
		List<GrantedAuthority> listAuthorities= user.getListRoles().stream()
												.map(roles-> new SimpleGrantedAuthority(roles.getRoleName().name()))
												.collect(Collectors.toList());
		//tra ve doi tuong CustomerUserDetail
		return new CustomerUserDetail(
				user.getId(),
				user.getUsername(),
				user.getPassword(),
				user.getEmail(),
				user.isStatus(),
				listAuthorities
				);
	}
	
	public CustomerUserDetail(int id, String username, String password, String email, boolean status,
			Collection<? extends GrantedAuthority> authorities) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.status = status;
		this.authorities = authorities;
	}
	
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public String getPassword() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public String getUsername() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean isAccountNonExpired() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean isAccountNonLocked() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean isCredentialsNonExpired() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean isEnabled() {
//		// TODO Auto-generated method stub
//		return false;
//	}

}

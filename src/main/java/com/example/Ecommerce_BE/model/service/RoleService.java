package com.example.Ecommerce_BE.model.service;

import java.util.Optional;

import com.example.Ecommerce_BE.model.entity.ERole;
import com.example.Ecommerce_BE.model.entity.Roles;

public interface RoleService {
	Optional<Roles> findByRoleName(ERole roleName);
}

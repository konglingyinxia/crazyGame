package com.crazy.web.service.repository.jpa;

import com.crazy.web.model.Role;
import com.crazy.web.model.User;
import com.crazy.web.model.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract interface UserRoleRepository  extends JpaRepository<UserRole, String>
{
	
	public abstract Page<UserRole> findByOrgiAndRole(String orgi, Role role, Pageable paramPageable);

	public abstract List<UserRole> findByOrgiAndRole(String orgi, Role role);

	public abstract List<UserRole> findByOrgiAndUser(String orgi, User user);
}


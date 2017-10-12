package com.crazy.web.service.repository.jpa;

import com.crazy.web.model.Organ;
import com.crazy.web.model.OrganRole;
import com.crazy.web.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract interface OrganRoleRepository  extends JpaRepository<OrganRole, String>
{
	
	public abstract Page<OrganRole> findByOrgiAndRole(String orgi, Role role, Pageable paramPageable);

	public abstract List<OrganRole> findByOrgiAndRole(String orgi, Role role);

	public abstract List<OrganRole> findByOrgiAndOrgan(String orgi, Organ organ);
}


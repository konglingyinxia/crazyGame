package com.crazy.web.service.repository.jpa;

import com.crazy.web.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract interface RoleRepository
  extends JpaRepository<Role, String>
{
  public abstract Role findByIdAndOrgi(String paramString, String orgi);
  
  public abstract List<Role> findByOrgi(String orgi);
  
  public abstract Role findByNameAndOrgi(String paramString, String orgi);
}


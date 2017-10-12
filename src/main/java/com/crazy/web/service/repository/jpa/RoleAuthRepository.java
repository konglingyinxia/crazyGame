package com.crazy.web.service.repository.jpa;

import com.crazy.web.model.RoleAuth;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract interface RoleAuthRepository
  extends JpaRepository<RoleAuth, String>
{
  public abstract RoleAuth findByIdAndOrgi(String paramString, String orgi);
  
  public List<RoleAuth> findByRoleidAndOrgi(String roleid, String orgi) ;
  
  public List<RoleAuth> findAll(Specification<RoleAuth> spec) ;
  
  public abstract RoleAuth findByNameAndOrgi(String paramString, String orgi);
}


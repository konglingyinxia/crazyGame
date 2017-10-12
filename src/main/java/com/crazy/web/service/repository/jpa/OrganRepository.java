package com.crazy.web.service.repository.jpa;

import com.crazy.web.model.Organ;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract interface OrganRepository
  extends JpaRepository<Organ, String>
{
  public abstract Organ findByIdAndOrgi(String paramString, String orgi);
  
  public abstract Page<Organ> findByOrgi(String orgi, Pageable paramPageable);

  public abstract Organ findByNameAndOrgi(String paramString, String orgi);

  public abstract List<Organ> findByOrgi(String orgi);

  public abstract List<Organ> findByOrgiAndSkill(String orgi, boolean skill);
}

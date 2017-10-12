package com.crazy.web.service.repository.jpa;

import com.crazy.web.model.AiConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract interface AiConfigRepository extends JpaRepository<AiConfig, String>
{
  public abstract List<AiConfig> findByOrgi(String orgi);
}

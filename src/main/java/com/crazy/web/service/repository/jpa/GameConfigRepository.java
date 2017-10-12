package com.crazy.web.service.repository.jpa;

import com.crazy.web.model.GameConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract interface GameConfigRepository extends JpaRepository<GameConfig, String>
{
  public abstract List<GameConfig> findByOrgi(String orgi);
}

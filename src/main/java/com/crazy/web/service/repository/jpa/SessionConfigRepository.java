package com.crazy.web.service.repository.jpa;

import com.crazy.web.model.GameConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract interface SessionConfigRepository  extends JpaRepository<GameConfig, String>
{
	public abstract GameConfig findByOrgi(String orgi);
}


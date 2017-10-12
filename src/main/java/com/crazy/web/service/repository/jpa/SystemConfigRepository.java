package com.crazy.web.service.repository.jpa;

import com.crazy.web.model.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract interface SystemConfigRepository  extends JpaRepository<SystemConfig, String>
{
	public abstract SystemConfig findByOrgi(String orgi);
}


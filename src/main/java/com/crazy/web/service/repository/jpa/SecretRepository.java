package com.crazy.web.service.repository.jpa;

import com.crazy.web.model.Secret;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract interface SecretRepository  extends JpaRepository<Secret, String>{
	public abstract List<Secret> findByOrgi(String orgi);
}


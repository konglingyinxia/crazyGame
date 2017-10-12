package com.crazy.web.service.repository.jpa;

import com.crazy.web.model.Generation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract interface GenerationRepository  extends JpaRepository<Generation, String>{
	public abstract Generation findByOrgiAndModel(String orgi, String model);
	public abstract List<Generation> findByOrgi(String orgi);
}


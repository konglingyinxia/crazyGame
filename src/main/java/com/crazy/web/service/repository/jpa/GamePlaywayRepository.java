package com.crazy.web.service.repository.jpa;

import com.crazy.web.model.GamePlayway;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract interface GamePlaywayRepository  extends JpaRepository<GamePlayway, String>{
	
  public abstract GamePlayway findByIdAndOrgi(String id, String orgi);
  
  public abstract List<GamePlayway> findByOrgi(String orgi, Sort sort);
  
  public abstract List<GamePlayway> findByOrgiAndTypeid(String orgi, String typeid, Sort sort);
}

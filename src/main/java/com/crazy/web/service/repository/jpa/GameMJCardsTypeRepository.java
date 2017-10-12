package com.crazy.web.service.repository.jpa;

import com.crazy.web.model.MJCardsType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract interface GameMJCardsTypeRepository  extends JpaRepository<MJCardsType, String>{
	
  public abstract MJCardsType findByIdAndOrgi(String id, String orgi);
  
  public abstract List<MJCardsType> findByOrgiAndGame(String orgi, String game);
}

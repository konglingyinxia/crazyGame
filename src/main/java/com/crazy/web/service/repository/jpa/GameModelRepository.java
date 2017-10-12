package com.crazy.web.service.repository.jpa;

import com.crazy.web.model.GameModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract interface GameModelRepository  extends JpaRepository<GameModel, String>{
	
  public abstract GameModel findByIdAndOrgi(String id, String orgi);
  
  public abstract List<GameModel> findByOrgiAndGame(String orgi, String game);
}

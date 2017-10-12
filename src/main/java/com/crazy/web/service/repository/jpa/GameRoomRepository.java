package com.crazy.web.service.repository.jpa;

import com.crazy.web.model.GameRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract interface GameRoomRepository  extends JpaRepository<GameRoom, String>{
	
  public abstract GameRoom findByIdAndOrgi(String id, String orgi);
  
  public abstract Page<GameRoom> findByOrgi(String orgi, Pageable page);
  
  public abstract List<GameRoom> findByRoomidAndOrgi(String roomid, String orgi);
}

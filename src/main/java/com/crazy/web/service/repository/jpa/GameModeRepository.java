package com.crazy.web.service.repository.jpa;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.crazy.web.model.GameMode;

public abstract interface GameModeRepository extends JpaRepository<GameMode, String>, JpaSpecificationExecutor<GameMode> {

	public abstract GameMode findByplayUserId(String playUserId);

	@Query(value = "UPDATE bm_game_mode SET online = 0 WHERE playuser_id = :playUserId", nativeQuery = true)
	@Modifying
	@Transactional
	public abstract void setOnLineByPlayUserId(@Param("playUserId") String playUserId);

	public abstract List<GameMode> findByOnline(int online);

	public abstract List<GameMode> findByOnlineAndIngame(int online, int ingame);

}

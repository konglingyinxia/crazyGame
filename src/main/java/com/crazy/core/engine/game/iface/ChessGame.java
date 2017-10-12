package com.crazy.core.engine.game.iface;

import com.crazy.util.rules.model.Board;
import com.crazy.web.model.GamePlayway;
import com.crazy.web.model.GameRoom;
import com.crazy.web.model.PlayUserClient;

import java.util.List;

/**
 * 棋牌游戏接口API
 * @author iceworld
 *
 */
public interface ChessGame {
	/**
	 * 创建一局新游戏
	 * @return
	 */
	public Board process(List<PlayUserClient> playUsers, GameRoom gameRoom, GamePlayway playway, String banker, int cardsnum) ;
}

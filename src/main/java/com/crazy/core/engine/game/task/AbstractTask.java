package com.crazy.core.engine.game.task;

import com.crazy.config.web.model.Game;
import com.crazy.util.GameUtils;
import com.crazy.util.UKTools;
import com.crazy.web.model.GamePlayway;
import com.crazy.web.model.GameRoom;
import com.crazy.core.engine.game.ActionTaskUtils;
import com.crazy.core.engine.game.Message;
import org.cache2k.expiry.ValueWithExpiryTime;

public abstract class AbstractTask implements ValueWithExpiryTime {

	/**
	 * 根据玩法，找到对应的状态机
	 * @param playway
	 * @param orgi
	 * @return
	 */
	public Game getGame(String playway , String orgi){
		return GameUtils.getGame(playway , orgi) ;
	}
	
	public void sendEvent(String event , Message message , GameRoom gameRoom){
		ActionTaskUtils.sendEvent(event, message, gameRoom);
	}
	
	public Object json(Object data){
		return UKTools.json(data) ;
	}
	/**
	 * 根据当前 ROOM的 玩法， 确定下一步的流程
	 * @param playway
	 * @param currentStatus
	 * @return
	 */
	public String getNextEvent(GamePlayway playway,String currentStatus){
		return "" ;
	}
}

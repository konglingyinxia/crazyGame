package com.crazy.core.engine.game.task.dizhu;

import com.crazy.core.BMDataContext;
import com.crazy.web.model.GameRoom;
import com.crazy.core.engine.game.BeiMiGameTask;
import com.crazy.core.engine.game.task.AbstractTask;
import org.cache2k.expiry.ValueWithExpiryTime;

/**
 * 出牌计时器，默认25秒，超时执行
 * @author zhangtianyi
 *
 */
public class CreatePlayCardsTask extends AbstractTask implements ValueWithExpiryTime  , BeiMiGameTask {

	private long timer  ;
	private GameRoom gameRoom = null ;
	private String orgi ;
	private String player ;
	
	public CreatePlayCardsTask(long timer ,String userid, GameRoom gameRoom, String orgi){
		super();
		this.timer = timer ;
		this.gameRoom = gameRoom ;
		this.orgi = orgi ;
		this.player = userid ;
	}
	@Override
	public long getCacheExpiryTime() {
		return System.currentTimeMillis()+timer*1000;	//5秒后执行
	}
	
	public void execute(){
		/**
		 * 合并代码，玩家 出牌超时处理和 玩家出牌统一使用一处代码
		 */
		BMDataContext.getGameEngine().takeCardsRequest(this.gameRoom.getId(), this.player, orgi,true,  null);
	}
}

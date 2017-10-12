package com.crazy.core.engine.game.task;

import com.crazy.core.BMDataContext;
import com.crazy.util.cache.CacheHelper;
import com.crazy.util.rules.model.Board;
import com.crazy.web.model.GameRoom;
import com.crazy.core.engine.game.BeiMiGameTask;
import org.cache2k.expiry.ValueWithExpiryTime;

public class CreateAllCardsTask extends AbstractTask implements ValueWithExpiryTime  , BeiMiGameTask {

	private long timer  ;
	private GameRoom gameRoom = null ;
	private String orgi ;
	
	public CreateAllCardsTask(long timer , GameRoom gameRoom, String orgi){
		super();
		this.timer = timer ;
		this.gameRoom = gameRoom ;
		this.orgi = orgi ;
	}
	@Override
	public long getCacheExpiryTime() {
		return System.currentTimeMillis()+timer*1000;	//5秒后执行
	}
	
	public void execute(){
		Board board = (Board) CacheHelper.getBoardCacheBean().getCacheObject(gameRoom.getId(), gameRoom.getOrgi());
		board.setFinished(true);
		/**
		 * 结算信息 ， 更新 玩家信息
		 */
		sendEvent("allcards", board , gameRoom) ;	//通知所有客户端结束牌局，进入结算
		
		if(board.isFinished()){
			BMDataContext.getGameEngine().finished(gameRoom.getId(), orgi);
		}
	}
}

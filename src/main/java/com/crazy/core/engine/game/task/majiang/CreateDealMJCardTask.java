package com.crazy.core.engine.game.task.majiang;

import com.crazy.util.cache.CacheHelper;
import com.crazy.util.rules.model.Board;
import com.crazy.web.model.GameRoom;
import com.crazy.core.engine.game.BeiMiGameTask;
import com.crazy.core.engine.game.task.AbstractTask;
import org.cache2k.expiry.ValueWithExpiryTime;

/**
 * 出牌计时器，默认25秒，超时执行
 * @author zhangtianyi
 *
 */
public class CreateDealMJCardTask extends AbstractTask implements ValueWithExpiryTime  , BeiMiGameTask {

	private long timer  ;
	private GameRoom gameRoom = null ;
	private String orgi ;
	
	public CreateDealMJCardTask(long timer , GameRoom gameRoom, String orgi){
		super();
		this.timer = timer ;
		this.gameRoom = gameRoom ;
		this.orgi = orgi ;
	}
	@Override
	public long getCacheExpiryTime() {
		return System.currentTimeMillis()+timer*1000;	//5秒后执行
	}
	/**
	 * 杠碰吃胡 时间到 了，执行发牌动作
	 */
	public void execute(){
		Board board = (Board) CacheHelper.getBoardCacheBean().getCacheObject(gameRoom.getId(), gameRoom.getOrgi());
		board.dealRequest(gameRoom, board, orgi , false,  null);
	}
}

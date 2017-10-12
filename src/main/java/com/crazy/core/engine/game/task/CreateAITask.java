package com.crazy.core.engine.game.task;

import com.crazy.core.BMDataContext;
import com.crazy.util.GameUtils;
import com.crazy.util.cache.CacheHelper;
import com.crazy.web.model.GameRoom;
import com.crazy.web.model.PlayUser;
import com.crazy.web.model.PlayUserClient;
import com.crazy.core.engine.game.ActionTaskUtils;
import com.crazy.core.engine.game.BeiMiGameEvent;
import com.crazy.core.engine.game.BeiMiGameTask;
import org.cache2k.expiry.ValueWithExpiryTime;

import java.util.List;

public class CreateAITask extends AbstractTask implements ValueWithExpiryTime  , BeiMiGameTask {

	private long timer  ;
	private GameRoom gameRoom = null ;
	private String orgi ;
	
	public CreateAITask(long timer , GameRoom gameRoom, String orgi){
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
		//执行生成AI
		GameUtils.removeGameRoom(gameRoom.getPlayway(), gameRoom.getId(), orgi);
		List<PlayUserClient> playerList = CacheHelper.getGamePlayerCacheBean().getCacheObject(gameRoom.getId(), gameRoom.getOrgi()) ;
		int aicount = gameRoom.getPlayers() - playerList.size() ;
		if(aicount>0){
			for(int i=0 ; i<aicount ; i++){
				PlayUserClient playerUser = GameUtils.create(new PlayUser() , BMDataContext.PlayerTypeEnum.AI.toString()) ;
				playerUser.setPlayerindex(System.currentTimeMillis());	//按照加入房间的时间排序，有玩家离开后，重新发送玩家信息列表，重新座位
				
				CacheHelper.getGamePlayerCacheBean().put(gameRoom.getId(), playerUser, orgi); //将用户加入到 room ， MultiCache
				playerList.add(playerUser) ;
			}
			/**
			 * 发送一个 Enough 事件
			 */
			ActionTaskUtils.sendPlayers(gameRoom, playerList);
			
			super.getGame(gameRoom.getPlayway(), orgi).change(gameRoom , BeiMiGameEvent.ENOUGH.toString());	//通知状态机 , 此处应由状态机处理异步执行
		}
	}
}

package com.crazy.core.engine.game.task.dizhu;

import com.crazy.core.BMDataContext;
import com.crazy.util.GameUtils;
import com.crazy.util.cache.CacheHelper;
import com.crazy.util.rules.model.DuZhuBoard;
import com.crazy.util.rules.model.Player;
import com.crazy.web.model.GameRoom;
import com.crazy.web.model.PlayUserClient;
import com.crazy.core.engine.game.ActionTaskUtils;
import com.crazy.core.engine.game.BeiMiGameEvent;
import com.crazy.core.engine.game.BeiMiGameTask;
import com.crazy.core.engine.game.GameBoard;
import com.crazy.core.engine.game.task.AbstractTask;
import org.apache.commons.lang.ArrayUtils;
import org.cache2k.expiry.ValueWithExpiryTime;

import java.util.Arrays;

public class CreateRaiseHandsTask extends AbstractTask implements ValueWithExpiryTime  , BeiMiGameTask {

	private long timer  ;
	private GameRoom gameRoom = null ;
	private String orgi ;
	
	public CreateRaiseHandsTask(long timer , GameRoom gameRoom, String orgi){
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
		/**
		 * 
		 * 顺手 把牌发了，注：此处应根据 GameRoom的类型获取 发牌方式
		 */
		DuZhuBoard board = (DuZhuBoard) CacheHelper.getBoardCacheBean().getCacheObject(gameRoom.getId(), gameRoom.getOrgi());
		Player lastHandsPlayer = null ;
		for(Player player : board.getPlayers()){
			if(player.getPlayuser().equals(board.getBanker())){//抢到地主的人
				byte[] lastHands = board.pollLastHands() ;
				board.setLasthands(lastHands);
				player.setCards(ArrayUtils.addAll(player.getCards(), lastHands)) ;//翻底牌 
				Arrays.sort(player.getCards());									  //重新排序
				player.setCards(GameUtils.reverseCards(player.getCards()));		  //从大到小 倒序
				lastHandsPlayer = player ;
				break ;
			}
		}
		/**
		 * 计算底牌倍率
		 */
		board.setRatio(board.getRatio() * board.calcRatio());
		
		/**
		 * 发送一个通知，翻底牌消息
		 */
		sendEvent("lasthands", new GameBoard(lastHandsPlayer.getPlayuser() , board.getLasthands(), board.getRatio()) , gameRoom) ;
		
		/**
		 * 更新牌局状态
		 */
		CacheHelper.getBoardCacheBean().put(gameRoom.getId(), board, orgi);
		/**
		 * 发送一个 开始打牌的事件 ， 判断当前出牌人是 玩家还是 AI，如果是 AI，则默认 1秒时间，如果是玩家，则超时时间是25秒
		 */
		PlayUserClient playUserClient = ActionTaskUtils.getPlayUserClient(gameRoom.getId(), lastHandsPlayer.getPlayuser(), orgi) ;
		
		if(BMDataContext.PlayerTypeEnum.NORMAL.toString().equals(playUserClient.getPlayertype())){
			super.getGame(gameRoom.getPlayway(), orgi).change(gameRoom , BeiMiGameEvent.PLAYCARDS.toString() , 25);	//应该从 游戏后台配置参数中获取
		}else{
			super.getGame(gameRoom.getPlayway(), orgi).change(gameRoom , BeiMiGameEvent.PLAYCARDS.toString() ,3);	//应该从游戏后台配置参数中获取
		}
	}
}

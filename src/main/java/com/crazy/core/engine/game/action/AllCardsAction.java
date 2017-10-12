package com.crazy.core.engine.game.action;

import com.crazy.core.BMDataContext;
import com.crazy.core.statemachine.action.Action;
import com.crazy.core.statemachine.impl.BeiMiExtentionTransitionConfigurer;
import com.crazy.core.statemachine.message.Message;
import com.crazy.util.cache.CacheHelper;
import com.crazy.web.model.GameRoom;
import com.crazy.core.engine.game.task.CreateAllCardsTask;
import org.apache.commons.lang3.StringUtils;

/**
 * 反底牌发给地主
 * @author iceworld
 *
 * @param <T>
 * @param <S>
 */
public class AllCardsAction<T,S> implements Action<T, S>{

	@Override
	public void execute(Message<T> message, BeiMiExtentionTransitionConfigurer<T,S> configurer) {
		String room = (String)message.getMessageHeaders().getHeaders().get("room") ;
		if(!StringUtils.isBlank(room)){
			GameRoom gameRoom = (GameRoom) CacheHelper.getGameRoomCacheBean().getCacheObject(room, BMDataContext.SYSTEM_ORGI) ; 
			if(gameRoom!=null){
				CacheHelper.getExpireCache().put(gameRoom.getRoomid(), new CreateAllCardsTask(0 , gameRoom , gameRoom.getOrgi()));
			}
		}
	}
}

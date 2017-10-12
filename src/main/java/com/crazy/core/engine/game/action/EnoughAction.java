package com.crazy.core.engine.game.action;

import com.crazy.core.BMDataContext;
import com.crazy.core.statemachine.action.Action;
import com.crazy.core.statemachine.impl.BeiMiExtentionTransitionConfigurer;
import com.crazy.core.statemachine.message.Message;
import com.crazy.util.cache.CacheHelper;
import com.crazy.web.model.GameRoom;
import com.crazy.core.engine.game.task.CreateBeginTask;
import org.apache.commons.lang3.StringUtils;

/**
 * 凑够了，开牌
 * @author iceworld
 *
 */
public class EnoughAction<T,S> implements Action<T, S>{
	@Override
	public void execute(Message<T> message , BeiMiExtentionTransitionConfigurer<T,S> configurer) {
		String room = (String)message.getMessageHeaders().getHeaders().get("room") ;
		if(!StringUtils.isBlank(room)){
			GameRoom gameRoom = (GameRoom) CacheHelper.getGameRoomCacheBean().getCacheObject(room, BMDataContext.SYSTEM_ORGI) ; 
			if(gameRoom!=null){
				CacheHelper.getExpireCache().put(gameRoom.getRoomid(), new CreateBeginTask(1 , gameRoom , gameRoom.getOrgi()));
			}
		}
	}
}

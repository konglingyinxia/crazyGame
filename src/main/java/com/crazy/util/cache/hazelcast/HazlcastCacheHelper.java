package com.crazy.util.cache.hazelcast;

import com.crazy.core.BMDataContext;
import com.crazy.util.cache.CacheBean;
import com.crazy.util.cache.CacheInstance;
import com.crazy.util.cache.hazelcast.impl.*;

/**
 * Hazlcast缓存处理实例类
 * @author admin
 *
 */
public class HazlcastCacheHelper implements CacheInstance {
	/**
	 * 服务类型枚举
	 * @author admin
	 *
	 */
	public enum CacheServiceEnum{
		HAZLCAST_CLUSTER_AGENT_USER_CACHE, HAZLCAST_CLUSTER_AGENT_STATUS_CACHE, HAZLCAST_CLUSTER_QUENE_USER_CACHE,HAZLCAST_ONLINE_CACHE , GAME_PLAYERS_CACHE , HAZLCAST_CULUSTER_SYSTEM , HAZLCAST_GAMEROOM_CACHE , API_USER_CACHE , QUENE_CACHE, HAZLCAST_TASK_CACHE, HAZLCAST_GAME_CACHE;
		public String toString(){
			return super.toString().toLowerCase();
		}
	}
	
	@Override
	public CacheBean getOnlineCacheBean() {
		return BMDataContext.getContext().getBean(OnlineCache.class).getCacheInstance(CacheServiceEnum.HAZLCAST_ONLINE_CACHE.toString()) ;
	}
	@Override
	public CacheBean getSystemCacheBean() {
		return BMDataContext.getContext().getBean(SystemCache.class).getCacheInstance(CacheServiceEnum.HAZLCAST_CULUSTER_SYSTEM.toString()) ;
	}
	@Override
	public CacheBean getGameRoomCacheBean() {
		return BMDataContext.getContext().getBean(GameRoomCache.class).getCacheInstance(CacheServiceEnum.HAZLCAST_GAMEROOM_CACHE.toString()) ;
	}
	@Override
	public CacheBean getGameCacheBean() {
		return BMDataContext.getContext().getBean(GameCache.class).getCacheInstance(CacheServiceEnum.HAZLCAST_GAME_CACHE.toString()) ;
	}
	@Override
	public CacheBean getApiUserCacheBean() {
		return BMDataContext.getContext().getBean(ApiUserCache.class).getCacheInstance(CacheServiceEnum.API_USER_CACHE.toString()) ;
	}
	@Override
	public QueneCache getQueneCache() {
		// TODO Auto-generated method stub
		return BMDataContext.getContext().getBean(QueneCache.class).getCacheInstance(CacheServiceEnum.QUENE_CACHE.toString()) ;
	}
	@Override
	public MultiCache getGamePlayerCacheBean() {
		// TODO Auto-generated method stub
		return BMDataContext.getContext().getBean(MultiCache.class).getCacheInstance(CacheServiceEnum.GAME_PLAYERS_CACHE.toString()) ;
	}
}

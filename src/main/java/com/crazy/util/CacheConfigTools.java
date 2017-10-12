package com.crazy.util;

import com.crazy.core.BMDataContext;
import com.crazy.web.model.AccountConfig;
import com.crazy.web.service.repository.jpa.AccountConfigRepository;
import com.crazy.util.cache.CacheHelper;

import java.util.List;

/**
 * 用于获取缓存配置
 * @author iceworld
 *
 */
public class CacheConfigTools {
	public static AccountConfig getGameAccountConfig(String orgi){
		AccountConfig config = (AccountConfig) CacheHelper.getSystemCacheBean().getCacheObject(BMDataContext.getGameAccountConfig(orgi), orgi) ;
		if(config == null){
			AccountConfigRepository accountRes = BMDataContext.getContext().getBean(AccountConfigRepository.class) ;
			List<AccountConfig> gameAccountList = accountRes.findByOrgi(orgi) ;
			if(gameAccountList!=null && gameAccountList.size() >0){
				config = gameAccountList.get(0) ;
			}else{
				config = new AccountConfig() ;
			}
			CacheHelper.getSystemCacheBean().put(BMDataContext.getGameAccountConfig(orgi), config, orgi);
		}
		return config;
	}
}

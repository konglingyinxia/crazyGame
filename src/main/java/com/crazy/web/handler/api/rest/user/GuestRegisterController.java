package com.crazy.web.handler.api.rest.user;

import com.crazy.core.BMDataContext;
import com.crazy.util.*;
import com.crazy.util.cache.CacheHelper;
import com.crazy.web.handler.Handler;
import com.crazy.web.model.*;
import com.crazy.web.service.repository.es.PlayUserClientESRepository;
import com.crazy.web.service.repository.es.PlayUserESRepository;
import com.crazy.web.service.repository.es.TokenESRepository;
import com.crazy.web.service.repository.jpa.PlayUserRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

@RestController
@RequestMapping("/api/guest")
public class GuestRegisterController extends Handler {

	@Autowired
	private PlayUserESRepository playUserESRes;
	
	@Autowired
	private PlayUserClientESRepository playUserClientRes ;
	
	@Autowired
	private PlayUserRepository playUserRes ;
	
	@Autowired
	private TokenESRepository tokenESRes ;

	@RequestMapping
    public ResponseEntity<ResultData> guest(HttpServletRequest request , @Valid String token) {
		PlayUserClient playUserClient = null ;
		Token userToken = null ;
		if(!StringUtils.isBlank(token)){
			userToken = tokenESRes.findById(token) ;
			if(userToken != null && !StringUtils.isBlank(userToken.getUserid()) && userToken.getExptime()!=null && userToken.getExptime().after(new Date())){
				//返回token， 并返回游客数据给游客
				playUserClient = playUserClientRes.findById(userToken.getUserid()) ;
				if(playUserClient!=null){
					playUserClient.setToken(userToken.getId());
				}
			}else{
				if(userToken!=null){
					tokenESRes.delete(userToken);
					userToken = null ;
				}
			}
		}
		String ip = UKTools.getIpAddr(request);
		IP ipdata = IPTools.getInstance().findGeography(ip);
		if(playUserClient == null){
			try {
				playUserClient = register(new PlayUser() , ipdata , request) ;
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		if(userToken == null){
			userToken = new Token();
			userToken.setIp(ip);
			userToken.setRegion(ipdata.getProvince()+ipdata.getCity());
			userToken.setId(UKTools.getUUID());
			userToken.setUserid(playUserClient.getId());
			userToken.setCreatetime(new Date());
			userToken.setOrgi(playUserClient.getOrgi());
			AccountConfig config = CacheConfigTools.getGameAccountConfig(BMDataContext.SYSTEM_ORGI) ;
    		if(config!=null && config.getExpdays() > 0){
    			userToken.setExptime(new Date(System.currentTimeMillis()+60*60*24*config.getExpdays()*1000));//默认有效期 ， 7天
    		}else{
    			userToken.setExptime(new Date(System.currentTimeMillis()+60*60*24*7*1000));//默认有效期 ， 7天
    		}
			userToken.setLastlogintime(new Date());
			userToken.setUpdatetime(new Date(0));
			
			tokenESRes.save(userToken) ;
		}
		playUserClient.setToken(userToken.getId());
		CacheHelper.getApiUserCacheBean().put(userToken.getId(),userToken, userToken.getOrgi());
		CacheHelper.getApiUserCacheBean().put(playUserClient.getId(),playUserClient, userToken.getOrgi());
		ResultData playerResultData = new ResultData( playUserClient!=null , playUserClient != null ? MessageEnum.USER_REGISTER_SUCCESS: MessageEnum.USER_REGISTER_FAILD_USERNAME , playUserClient , userToken) ;
		playerResultData.setGametype(GameUtils.gameConfig(userToken.getOrgi()).getGametype());
		/**
		 * 封装 游戏对象，发送到客户端
		 */
		if(!StringUtils.isBlank(playerResultData.getGametype())){
			/**
			 * 找到游戏配置的 模式 和玩法，如果多选，则默认进入的是 大厅模式，如果是单选，则进入的是选场模式
			 */
			playerResultData.setGames(GameUtils.games(playerResultData.getGametype()));
		}
		/**
		 * 根据游戏配置 ， 选择 返回的 玩法列表
		 */
        return new ResponseEntity<>(playerResultData, HttpStatus.OK);
    }
	/**
	 * 注册用户
	 * @param player
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public PlayUserClient register(PlayUser player , IP ipdata , HttpServletRequest request ) throws IllegalAccessException, InvocationTargetException{
		PlayUserClient playUserClient = GameUtils.create(player, ipdata, request) ;
		int users = playUserESRes.countByUsername(player.getUsername()) ;
		if(users == 0){
			UKTools.published(player , playUserESRes , playUserRes);
		}
		return playUserClient ;
	}
	
}
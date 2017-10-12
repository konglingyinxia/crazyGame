package com.crazy.web.handler.apps.business.platform;

import com.crazy.core.BMDataContext;
import com.crazy.util.GameUtils;
import com.crazy.util.Menu;
import com.crazy.util.cache.CacheHelper;
import com.crazy.web.handler.Handler;
import com.crazy.web.model.*;
import com.crazy.web.service.repository.es.PlayUserESRepository;
import com.crazy.web.service.repository.jpa.GamePlaywayRepository;
import com.crazy.web.service.repository.jpa.GameRoomRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/apps/platform")
public class GameRoomController extends Handler {
	
	@Autowired
	private GameRoomRepository gameRoomRes ;
	
	@Autowired
	private PlayUserESRepository playUserRes;
	
	@Autowired
	private GamePlaywayRepository playwayRes;
	
	@RequestMapping({"/gameroom"})
	@Menu(type="platform", subtype="gameroom")
	public ModelAndView gameusers(ModelMap map , HttpServletRequest request , @Valid String id){
		Page<GameRoom> gameRoomList = gameRoomRes.findByOrgi(super.getOrgi(request), new PageRequest(super.getP(request), super.getPs(request))) ;
		List<String> playUsersList = new ArrayList<String>() ;
		for(GameRoom gameRoom : gameRoomList.getContent()){
			List<PlayUserClient> players = CacheHelper.getGamePlayerCacheBean().getCacheObject(gameRoom.getId(),gameRoom.getOrgi()) ;
			gameRoom.setPlayers(players.size());
			if(!StringUtils.isBlank(gameRoom.getMaster())){
				playUsersList.add(gameRoom.getMaster()) ;
			}
			if(!StringUtils.isBlank(gameRoom.getPlayway())){
				gameRoom.setGamePlayway((GamePlayway) CacheHelper.getSystemCacheBean().getCacheObject(gameRoom.getPlayway(), super.getOrgi(request)));
			}
		}
		if(playUsersList.size() > 0){
			for(PlayUser playUser : playUserRes.findAll(playUsersList) ){
				for(GameRoom gameRoom : gameRoomList.getContent()){
					if(playUser.getId().equals(gameRoom.getMaster())){
						gameRoom.setMasterUser(playUser); break ;
					}
				}
			}
		}
		map.addAttribute("gameRoomList", gameRoomList) ;
		
		map.addAttribute("gameModelList", BeiMiDic.getInstance().getDic(BMDataContext.BEIMI_SYSTEM_GAME_TYPE_DIC)) ;
		
		return request(super.createAppsTempletResponse("/apps/business/platform/game/room/index"));
	}
	

	@RequestMapping({"/gameroom/delete"})
	@Menu(type="platform", subtype="gameroom")
	public ModelAndView delete(ModelMap map , HttpServletRequest request , @Valid String id , @Valid String game){
		if(!StringUtils.isBlank(id)){
			GameRoom gameRoom = gameRoomRes.findByIdAndOrgi(id, super.getOrgi(request)) ;
			if(gameRoom!=null){
				gameRoomRes.delete(gameRoom);
			}
			GameUtils.removeGameRoom(gameRoom.getPlayway(),gameRoom.getId(), super.getOrgi(request));
			CacheHelper.getGameRoomCacheBean().delete(gameRoom.getId(), super.getOrgi(request)) ;
			CacheHelper.getExpireCache().remove(gameRoom.getId());
			List<PlayUserClient> playerUsers = CacheHelper.getGamePlayerCacheBean().getCacheObject(id, super.getOrgi(request)) ;
			for(PlayUserClient tempPlayUser : playerUsers){
				CacheHelper.getRoomMappingCacheBean().delete(tempPlayUser.getId(), super.getOrgi(request)) ;
			}
			CacheHelper.getGamePlayerCacheBean().delete(gameRoom.getId()) ;
		}
		return request(super.createRequestPageTempletResponse("redirect:/apps/platform/gameroom.html"));
	}
	
}

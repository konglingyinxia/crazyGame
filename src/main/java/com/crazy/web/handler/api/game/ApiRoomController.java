package com.crazy.web.handler.api.game;

import com.crazy.core.BMDataContext;
import com.crazy.core.engine.game.BeiMiGame;
import com.crazy.util.GameUtils;
import com.crazy.util.cache.CacheHelper;
import com.crazy.web.model.GameRoom;
import com.crazy.web.model.PlayUserClient;
import com.crazy.web.model.Token;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ApiRoomController {

    @RequestMapping({"/api/room/create"})
    @ResponseBody
    public String createRoom(ModelMap map , @RequestParam("token") String token) {
        Token userToken = (Token) CacheHelper.getApiUserCacheBean().getCacheObject(token, BMDataContext.SYSTEM_ORGI) ;

        //创建房间号，并返回客户端
        String room = (int)((Math.random()*9+1)*100000)+"" ;

        String gameType = GameUtils.gameConfig(userToken.getOrgi()).getGametype() ;
        List<BeiMiGame> beiMiGames = GameUtils.games(gameType) ;

        return "{\"room\":\""+room+"\",\"code\":\""+beiMiGames.get(0).getCode()+"\",\"playway\":\""+beiMiGames.get(0).getTypes().get(0).getPlayways().get(0).getId()+"\"}" ;
    }

    @RequestMapping({"/api/room/query"})
    @ResponseBody
    public String queryRoom(ModelMap map , @RequestParam("token") String token , @RequestParam("room") String room) {
        Token userToken = (Token) CacheHelper.getApiUserCacheBean().getCacheObject(token, BMDataContext.SYSTEM_ORGI) ;
        PlayUserClient userClient = (PlayUserClient) CacheHelper.getApiUserCacheBean().getCacheObject(userToken.getUserid(), userToken.getOrgi()) ;

        //创建房间号，并返回客户端
        String roomId = (String) CacheHelper.getBoardCacheBean().getCacheObject(room+"RoomCard", userToken.getOrgi()) ;
        GameRoom gameRoom = (GameRoom) CacheHelper.getGameRoomCacheBean().getCacheObject(roomId, userToken.getOrgi()) ;
        if ( null == gameRoom ) {
            return "{}";
        } else {
            return "{\"room\":\"" + room + "\",\"code\":\"" + gameRoom.getCode() + "\",\"playway\":\"" + gameRoom.getPlayway() + "\"}";
        }
    }

    @RequestMapping({"/api/room/match"})
    @ResponseBody
    public String matchRoom(ModelMap map , @RequestParam("token") String token) {
        Token userToken = (Token) CacheHelper.getApiUserCacheBean().getCacheObject(token, BMDataContext.SYSTEM_ORGI) ;

        String gameType = GameUtils.gameConfig(userToken.getOrgi()).getGametype() ;
        List<BeiMiGame> beiMiGames = GameUtils.games(gameType) ;

        return "{\"code\":\""+beiMiGames.get(0).getCode()+"\",\"playway\":\""+beiMiGames.get(0).getTypes().get(0).getPlayways().get(0).getId()+"\"}" ;
    }
}

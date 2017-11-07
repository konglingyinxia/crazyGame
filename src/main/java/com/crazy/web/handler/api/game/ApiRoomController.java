package com.crazy.web.handler.api.game;

import com.crazy.core.BMDataContext;
import com.crazy.core.engine.game.BeiMiGame;
import com.crazy.util.GameUtils;
import com.crazy.util.cache.CacheHelper;
import com.crazy.util.rules.model.Board;
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
        GameRoom gameRoom = BMDataContext.getGameEngine().whichRoom(userToken.getUserid(), userToken.getOrgi()) ;
        //Board board = (Board) CacheHelper.getBoardCacheBean().getCacheObject(gameRoom.getRoomid(), gameRoom.getOrgi());
        String gameType = GameUtils.gameConfig(userToken.getOrgi()).getGametype() ;
        List<BeiMiGame> beiMiGames = GameUtils.games(gameType) ;
        if ( null != gameRoom ) {
            return "{\"error\":\"true\",\"msg\":\"您还有未完成的牌局!\",\"room\":\""+gameRoom.getId()+"\",\"code\":\""+beiMiGames.get(0).getCode()+"\",\"playway\":\""+beiMiGames.get(0).getTypes().get(0).getPlayways().get(0).getId()+"\"}" ;
        }

        //创建房间号，并返回客户端
        String room = (int)((Math.random()*9+1)*100000)+"" ;

        return "{\"room\":\""+room+"\",\"code\":\""+beiMiGames.get(0).getCode()+"\",\"playway\":\""+beiMiGames.get(0).getTypes().get(0).getPlayways().get(0).getId()+"\"}" ;
    }

    @RequestMapping({"/api/room/query"})
    @ResponseBody
    public String queryRoom(ModelMap map , @RequestParam("token") String token , @RequestParam("room") String room) {
        Token userToken = (Token) CacheHelper.getApiUserCacheBean().getCacheObject(token, BMDataContext.SYSTEM_ORGI) ;
        GameRoom gameRoom = BMDataContext.getGameEngine().whichRoom(userToken.getUserid(), userToken.getOrgi()) ;

        //Board board = (Board) CacheHelper.getBoardCacheBean().getCacheObject(gameRoom.getRoomid(), gameRoom.getOrgi());
        String gameType = GameUtils.gameConfig(userToken.getOrgi()).getGametype() ;
        List<BeiMiGame> beiMiGames = GameUtils.games(gameType) ;
        if ( null != gameRoom ) {
            return "{\"error\":\"true\",\"msg\":\"您还有为完成的牌局!\",\"room\":\""+gameRoom.getId()+"\",\"code\":\""+beiMiGames.get(0).getCode()+"\",\"playway\":\""+beiMiGames.get(0).getTypes().get(0).getPlayways().get(0).getId()+"\"}" ;
        }
        PlayUserClient userClient = (PlayUserClient) CacheHelper.getApiUserCacheBean().getCacheObject(userToken.getUserid(), userToken.getOrgi()) ;

        //创建房间号，并返回客户端
        String roomId = (String) CacheHelper.getBoardCacheBean().getCacheObject(room+"RoomCard", userToken.getOrgi()) ;
        if ( null == roomId ) {
            return "{\"error\":\"true\",\"msg\":\"房间还没开启!\"}" ;
        }
        gameRoom = (GameRoom) CacheHelper.getGameRoomCacheBean().getCacheObject(roomId, userToken.getOrgi()) ;
        if ( null == gameRoom ) {
            return "{}";
        } else {
            List<PlayUserClient> userList = CacheHelper.getGamePlayerCacheBean().getMultiList(gameRoom.getId(), userToken.getOrgi(),false) ;
            boolean inroom = false ;
            for(PlayUserClient user : userList){
                if(user.getId().equals(userToken.getUserid())){
                    inroom = true ; break ;
                }
            }
            if ( !inroom && userList.size() >= gameRoom.getPlayers() ) {
                //房间已经达到最大人数，退出
                return "{\"error\":\"true\",\"msg\":\"房间已经满员!\"}" ;
            }
            return "{\"room\":\"" + roomId + "\",\"code\":\"" + gameRoom.getCode() + "\",\"playway\":\"" + gameRoom.getPlayway() + "\"}";
        }
    }

    /**
     * 游戏断开后重连的接口查询方法，此处可以获取到用户是否正在参与某个类型的房间的游戏，如果正在参与则返回参与中的房间信息
     * @param map 参数集合
     * @param token 令牌
     * @return 返回客户端的对象数组
     */
    @RequestMapping({"/api/room/reConnection"})
    @ResponseBody
    public String reConnection(ModelMap map , @RequestParam("token") String token) {
        Token userToken = (Token) CacheHelper.getApiUserCacheBean().getCacheObject(token, BMDataContext.SYSTEM_ORGI) ;
        if ( null == userToken ) {
            return "{}" ;
        }
        PlayUserClient userClient = (PlayUserClient) CacheHelper.getApiUserCacheBean().getCacheObject(userToken.getUserid(), userToken.getOrgi()) ;
        String room = (String) CacheHelper.getRoomMappingCacheBean().getCacheObject(userClient.getId(), userClient.getOrgi()) ;
        if ( null == room ) {
            return "{}" ;
        }

        //创建房间号，并返回客户端
        String roomId = (String) CacheHelper.getBoardCacheBean().getCacheObject(room+"RoomCard", userToken.getOrgi()) ;
        if(roomId != null){
        	GameRoom gameRoom = (GameRoom) CacheHelper.getGameRoomCacheBean().getCacheObject(roomId, userToken.getOrgi()) ;
            if ( null == gameRoom ) {
                return "{}";
            } else {
                return "{\"room\":\"" + roomId + "\",\"code\":\"" + gameRoom.getCode() + "\",\"playway\":\"" + gameRoom.getPlayway() + "\"}";
            }
        }else{
        	 return "{}";
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

package com.crazy.web.handler.mobileter.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.crazy.util.IP;
import com.crazy.util.IPTools;
import com.crazy.util.UKTools;
import com.crazy.web.handler.Handler;
import com.crazy.web.model.GameMode;
import com.crazy.web.model.PlayUser;
import com.crazy.web.service.repository.jpa.GameModeRepository;
import com.crazy.web.service.repository.jpa.PlayUserRepository;
import com.google.gson.Gson;

@Controller
@RequestMapping("/gamemode")
public class GameModeController extends Handler {

	@Autowired
	private GameModeRepository gameModeRepository;

	@Autowired
	private PlayUserRepository playUserRes;

	/**
	 * @Title: intoTheHeats
	 * @Description: TODO(进入预赛大厅)
	 * @param token
	 * @param request
	 * @return 设定文件 String 返回类型
	 */
	@RequestMapping("/intoTheHeats")
	private String intoTheHeats(String token, HttpServletRequest request) {
		Gson gson = new Gson();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			PlayUser playUser = playUserRes.findByToken(token);
			if (null != playUser) {
				GameMode newGameMode = gameModeRepository.findByplayUserId(playUser.getId());
				GameMode gameMode = new GameMode();
				if (null == newGameMode) {
					String ip = UKTools.getIpAddr(request);
					IP ipdata = IPTools.getInstance().findGeography(ip);
					gameMode.setPlayUserId(playUser.getId());
					gameMode.setIp(ip);
					gameMode.setCity(ipdata.getCity());
					gameMode.setProvince(ipdata.getProvince());
				}
				gameMode.setOnline(1);
				gameMode.setIngame(0);
				newGameMode = gameMode;
				gameModeRepository.saveAndFlush(newGameMode);
			} else {
				dataMap.put("msg", "用户信息不存在！");
			}
		} catch (Exception e) {
			dataMap.put("msg", "进入大厅失败！");
			e.printStackTrace();
		}
		return gson.toJson(dataMap);
	}

	/**
	 * @Title: exitHeats
	 * @Description: TODO(退出预赛大厅)
	 * @param token
	 * @return 设定文件 String 返回类型
	 */
	@RequestMapping("/exitHeats")
	private String exitHeats(String token) {
		Gson gson = new Gson();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			PlayUser playUser = playUserRes.findByToken(token);
			if (null != playUser) {
				GameMode newGameMode = gameModeRepository.findByplayUserId(playUser.getId());
				if (null != newGameMode) {
					gameModeRepository.setOnLineByPlayUserId(playUser.getId());
				} else {
					dataMap.put("msg", "查询客户信息失败！");
				}
			} else {
				dataMap.put("msg", "查询客户信息失败！");
			}
		} catch (Exception e) {
			dataMap.put("msg", "退出大厅失败！");
			e.printStackTrace();
		}
		return gson.toJson(dataMap);
	}

	/**
	 * @Title: matchPlayer
	 * @Description: TODO(匹配玩家)
	 * @param token
	 * @return 设定文件 String 返回类型
	 */
	@RequestMapping("/matchPlayer")
	private String matchPlayer(String token) {
		Gson gson = new Gson();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			PlayUser playUser = playUserRes.findByToken(token);
			if (null != playUser) {
				List<GameMode> list = gameModeRepository.findByOnlineAndIngame(1, 0);// 查询出当前在线并且不在游戏中的玩家
				if (list.size() >= 3) {
					List<GameMode> finList = new ArrayList<GameMode>();
					Collections.shuffle(list);// 随机打乱列表顺序
					for (int i = 0; i < 3; i++) {
						finList.add(list.get(i));
					}
					dataMap.put("data", finList);
				} else {
					dataMap.put("info", "当前能匹配玩家数量小于四人！");
				}
			} else {
				dataMap.put("msg", "查询客户信息失败！");
			}
		} catch (Exception e) {
			dataMap.put("msg", "匹配玩家失败！");
			e.printStackTrace();
		}
		return gson.toJson(dataMap);
	}

	
	
}

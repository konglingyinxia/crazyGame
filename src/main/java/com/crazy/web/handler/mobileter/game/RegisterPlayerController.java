package com.crazy.web.handler.mobileter.game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.OutputBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.crazy.core.BMDataContext;
import com.crazy.util.BeanUtils;
import com.crazy.util.CacheConfigTools;
import com.crazy.util.GameUtils;
import com.crazy.util.IP;
import com.crazy.util.IPTools;
import com.crazy.util.UKTools;
import com.crazy.util.cache.CacheHelper;
import com.crazy.util.twm.QRCodeUtil;
import com.crazy.util.wx.ConfigUtil;
import com.crazy.util.wx.WxUserInfo;
import com.crazy.web.handler.Handler;
import com.crazy.web.model.AccountConfig;
import com.crazy.web.model.PlayUser;
import com.crazy.web.model.PlayUserClient;
import com.crazy.web.model.RoomRechargeRecord;
import com.crazy.web.model.RoomTouseRecord;
import com.crazy.web.model.Token;
import com.crazy.web.model.mobileter.murecharge.vo.PlayUserVo;
import com.crazy.web.service.repository.es.PlayUserClientESRepository;
import com.crazy.web.service.repository.es.PlayUserESRepository;
import com.crazy.web.service.repository.es.TokenESRepository;
import com.crazy.web.service.repository.jpa.PlayUserRepository;
import com.crazy.web.service.repository.jpa.RoomRechargeRecordRepository;
import com.crazy.web.service.repository.jpa.RoomTouseRecordRepository;
import com.crazy.web.service.repository.spec.DefaultSpecification;
import com.google.gson.Gson;

/**
 * @ClassName: RegisterPlayerController
 * @Description: TODO(注册玩家控制层)
 * @author dave
 * @date 2017年9月25日 下午4:03:15
 */
@Controller
@RequestMapping("/registerPlayer")
public class RegisterPlayerController extends Handler {

	@Autowired
	private PlayUserRepository playUserRes;

	@Autowired
	private PlayUserClientESRepository playUserClientRes;

	@Autowired
	private PlayUserESRepository playUserESRes;

	@Autowired
	private TokenESRepository tokenESRes;

	@Autowired
	private RoomRechargeRecordRepository roomRechargeRecordRepository;

	@Autowired
	private RoomTouseRecordRepository roomTouseRecordRepository;

	@RequestMapping("/wxLogin")
	public String wxLogin(ModelMap map, String code, HttpSession session, String invitationcode, HttpServletRequest request) {
		/** 请求结果 */
		String result = WxUserInfo.getWxUserInfo(code);// 根据code获取微信用户信息
		JSONObject jsonObject = (JSONObject) JSON.parse(result);
		Token userToken = null;
		PlayUserClient playUserClient = null;
		Gson gson = new Gson();
		try {
			if (null != jsonObject.get("openid")) {
				PlayUser playUser = gson.fromJson(result, PlayUser.class);
				PlayUser newPlayUser = playUserRes.findByOpenid(playUser.getOpenid());
				if (null == newPlayUser) {
					playUser.setInvitationcode(UKTools.getUUID());
					playUser.setCards(10);
					RoomRechargeRecord roomRechargeRecord = new RoomRechargeRecord();
					roomRechargeRecord.setUserName(playUser.getNickname());
					roomRechargeRecord.setInvitationCode(playUser.getInvitationcode());
					roomRechargeRecord.setRoomCount(10);
					roomRechargeRecord.setPayAmount(BigDecimal.valueOf(30.00));
					roomRechargeRecord.setDirectlyTheLastAmount(BigDecimal.valueOf(0.00));
					roomRechargeRecord.setIndirectTheLastAmount(BigDecimal.valueOf(0.00));
					playUser.setTrtProfit(BigDecimal.valueOf(0.00));
					playUser.setPinvitationcode(invitationcode);
					playUser.setOrgi(BMDataContext.SYSTEM_ORGI);
					roomRechargeRecordRepository.saveAndFlush(roomRechargeRecord);
					map.addAttribute("url", ConfigUtil.GAME_URL + "?userId=" + playUser.getId());
				} else {
					playUser = newPlayUser;
					map.addAttribute("url", ConfigUtil.GAME_URL + "?userId=" + playUser.getId());
					userToken = tokenESRes.findById(playUser.getToken());
					if (userToken != null) {
						tokenESRes.delete(userToken);
						userToken = null;
					}
				}

				String ip = UKTools.getIpAddr(request);
				IP ipdata = IPTools.getInstance().findGeography(ip);
				userToken = new Token();
				userToken.setIp(ip);
				userToken.setRegion(ipdata.getProvince() + ipdata.getCity());
				userToken.setId(UKTools.getUUID());
				userToken.setUserid(playUser.getId());
				userToken.setCreatetime(new Date());
				userToken.setOrgi(playUser.getOrgi());
				AccountConfig config = CacheConfigTools.getGameAccountConfig(BMDataContext.SYSTEM_ORGI);
				if (config != null && config.getExpdays() > 0) {
					userToken.setExptime(new Date(System.currentTimeMillis() + 60 * 60 * 24 * config.getExpdays() * 1000));// 默认有效期 ， 7天
				} else {
					userToken.setExptime(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 7 * 1000));// 默认有效期 ， 7天
				}
				userToken.setLastlogintime(new Date());
				userToken.setUpdatetime(new Date(0));
				playUserClient = playUserClientRes.findById(userToken.getUserid());
				if (playUserClient == null) {
					try {
						playUserClient = register(playUser, ipdata, request);
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}

				tokenESRes.save(userToken);
				playUser.setToken(userToken.getId());
				playUserRes.saveAndFlush(playUser);
				playUserClient.setToken(userToken.getId());
				CacheHelper.getApiUserCacheBean().put(userToken.getId(), userToken, userToken.getOrgi());
				CacheHelper.getApiUserCacheBean().put(playUserClient.getId(), playUserClient, userToken.getOrgi());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/apps/business/platform/game/wxGetCode/main";
	}

	/**
	 * @Title: findPlayUserInfo
	 * @Description: TODO(获取回显用户信息)
	 * @param token
	 * @return 设定文件 JSONObject 返回类型
	 */
	@ResponseBody
	@RequestMapping("/findPlayUserInfo")
	public JSONObject findPlayUserInfo(String token) {
		Map<Object, Object> dataMap = new HashMap<Object, Object>();
		try {
			PlayUser playUser = playUserRes.findByToken(token);
			dataMap.put("playUser", playUser);
		} catch (Exception e) {
			e.printStackTrace();
			dataMap.put("success", false);
			dataMap.put("msg", "查询失败");
		}
		return (JSONObject) JSONObject.toJSON(dataMap);
	}

	/**
	 * @Title: findRegisterPlayerList
	 * @Description: TODO(查询玩家信息)
	 * @param playUser
	 * @return 设定文件 JSONObject 返回类型
	 */
	@ResponseBody
	@RequestMapping("/findRegisterPlayerList")
	public JSONObject findRegisterPlayerList(PlayUser playUser, Integer page, Integer limit) {
		Map<Object, Object> dataMap = new HashMap<Object, Object>();
		try {
			Pageable pageable = new PageRequest(page - 1, limit);
			DefaultSpecification<PlayUser> spec = new DefaultSpecification<PlayUser>();
			if (null != playUser.getNickname() && !playUser.getNickname().equals("")) spec.setParams("nickname", "like", "%" + playUser.getNickname() + "%");
			Page<PlayUser> p = playUserRes.findAll(spec, pageable);
			List<PlayUserVo> puolist = new ArrayList<PlayUserVo>();
			for (PlayUser pu : p.getContent()) {
				PlayUserVo puv = new PlayUserVo();
				BeanUtils.copyProperties(pu, puv);
				if (null != puv.getPinvitationcode()) {
					String supAccount = playUserRes.findByInvitationcode(puv.getPinvitationcode()).getNickname();
					puv.setSupAccount(supAccount);
				}
				int subCount = playUserRes.countByPinvitationcode(puv.getInvitationcode());
				puv.setSubCount(String.valueOf(subCount));
				puolist.add(puv);
			}
			dataMap.put("data", puolist);
			dataMap.put("count", p.getTotalElements());
			dataMap.put("code", 0);
		} catch (Exception e) {
			e.printStackTrace();
			dataMap.put("success", false);
			dataMap.put("msg", "查询失败");
		}
		return (JSONObject) JSONObject.toJSON(dataMap);
	}

	/**
	 * 方法描述: 微信绑定平台的用户手机号<br>
	 * 作者：田帅 <br>
	 * 创建时间：2017-09-16 <br>
	 * 版本：V1.0
	 */
	@RequestMapping("/wxBound")
	@ResponseBody
	public Object wxBound(String mobile, String openid, String nickname, String headimgurl) {
		Map<String, Object> json = new HashMap<String, Object>();
		System.out.println("mobile=" + mobile);
		System.out.println("openid=" + openid);
		System.out.println("nickname=" + nickname);
		System.out.println("headimgurl=" + headimgurl);
		if (mobile == null || mobile.isEmpty() || openid == null || openid.isEmpty()) {
			json.put("status", "203");
			json.put("message", "参数不完整");
			return json;
		}
		// 赋值书手机号
		/* 根据手机号查询账号是否存在 */
		// 该手机号还没有注册
		// 该手机号已注册
		// 先查询该手机号是否已绑定微信openid
		return json;
	}

	/**
	 * @Title: buildPinvitationcode
	 * @Description: TODO(添加邀请码)
	 * @param playUser
	 * @return 设定文件 JSONObject 返回类型
	 */
	@ResponseBody
	@RequestMapping("/buildPinvitationcode")
	public JSONObject buildPinvitationcode(PlayUser playUser) {
		Map<Object, Object> dataMap = new HashMap<Object, Object>();
		try {
			playUserRes.setPinvitationcodeById(playUser.getPinvitationcode(), playUser.getId());
		} catch (Exception e) {
			e.printStackTrace();
			dataMap.put("success", false);
			dataMap.put("msg", "添加邀请码失败");
		}
		return (JSONObject) JSONObject.toJSON(dataMap);
	}

	/**
	 * @Title: deductRoomCard
	 * @Description: TODO(扣除房卡)
	 * @param playUser
	 * @return 设定文件 JSONObject 返回类型
	 */
	@ResponseBody
	@RequestMapping("/deductRoomCard")
	public JSONObject deductRoomCard(PlayUser playUser) {
		Map<Object, Object> dataMap = new HashMap<Object, Object>();
		try {
			PlayUser newPlayUser = playUserRes.findById(playUser.getId());
			RoomTouseRecord roomTouseRecord = new RoomTouseRecord();
			roomTouseRecord.setInvitationCode(newPlayUser.getInvitationcode());
			roomTouseRecord.setUserName(newPlayUser.getNickname());
			roomTouseRecord.setUseRoomCount(1);
			int cards = newPlayUser.getCards() - 1;
			roomTouseRecord.setSurplusRoomCount(cards);
			if (cards < 0) {
				throw new Exception("扣卡失败");
			}
			roomTouseRecordRepository.save(roomTouseRecord);
			playUserRes.setCardsById(cards, newPlayUser.getId());
		} catch (Exception e) {
			e.printStackTrace();
			dataMap.put("success", false);
			dataMap.put("msg", e.getMessage());
		}
		return (JSONObject) JSONObject.toJSON(dataMap);
	}

	/**
	 * 注册用户
	 * 
	 * @param player
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public PlayUserClient register(PlayUser player, IP ipdata, HttpServletRequest request) throws IllegalAccessException, InvocationTargetException {
		PlayUserClient playUserClient = GameUtils.create(player, ipdata, request);
		return playUserClient;
	}

	/**
	 * @Title: getEWMImage
	 * @Description: TODO(获取二维码)
	 * @param token
	 * @param response 设定文件 void 返回类型
	 */
	@ResponseBody
	@RequestMapping("/getEWMImage")
	public void getEWMImage(String token, HttpServletResponse response) {
		String invitationcode = playUserRes.findByToken(token).getInvitationcode();
		String text = ConfigUtil.GAME_URL + "?invitationcode=" + invitationcode;
		// 生成二维码
		try {
			BufferedImage img = QRCodeUtil.getImage(text);
			ImageIO.write(img, "JPG", response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

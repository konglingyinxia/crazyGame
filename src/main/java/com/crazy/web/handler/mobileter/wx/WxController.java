package com.crazy.web.handler.mobileter.wx;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.crazy.util.StateUtils;
import com.crazy.util.wx.ConfigUtil;
import com.crazy.util.wx.GetWxOrderno;
import com.crazy.util.wx.PayCommonUtil;
import com.crazy.util.wx.RequestHandler;
import com.crazy.util.wx.Sha1Util;
import com.crazy.web.model.Bill;
import com.crazy.web.model.Money;
import com.crazy.web.model.PlayUser;
import com.crazy.web.model.RoomRechargeRecord;
import com.crazy.web.model.RunHistory;
import com.crazy.web.model.WxConfig;
import com.crazy.web.service.repository.jpa.BillRepository;
import com.crazy.web.service.repository.jpa.MoneyRepository;
import com.crazy.web.service.repository.jpa.PlayUserRepository;
import com.crazy.web.service.repository.jpa.RoomRechargeRecordRepository;
import com.crazy.web.service.repository.jpa.RunHistoryRepository;
import com.crazy.web.service.repository.jpa.WxConfigRepository;

/**
 * 类描述：微信授权登录 <br>
 * 作者：田帅 <br>
 * 创建时间：2017-09-16 <br>
 * 版本：V1.0
 */
@Controller
@RequestMapping(value = "/wxController")
public class WxController {

	private static final Logger logger = Logger.getLogger(WxController.class);

	@Autowired
	private PlayUserRepository playUserRes;

	@Autowired
	private RoomRechargeRecordRepository roomRechargeRecordRepository;

	@Autowired
	private RunHistoryRepository runHistoryRepository;

	@Autowired
	private WxConfigRepository wxConfigRepository;

	@Autowired
	private BillRepository billRepository;

	@Autowired
	private MoneyRepository moneyRepository;

	/**
	 * 跳转微信 wxController/wxLoginHtml
	 * 
	 * @return
	 */
	@RequestMapping(value = "/wxLoginHtml")
	public String wxMain(ModelMap map) {
		map.addAttribute("userName", "");
		return "/apps/business/platform/game/wxGetCode/main";
	}

	@RequestMapping(value = "/wxLoginHtml1")
	public String wxMain1(ModelMap map) {
		map.addAttribute("userName", "");
		return "/apps/business/platform/game/wxGetCode/main1";
	}

	/**
	 * 微信支付界面
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/wxPayHtml")
	public String wxPayHtml(ModelMap map, String roomNum, String token) {
		map.addAttribute("token", token);
		map.addAttribute("roomNum", roomNum);
		return "/apps/business/platform/game/wxGetCode/WxPay";
	}

	/**
	 * 微信授权用户信息返回code地址拼接
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getLoginCode")
	@ResponseBody
	public String getLoginCode(String invitationcode) {
		String oauth2 = ConfigUtil.OAUTH2_URL;
		String appid = ConfigUtil.APPIDH5;// 公众号appid
		String redirect_uri = ConfigUtil.CODE_URL;// 返回code值地址
		if (invitationcode != null && !"".equals(invitationcode)) {
			redirect_uri += "%3finvitationcode%3d" + invitationcode;
		}
		String wxLogin = oauth2 + "?appid=" + appid + "&redirect_uri=" + redirect_uri + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
		return wxLogin;
	}

	@RequestMapping(value = "/toWXAuth")
	public String toWXAuth(HttpServletResponse response, String invitationcode) {
		String oauth2 = ConfigUtil.OAUTH2_URL;
		String appid = ConfigUtil.APPIDH5;// 公众号appid
		String redirect_uri = ConfigUtil.CODE_URL;// 返回code值地址
		if (invitationcode != null && !"".equals(invitationcode)) {
			redirect_uri += "%3finvitationcode%3d" + invitationcode;
		}
		String wxLogin = oauth2 + "?appid=" + appid + "&redirect_uri=" + redirect_uri + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
		try {
			response.sendRedirect(wxLogin);
		} catch (IOException e) {
			logger.error(e) ;
		}
		return "login";
	}

	/**
	 * 获取当前用户token wxController/getWxUserToken
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getWxUserToken")
	@ResponseBody
	public Object getWxUserToken(HttpSession session, String userId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			if (StringUtils.isNotBlank(userId)) {
				PlayUser playUser = playUserRes.findById(userId);
				if (null != playUser) {
					dataMap.put("token", playUser.getToken());
					dataMap.put("playUser", playUser);
					dataMap.put("success", true);
				} else {
					dataMap.put("success", false);
					dataMap.put("token", "");
					dataMap.put("msg", "当前用户信息已失效！");
				}
			}
		} catch (Exception e) {
			dataMap.put("success", false);
			dataMap.put("token", "");
			dataMap.put("msg", "获取用户数据异常！");
		}
		return dataMap;
	}

	/**
	 * 方法描述: 跳转到h5微信支付<br>
	 * 作者：田帅 <br>
	 * 测试 创建时间：2017-09-16 <br>
	 * 版本：V1.0
	 */
	@RequestMapping(value = "/toH5WxPay")
	public Object toH5WxPay(String out_trade_no, String total_fee, String body) {
		System.out.println("out_trade_no=" + out_trade_no);
		System.out.println("total_fee=" + total_fee);
		System.out.println("body=" + body);
		String backUri = "";// 微信授权后跳转的支付界面
		backUri = backUri + "?out_trade_no=" + out_trade_no + "&total_fee=" + total_fee + "&body=" + body;
		// URLEncoder.encode 后可以在backUri 的url里面获取传递的所有参数
		backUri = URLEncoder.encode(backUri);
		// scope 参数视各自需求而定，这里用scope=snsapi_base 不弹出授权页面直接授权目的只获取统一支付接口的openid
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" + "appid=" + ConfigUtil.APPIDH5 + "&redirect_uri=" + backUri
				+ "&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect";
		return "redirect:" + url;
	}

	/**
	 * 方法描述: 获取微信配置信息<br>
	 * 作者：田帅 <br>
	 * 创建时间：2017-09-16 <br>
	 * 版本：V1.0 wxController/getWxConfig
	 */
	@RequestMapping(value = "/getWxConfig")
	@ResponseBody
	public Object getWxConfig(String url) {
		Map<String, String> ret = new HashMap<String, String>();
		WxConfig ticket = wxConfigRepository.selectTicket("2");
		String jsapi_ticket = ticket.getTypevalue();
		String signature = "";
		// 注意这里参数名必须全部小写，且必须有序
		String nonce_str = create_nonce_str();
		String timestamp = create_timestamp();
		String string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string1.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ret.put("url", url);
		ret.put("jsapi_ticket", jsapi_ticket);
		ret.put("nonceStr", nonce_str);
		ret.put("timestamp", timestamp);
		ret.put("signature", signature);
		ret.put("appId", ConfigUtil.APPIDH5);
		return ret;
	}

	/**
	 * 方法描述: H5向微信请求支付信息<br>
	 * 作者：田帅 <br>
	 * 创建时间：2017-09-16 <br>
	 * 版本：V1.0
	 */
	@RequestMapping(value = "/getWXPayXmlH5")
	@ResponseBody
	public Object getWXPayXmlH5(HttpSession session, HttpServletRequest request, HttpServletResponse response, @RequestParam(defaultValue = "0") int roomNum, String token) {
		Map<String, Object> json = new HashMap<String, Object>();
		// 获取付款用户openid
		PlayUser playUser = playUserRes.findByToken(token);
		String openid = playUser.getOpenid();
		// String openid = "oTGVJtwV_U8M9jUS9aLk36iA_wys";
		if (openid == null) {
			json.put("status", false);
			json.put("msg", "用户不存在！");
			return json;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHHmmssSSS");
		// 生成32位订单号
		String out_trade_no = "YX" + formatter.format(new Date());// 充值订单号时间戳
		out_trade_no += formatter.format(new Date());// 充值订单号时间戳
		int finalmoney = 0;
		if (roomNum == 4) {
			finalmoney = (int) (ConfigUtil.ROOM_4 * 100);
		} else if (roomNum == 8) {
			finalmoney = (int) (ConfigUtil.ROOM_8 * 100);
		}
		// 金额转化为分为单位
		request.getSession();
		// 随机数
		String nonce_str = PayCommonUtil.CreateNoncestr();
		// 支付成功后显示的标题
		String body = "测试";
		// 订单生成的机器 IP
		String spbill_create_ip = PayCommonUtil.getIpAddress(request);
		spbill_create_ip = (spbill_create_ip.split(","))[0];
		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url = ConfigUtil.NOTIFY_URL;
		String trade_type = "JSAPI";
		SortedMap<Object, Object> signParams = new TreeMap<Object, Object>();
		signParams.put("appid", ConfigUtil.APPIDH5);// app_id
		signParams.put("body", body);// 商品参数信息
		signParams.put("mch_id", ConfigUtil.MCH_IDH5);// 微信商户账号
		signParams.put("nonce_str", nonce_str);// 32位不重复的编号
		signParams.put("notify_url", notify_url);// 回调页面
		signParams.put("openid", openid);// openid
		signParams.put("out_trade_no", out_trade_no);// 订单编号
		signParams.put("spbill_create_ip", spbill_create_ip);// 请求的实际ip地址
		signParams.put("total_fee", finalmoney + "");// 支付金额 单位为分
		signParams.put("trade_type", trade_type);// 付款类型

		RequestHandler reqHandler = new RequestHandler(request, response);
		reqHandler.init(ConfigUtil.APPIDH5, ConfigUtil.APP_SECRECTH5, ConfigUtil.API_KEYH5);

		String sign = PayCommonUtil.createSign("UTF-8", signParams);// 生成签名
		String xml = "<xml>" + "<appid>" + ConfigUtil.APPIDH5 + "</appid>" + "<body><![CDATA[" + body + "]]></body>" + "<mch_id>" + ConfigUtil.MCH_IDH5 + "</mch_id>"
				+ "<nonce_str>" + nonce_str + "</nonce_str>" + "<notify_url>" + notify_url + "</notify_url>" + "<openid>" + openid + "</openid>" + "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>" + "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>" + "<total_fee>" + finalmoney + "</total_fee>" + "<trade_type>" + trade_type
				+ "</trade_type>" + "<sign>" + sign + "</sign>" + "</xml>";
		logger.info("xml=" + xml);
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		String prepay_id = "";
		try {
			prepay_id = new GetWxOrderno().getPayNo(createOrderURL, xml);
			if (prepay_id.equals("")) {
				json.put("status", false);
				json.put("msg", "订单支付异常！");
				return json;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
		String appid2 = ConfigUtil.APPIDH5;
		String timestamp = Sha1Util.getTimeStamp();
		String nonceStr2 = nonce_str;
		String prepay_id2 = "prepay_id=" + prepay_id;
		String packages = prepay_id2;
		finalpackage.put("appId", appid2);
		finalpackage.put("timeStamp", timestamp);
		finalpackage.put("nonceStr", nonceStr2);
		finalpackage.put("package", packages);
		finalpackage.put("signType", ConfigUtil.SIGN_TYPE);
		String finalsign = reqHandler.createSign(finalpackage);

		json.put("appid", appid2);
		json.put("timeStamp", timestamp);
		json.put("nonceStr", nonceStr2);
		json.put("package", packages);
		json.put("paySign", finalsign);
		json.put("gameUrl", ConfigUtil.GAME_URL);
		json.put("token", playUser.getToken());
		json.put("userId", playUser.getId());
		json.put("status", true);
		return json;

	}

	/**
	 * @Title: rechargeManagement
	 * @Description: TODO(支付完成后调整数据库)
	 * @param playUser 用户信息
	 * @param roomCount 房卡数量
	 * @return 设定文件 String 返回类型
	 */
	@ResponseBody
	@RequestMapping("/rechargeManagement")
	public JSONObject rechargeManagement(PlayUser playUser, int roomCount) {
		Map<Object, Object> dataMap = new HashMap<Object, Object>();
		RoomRechargeRecord roomRechargeRecord = new RoomRechargeRecord();
		Bill bill = new Bill();

		Double adtaoProfit = Double.valueOf(roomCount * ConfigUtil.ROOM);
		Money money = moneyRepository.findByIdAndType(1, 1);
		BigDecimal ye = money.getBalance();
		ye = ye.add(BigDecimal.valueOf(adtaoProfit));// 算出账户余额
		moneyRepository.setBalanceById(ye, 1);// 修改账户余额
		try {
			PlayUser zjPlayUser = playUserRes.findByToken(playUser.getToken());

			// 账单
			bill.setUserName(zjPlayUser.getNickname());
			bill.setBillMode("微信");
			bill.setBillType(1);
			bill.setIncomeAmount(BigDecimal.valueOf(adtaoProfit));
			bill.setAccountAmount(ye);

			roomRechargeRecord.setUserName(zjPlayUser.getNickname());
			roomRechargeRecord.setInvitationCode(zjPlayUser.getInvitationcode());
			roomRechargeRecord.setRoomCount(roomCount);
			roomRechargeRecord.setPayAmount(BigDecimal.valueOf(adtaoProfit));
			roomRechargeRecord.setPaymentMethod("微信");

			int cards = zjPlayUser.getCards() + roomCount;
			Double ziFenrun = adtaoProfit * StateUtils.ZJDZ;// 这笔交易直接自己分润金额
			adtaoProfit = adtaoProfit - ziFenrun;
			BigDecimal zjTrtProfit = zjPlayUser.getTrtProfit();
			zjTrtProfit = zjTrtProfit.add(BigDecimal.valueOf(ziFenrun));
			playUserRes.setCardsAndTrtProfitById(cards, zjTrtProfit, zjPlayUser.getId());// 修改充值完的房卡数量

			// 保存自己分润历史
			RunHistory zjRunHistory = new RunHistory();
			zjRunHistory.setUserName(zjPlayUser.getNickname());
			zjRunHistory.setInvitationCode(zjPlayUser.getInvitationcode());
			zjRunHistory.setGetProfitAmount(BigDecimal.valueOf(ziFenrun));
			zjRunHistory.setSourceId(zjPlayUser.getId());
			zjRunHistory.setSourceName(zjPlayUser.getNickname());
			runHistoryRepository.saveAndFlush(zjRunHistory);

			// 计算分润
			if (null != zjPlayUser.getPinvitationcode()) {// 存在直接上级
				PlayUser zPlayUser = playUserRes.findByInvitationcode(zjPlayUser.getPinvitationcode());
				Double fenrun = adtaoProfit * StateUtils.ZJFR;// 这笔交易直接上级分润金额
				adtaoProfit = adtaoProfit - fenrun;
				BigDecimal trtProfit = zPlayUser.getTrtProfit();
				trtProfit = trtProfit.add(BigDecimal.valueOf(fenrun));// 这笔交易直接上级分润金额加上总额
				playUserRes.setTrtProfitById(trtProfit, zPlayUser.getId());
				roomRechargeRecord.setDirectlyTheLastAmount(trtProfit);

				// 保存分润历史
				RunHistory runHistory = new RunHistory();
				runHistory.setUserName(zPlayUser.getNickname());
				runHistory.setInvitationCode(zPlayUser.getInvitationcode());
				runHistory.setGetProfitAmount(BigDecimal.valueOf(fenrun));
				runHistory.setSourceId(zjPlayUser.getId());
				runHistory.setSourceName(zjPlayUser.getNickname());
				runHistoryRepository.saveAndFlush(runHistory);

				if (null != zPlayUser.getPinvitationcode()) {// 存在间接接上级
					PlayUser jPlayUser = playUserRes.findByInvitationcode(zPlayUser.getPinvitationcode());
					Double jjFenrun = adtaoProfit * StateUtils.JJFR;// 这笔交易间接上级分润金额
					adtaoProfit = adtaoProfit - jjFenrun;
					BigDecimal jjTrtProfit = jPlayUser.getTrtProfit();
					jjTrtProfit = jjTrtProfit.add(BigDecimal.valueOf(jjFenrun));// 这笔交易间接上级分润金额加上总额
					playUserRes.setTrtProfitById(trtProfit, jPlayUser.getId());
					roomRechargeRecord.setIndirectTheLastAmount(jjTrtProfit);

					// 保存分润历史
					RunHistory jjRunHistory = new RunHistory();
					jjRunHistory.setUserName(jPlayUser.getNickname());
					jjRunHistory.setInvitationCode(jPlayUser.getInvitationcode());
					jjRunHistory.setGetProfitAmount(BigDecimal.valueOf(jjFenrun));
					jjRunHistory.setSourceId(zjPlayUser.getId());
					jjRunHistory.setSourceName(zjPlayUser.getNickname());
					runHistoryRepository.saveAndFlush(jjRunHistory);
				}
			}

			roomRechargeRecord.setAdtaoProfit(BigDecimal.valueOf(adtaoProfit));
			// 保存房卡充值历史
			roomRechargeRecordRepository.saveAndFlush(roomRechargeRecord);
			billRepository.save(bill);
		} catch (Exception e) {
			e.printStackTrace();
			dataMap.put("success", false);
			dataMap.put("msg", "存档失败");
		}
		return (JSONObject) JSONObject.toJSON(dataMap);
	}

	/**
	 * 企业向个人打款
	 * 
	 * @return
	 */
	@RequestMapping("/wxTransfersAmount")
	@ResponseBody
	public Object wxTransfersAmount(String token, HttpSession session, HttpServletRequest request) {
		SortedMap<Object, Object> signParams = new TreeMap<Object, Object>();
		// 获取付款用户id
		// String openid = ((PlayUser) session.getAttribute("mgPlayUser")).getOpenid();// 获取用户id
		String openid = playUserRes.findByToken(token).getOpenid();// 获取用户id

		// 生成32位订单号
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHHmmssSSS");
		String partner_trade_no = "YX" + formatter.format(new Date()) + formatter.format(new Date());// 充值订单号时间戳
		// 随机字符串
		String nonce_str = PayCommonUtil.CreateNoncestr();
		// 订单生成的机器IP
		String spbill_create_ip = PayCommonUtil.getIpAddress(request).split(",")[0];
		int amount = 1;
		signParams.put("amount", amount);// 金额 企业付款金额，单位为分
		signParams.put("check_name", "NO_CHECK");// 校验用户姓名选项 NO_CHECK：不校验真实姓名 FORCE_CHECK：强校验真实姓名
		signParams.put("desc", "测试");// 企业付款描述信息
		signParams.put("mchid", ConfigUtil.MCH_IDH5);// 商户号
		signParams.put("mch_appid", ConfigUtil.APPIDH5);// 商户账号appid
		signParams.put("nonce_str", nonce_str);// 随机字符串
		signParams.put("openid", openid);// 用户openid
		signParams.put("partner_trade_no", partner_trade_no);// 商户订单号
		signParams.put("spbill_create_ip", spbill_create_ip);// 调用接口的机器Ip地址
		String sign = PayCommonUtil.createSign("UTF-8", signParams);// 生成签名
		signParams.put("sign", sign);// 签名

		String xml = "<xml>" + "<amount>" + amount + "</amount>" + "<check_name>NO_CHECK</check_name>" + "<desc><![CDATA[测试]]></desc>" + "<mchid>" + ConfigUtil.MCH_IDH5
				+ "</mchid>" + "<mch_appid>" + ConfigUtil.APPIDH5 + "</mch_appid>" + "<nonce_str>" + nonce_str + "</nonce_str>" + "<openid>" + openid + "</openid>"
				+ "<partner_trade_no>" + partner_trade_no + "</partner_trade_no>" + "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>" + "<sign>" + sign + "</sign>"
				+ "</xml>";
		logger.info("xml=" + xml);
		Map<String, Object> json = new HashMap<String, Object>();
		String returnCode = "";
		try {
			returnCode = new GetWxOrderno().getTransfersAmount(ConfigUtil.WX_TRANSFERS_AMOUNT_URL, xml);
			if (returnCode.equals("SUCCESS")) {
				json.put("code", true);
				json.put("msg", "已打款");
			} else {
				json.put("code", false);
				json.put("msg", "打款异常");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", false);
			json.put("msg", "打款异常");
		}
		return json;
	}

	private static String create_nonce_str() {
		return UUID.randomUUID().toString();
	}

	private static String create_timestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
}

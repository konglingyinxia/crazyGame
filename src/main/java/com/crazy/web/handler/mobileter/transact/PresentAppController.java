package com.crazy.web.handler.mobileter.transact;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.crazy.web.handler.Handler;
import com.crazy.web.model.PlayUser;
import com.crazy.web.model.PresentApp;
import com.crazy.web.model.ProManagement;
import com.crazy.web.model.mobileter.murecharge.vo.PresentAppVo;
import com.crazy.web.service.repository.jpa.PlayUserRepository;
import com.crazy.web.service.repository.jpa.PresentAppRepository;
import com.crazy.web.service.repository.jpa.ProManagementRepository;
import com.crazy.web.service.repository.spec.DefaultSpecification;
import com.google.gson.Gson;

/**
 * @ClassName: PresentAppController
 * @Description: TODO(提现记录表)
 * @author dave
 * @date 2017年9月26日 上午9:38:03
 */
@Controller
@RequestMapping("/presentapp")
public class PresentAppController extends Handler {

	@Autowired
	private ProManagementRepository proManagementRepository;

	@Autowired
	private PresentAppRepository presentAppRepository;

	@Autowired
	private PlayUserRepository playUserRes;

	/**
	 * 提现审批
	 * 
	 * @return
	 */
	@RequestMapping({ "/index" })
	public ModelAndView index(ModelMap map, HttpServletRequest request) {
		return request(super.createAppsTempletResponse("/apps/business/platform/room/approvel/index"));
	}

	/**
	 * @Title: findPresentappList
	 * @Description: TODO(提现记录)
	 * @param presentApp
	 * @param page 页码
	 * @param limit 页尾
	 * @param firstParms 开始金额
	 * @param endParms 结束金额
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return 设定文件 JSONObject 返回类型
	 */
	@ResponseBody
	@RequestMapping("/findPresentappList")
	public JSONObject findPresentappList(PresentApp presentApp, Integer page, Integer limit, Double firstParms, Double endParms, Date startDate, Date endDate) {
		Map<Object, Object> dataMap = new HashMap<Object, Object>();
		try {
			Pageable pageable = new PageRequest(page - 1, limit);
			DefaultSpecification<PresentApp> spec = new DefaultSpecification<PresentApp>();
			if (null != presentApp.getUserName() && !presentApp.getUserName().equals("")) spec.setParams("userName", "like", "%" + presentApp.getUserName() + "%");

			if (null != firstParms) spec.setParams("amountMoney", ">=", firstParms);

			if (null != endParms) spec.setParams("amountMoney", "<=", endParms);

			if (null != startDate) spec.setParams("presentAppTime", ">=", startDate);

			if (null != endDate) spec.setParams("presentAppTime", "<=", endDate);

			if (3 != presentApp.getPreState()) {
				spec.setParams("preState", "eq", presentApp.getPreState());
			} else if (3 == presentApp.getPreState()) {

			}
			Page<PresentApp> p = presentAppRepository.findAll(spec, pageable);
			dataMap.put("data", p.getContent());
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
	 * @Title: appForCash
	 * @Description: TODO(申请提现)
	 * @param presentApp 接收对象
	 * @param playUser 缓存对象
	 * @return 设定文件 JSONObject 返回类型
	 */
	@ResponseBody
	@RequestMapping("/appForCash")
	public JSONObject appForCash(PresentApp presentApp, @SessionAttribute("mgPlayUser") PlayUser playUser) {
		Map<Object, Object> dataMap = new HashMap<Object, Object>();
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHHmmssSSS");
			String time = formatter.format(new Date());
			presentApp.setApplicationNum(time);
			presentApp.setUserName(playUser.getNickname());
			presentApp.setInvitationCode(playUser.getInvitationcode());
			presentApp.setPlayUserId(playUser.getId());
			presentApp.setOpenid(playUser.getOpenid());
			presentAppRepository.saveAndFlush(presentApp);
		} catch (Exception e) {
			e.printStackTrace();
			dataMap.put("success", false);
			dataMap.put("msg", "申请失败");
		}
		return (JSONObject) JSONObject.toJSON(dataMap);
	}

	/**
	 * @Title: cashWithdrawal
	 * @Description: TODO(通过审批)
	 * @param parms
	 * @return 设定文件 JSONObject 返回类型
	 */
	@ResponseBody
	@RequestMapping("/cashWithdrawal")
	public JSONObject cashWithdrawal(String parms) {
		Map<Object, Object> dataMap = new HashMap<Object, Object>();
		try {
			PresentAppVo presentAppVo = getPresentAppVoByJson(parms);
			for (String id : presentAppVo.getId()) {
				PresentApp presentApp = presentAppRepository.findById(id);
				// 接下来要对挨个数据分润，然后更新数据库数据结构
				// 企业打钱接口。。。。。

				// 更新数据库数据结构
				BigDecimal zjTrtProfit = playUserRes.findById(presentApp.getPlayUserId()).getTrtProfit();
				zjTrtProfit = zjTrtProfit.subtract(presentApp.getAmountMoney());
				playUserRes.setTrtProfitById(zjTrtProfit, presentApp.getPlayUserId());// 更新人员剩余分润总额度
				// 生成提现历史
				ProManagement pm = new ProManagement();
				pm.setUserName(presentApp.getUserName());
				pm.setInvitationCode(presentApp.getInvitationCode());
				pm.setAmountMoney(presentApp.getAmountMoney());
				pm.setProType("微信");
				pm.setTrtProfit(zjTrtProfit);
				proManagementRepository.saveAndFlush(pm);
			}
		} catch (Exception e) {
			e.printStackTrace();
			dataMap.put("success", false);
			dataMap.put("msg", "审批失败");
		}
		return (JSONObject) JSONObject.toJSON(dataMap);
	}

	private PresentAppVo getPresentAppVoByJson(String parms) {
		Gson gson = new Gson();
		return gson.fromJson(parms, PresentAppVo.class);
	}
}

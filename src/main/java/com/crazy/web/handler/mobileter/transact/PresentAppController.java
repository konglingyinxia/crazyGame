package com.crazy.web.handler.mobileter.transact;

import java.math.BigDecimal;
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
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.crazy.util.UKTools;
import com.crazy.web.handler.Handler;
import com.crazy.web.model.Bill;
import com.crazy.web.model.PlayUser;
import com.crazy.web.model.PresentApp;
import com.crazy.web.model.ProManagement;
import com.crazy.web.model.RunHistory;
import com.crazy.web.model.mobileter.murecharge.vo.PresentAppVo;
import com.crazy.web.model.vo.RunSummaryVo;
import com.crazy.web.service.repository.jpa.BillRepository;
import com.crazy.web.service.repository.jpa.MoneyRepository;
import com.crazy.web.service.repository.jpa.PlayUserRepository;
import com.crazy.web.service.repository.jpa.PresentAppRepository;
import com.crazy.web.service.repository.jpa.ProManagementRepository;
import com.crazy.web.service.repository.jpa.RunHistoryRepository;
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

	@Autowired
	private MoneyRepository moneyRepository;

	@Autowired
	private BillRepository billRepository;

	@Autowired
	private RunHistoryRepository runHistoryRepository;

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
	 * @Title: runSummary
	 * @Description: TODO(分润汇总)
	 * @param token
	 * @return 设定文件 JSONObject 返回类型
	 */
	@ResponseBody
	@RequestMapping("/runSummary")
	public JSONObject runSummary(String token) {
		Map<Object, Object> dataMap = new HashMap<Object, Object>();
		RunSummaryVo runSummaryVo = new RunSummaryVo();
		try {
			PlayUser playUser = playUserRes.findByToken(token);
			if (null != playUser) {
				runSummaryVo.setTrtProfit(playUser.getTrtProfit());
				runSummaryVo.setPpAmount(playUser.getPpAmount() == null ? BigDecimal.valueOf(0.00) : playUser.getPpAmount());
				int num = playUserRes.countByPinvitationcode(playUser.getInvitationcode());
				runSummaryVo.setSubCount(num);
				BigDecimal sumAmountMoney = proManagementRepository.sumAmountMoneyByInvitationcode(playUser.getInvitationcode());
				runSummaryVo.setAmountPaid(sumAmountMoney == null ? BigDecimal.valueOf(0.00) : sumAmountMoney);
			}

			dataMap.put("data", runSummaryVo);
		} catch (Exception e) {
			e.printStackTrace();
			dataMap.put("success", false);
			dataMap.put("msg", "查询失败");
		}
		return (JSONObject) JSONObject.toJSON(dataMap);
	}

	/**
	 * @Title: runHistoryMySelf
	 * @Description: TODO(查询自己获得分润的历史)
	 * @param token
	 * @param page
	 * @param limit
	 * @return 设定文件 JSONObject 返回类型
	 */
	@ResponseBody
	@RequestMapping("/runHistoryMySelf")
	public JSONObject runHistoryMySelf(String token, Integer page, Integer limit) {
		Map<Object, Object> dataMap = new HashMap<Object, Object>();
		try {
			Pageable pageable = new PageRequest(page, limit);
			DefaultSpecification<RunHistory> spec = new DefaultSpecification<RunHistory>();
			PlayUser playUser = playUserRes.findByToken(token);
			if (null != playUser) {
				String invitationcode = playUser.getInvitationcode();
				spec.setParams("invitationCode", "eq", invitationcode);
			}
			Page<RunHistory> p = runHistoryRepository.findAll(spec, pageable);
			dataMap.put("data", p.getContent());
			dataMap.put("count", p.getTotalElements());
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
	public JSONObject appForCash(PresentApp presentApp, String token) {
		Map<Object, Object> dataMap = new HashMap<Object, Object>();
		try {
			PlayUser playUser = playUserRes.findByToken(token);

			presentApp.setApplicationNum(UKTools.getUUID());
			presentApp.setUserName(playUser.getNickname());
			presentApp.setInvitationCode(playUser.getInvitationcode());
			presentApp.setPlayUserId(playUser.getId());
			presentApp.setOpenid(playUser.getOpenid());
			presentAppRepository.saveAndFlush(presentApp);// 提现申请

			BigDecimal trtProfit = playUser.getTrtProfit().subtract(presentApp.getAmountMoney());// 去掉申请提现的金额剩余的分润金额
			playUserRes.setTrtProfitAndPpAmountById(trtProfit, presentApp.getAmountMoney(), playUser.getId());// 修改剩余的分润金额和待通过的金额
			dataMap.put("success", true);
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
				BigDecimal zjTrtProfit = playUserRes.findById(presentApp.getPlayUserId()).getPpAmount();

				// 修改账户余额
				BigDecimal ye = moneyRepository.findById(1).getBalance();
				ye = ye.subtract(presentApp.getAmountMoney());
				moneyRepository.setBalanceById(ye, 1);

				zjTrtProfit = zjTrtProfit.subtract(presentApp.getAmountMoney());
				playUserRes.setPpAmountById(zjTrtProfit, presentApp.getPlayUserId());// 更新人员待审批分润金额总额度

				// 生成提现历史
				ProManagement pm = new ProManagement();
				pm.setUserName(presentApp.getUserName());
				pm.setInvitationCode(presentApp.getInvitationCode());
				pm.setAmountMoney(presentApp.getAmountMoney());
				pm.setProType("微信");
				pm.setTrtProfit(zjTrtProfit);

				// 生成账单历史
				Bill bill = new Bill();
				bill.setUserName(presentApp.getUserName());
				bill.setBillType(2);
				bill.setExpAmount(presentApp.getAmountMoney());// 支出金额
				bill.setBillMode("微信");
				bill.setAccountAmount(ye);// 账户余额
				billRepository.save(bill);

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

package com.crazy.web.handler.mobileter.transact;

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
import com.crazy.web.handler.Handler;
import com.crazy.web.model.RunHistory;
import com.crazy.web.service.repository.jpa.RunHistoryRepository;
import com.crazy.web.service.repository.spec.DefaultSpecification;

/**
 * @ClassName: RunHistoryController
 * @Description: TODO(分润历史控制层)
 * @author dave
 * @date 2017年9月28日 下午2:46:36
 */
@Controller
@RequestMapping("/runhistory")
public class RunHistoryController extends Handler {

	@Autowired
	private RunHistoryRepository runHistoryRepository;

	/**
	 * @Title: runHistoryUrl
	 * @Description: TODO(分润历史调整路径)
	 * @param map
	 * @param request
	 * @return 设定文件 ModelAndView 返回类型
	 */
	@RequestMapping({ "/runHistoryUrl" })
	public ModelAndView runHistoryUrl(ModelMap map, HttpServletRequest request) {
		return request(super.createAppsTempletResponse("/apps/business/platform/room/FenRun/index"));
	}

	/**
	 * @Title: getRunHistoryList
	 * @Description: TODO(查询分润历史)
	 * @param runHistory
	 * @param page
	 * @param limit
	 * @return 设定文件 JSONObject 返回类型
	 */
	@ResponseBody
	@RequestMapping("/runhistory")
	public JSONObject getRunHistoryList(RunHistory runHistory, Integer page, Integer limit) {
		Map<Object, Object> dataMap = new HashMap<Object, Object>();
		try {
			Pageable pageable = new PageRequest(page - 1, limit);
			DefaultSpecification<RunHistory> spec = new DefaultSpecification<RunHistory>();
			if (null != runHistory.getUserName() && !runHistory.getUserName().equals("")) spec.setParams("userName", "like", "%" + runHistory.getUserName() + "%");
			if (null != runHistory.getInvitationCode() && !runHistory.getInvitationCode().equals("")) spec.setParams("invitationCode", "eq", runHistory.getInvitationCode());
			Page<RunHistory> p = runHistoryRepository.findAll(spec, pageable);
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
}

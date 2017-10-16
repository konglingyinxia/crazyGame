package com.crazy.web.handler.mobileter.transact;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.crazy.web.handler.Handler;
import com.crazy.web.model.Bill;
import com.crazy.web.service.repository.jpa.BillRepository;
import com.crazy.web.service.repository.spec.DefaultSpecification;

/**
 * @ClassName: BillController
 * @Description: TODO(账单控制层)
 * @author dave
 * @date 2017年10月16日 上午10:23:09
 */
@Controller
@RequestMapping("/bill")
public class BillController extends Handler {

	@Autowired
	private BillRepository billRepository;

	/**
	 * @Title: findBillList
	 * @Description: TODO(账单列表页)
	 * @param bill
	 * @param page
	 * @param limit
	 * @param startDate
	 * @param endDate
	 * @return 设定文件 JSONObject 返回类型
	 */
	@ResponseBody
	@RequestMapping("/findBillList")
	public JSONObject findBillList(Bill bill, Integer page, Integer limit, Date startDate, Date endDate) {
		Map<Object, Object> dataMap = new HashMap<Object, Object>();
		try {
			Pageable pageable = new PageRequest(page - 1, limit);
			DefaultSpecification<Bill> spec = new DefaultSpecification<Bill>();
			if (null != bill.getBillType() && !bill.getBillType().equals("")) spec.setParams("billType", "eq", bill.getBillType());

			if (null != bill.getBillMode() && !bill.getBillMode().equals("")) spec.setParams("billMode", "eq", bill.getBillMode());

			if (null != startDate) spec.setParams("billTime", ">=", startDate);

			if (null != endDate) spec.setParams("billTime", "<=", endDate);

			Page<Bill> p = billRepository.findAll(spec, pageable);
			dataMap.put("data", p.getContent());
			dataMap.put("count", p.getTotalElements());
			dataMap.put("code", 0);
			dataMap.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			dataMap.put("success", false);
			dataMap.put("msg", "查询失败");
		}
		return (JSONObject) JSONObject.toJSON(dataMap);
	}
}

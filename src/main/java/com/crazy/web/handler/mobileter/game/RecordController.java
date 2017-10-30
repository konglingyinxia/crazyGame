package com.crazy.web.handler.mobileter.game;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crazy.web.handler.Handler;
import com.crazy.web.model.PlayUser;
import com.crazy.web.model.Record;
import com.crazy.web.service.repository.jpa.PlayUserRepository;
import com.crazy.web.service.repository.jpa.RecordRepository;
import com.crazy.web.service.repository.spec.DefaultSpecification;
import com.google.gson.Gson;

@Controller
@RequestMapping("/record")
public class RecordController extends Handler {

	@Autowired
	private PlayUserRepository playUserRes;

	@Autowired
	private RecordRepository recordRepository;

	/**
	 * @Title: perRecord
	 * @Description: TODO(个人战绩)
	 * @param token
	 * @param page
	 * @param limit
	 * @return 设定文件 String 返回类型
	 */
	@ResponseBody
	@RequestMapping("/perRecord")
	private String perRecord(String token, Integer gameType, Integer page, Integer limit) {
		Gson gson = new Gson();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			PlayUser playUser = playUserRes.findByToken(token);
			if (null != playUser) {
				Pageable pageable = new PageRequest(page - 1, limit, new Sort(Direction.DESC, "time"));
				DefaultSpecification<Record> spec = new DefaultSpecification<Record>();
				spec.setParams("playuserId", "eq", playUser.getId());
				spec.setParams("gameType", "eq", gameType);
				Page<Record> p = recordRepository.findAll(spec, pageable);
				dataMap.put("data", p.getContent());
				dataMap.put("count", p.getTotalElements());
			}
		} catch (Exception e) {
			dataMap.put("msg", "查询失败");
			e.printStackTrace();
		}
		return gson.toJson(dataMap);
	}
}

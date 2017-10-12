package com.crazy.web.handler.admin.system;

import com.crazy.util.Menu;
import com.crazy.web.handler.Handler;
import com.google.gson.Gson;
import com.hazelcast.com.eclipsesource.json.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/monitor")
public class HazelcastMonitorController extends Handler {
	
    @RequestMapping("/hazelcast")
    @Menu(type = "admin" , subtype = "metadata" , admin = true)
    public ModelAndView index(ModelMap map , HttpServletRequest request , HttpServletResponse response) throws SQLException {
    	Map<String , Object> jsonObjectMap = new HashMap< String , Object>();
    	map.addAttribute("systemStatics", new Gson().toJson(jsonObjectMap)) ;
    	
    	response.setCharacterEncoding("UTF-8");
    	response.setContentType("application/json; charset=utf-8");
        return request(super.createRequestPageTempletResponse("/admin/system/monitor/hazelcast"));
    }
    /**
     * 转换统计数据
     * @param json
     * @return
     */
    @SuppressWarnings("unused")
	private Map<String , Object> convert(JsonObject json){
    	Map<String , Object> values = new HashMap<String , Object>();
    	for(String key : json.names()){
    		values.put(key, json.get(key)) ;
    	}
    	return values;
    }
}
package com.crazy.web.handler.apps;

import com.crazy.util.Menu;
import com.crazy.web.handler.Handler;
import com.crazy.web.service.repository.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AppsController extends Handler {
	
	@Autowired
	private UserRepository userRes;
	
	@RequestMapping({"/apps/content"})
	@Menu(type="apps", subtype="content")
	public ModelAndView content(ModelMap map , HttpServletRequest request){
		return request(super.createAppsTempletResponse("/apps/desktop/index"));
	}
}

package com.crazy.web.handler.apps.business.platform;

import com.crazy.util.Menu;
import com.crazy.web.handler.Handler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/apps/platform")
public class PlatformController extends Handler {
	
	@RequestMapping({"/index"})
	@Menu(type="apps", subtype="content")
	public ModelAndView content(ModelMap map , HttpServletRequest request){
		return request(super.createAppsTempletResponse("/apps/business/platform/desktop/index"));
	}
}

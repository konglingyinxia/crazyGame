package com.crazy.web.handler.apps.business.platform;

import com.crazy.util.Menu;
import com.crazy.web.handler.Handler;
import com.crazy.web.service.repository.jpa.AccountConfigRepository;
import com.crazy.web.service.repository.jpa.AiConfigRepository;
import com.crazy.web.service.repository.jpa.GameConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/apps/platform")
public class MJCardsTypeController extends Handler {
	
	@Autowired
	private AccountConfigRepository accountRes ;
	
	@Autowired
	private GameConfigRepository gameConfigRes ;
	
	@Autowired
	private AiConfigRepository aiConfigRes ;
	
	@RequestMapping({"/mjcardstype"})
	@Menu(type="platform", subtype="mjcardstype")
	public ModelAndView index(ModelMap map , HttpServletRequest request){
		
		return request(super.createAppsTempletResponse("/apps/business/platform/game/mjcardstype/index"));
	}
}

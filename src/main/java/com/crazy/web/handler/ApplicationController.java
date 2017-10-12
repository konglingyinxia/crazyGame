package com.crazy.web.handler;

import com.crazy.util.Menu;
import com.crazy.web.service.repository.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ApplicationController extends Handler {
	
	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/")
	@Menu(type = "apps" , subtype = "index" , access = false)
    public ModelAndView admin(HttpServletRequest request) {
        return request(super.createRequestPageTempletResponse("/apps/index"));
    }
}
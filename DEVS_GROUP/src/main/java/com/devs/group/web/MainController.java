package com.devs.group.web;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.devs.group.common.ssohandler.domain.entity.Member;
import com.devs.group.service.SideBarService;

@Controller
@RequestMapping("/")
public class MainController {

	private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

	// 프로그램 초기 진입
	@GetMapping("")
	public String index(HttpSession session) {
		return "redirect:/group/";
	}

}

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

	@Autowired
	private SideBarService sideBarService;

	// 프로그램 초기 진입
	@GetMapping("")
	public String index(HttpSession session, Integer channelcode) {

		if (channelcode == null) {
			session.removeAttribute("channel");
			return "redirect:/group/";
		} else {
			session.setAttribute("channel", sideBarService.selectChannel(channelcode));
			session.setAttribute("follow", sideBarService.selectGroupFollow(channelcode,
					((Member) session.getAttribute("user")).getMembercode()));
			return "redirect:/group/channel";
		}
	}

}

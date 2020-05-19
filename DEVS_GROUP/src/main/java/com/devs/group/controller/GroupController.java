package com.devs.group.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.devs.group.ssohandler.domain.entity.Member;
import com.devs.group.ssohandler.service.MemberService;

@RestController
@RequestMapping("/group/*")
public class GroupController {

	private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

	@Autowired
	private MemberService memberService;

	@Value("${server.port}")
	private int SERVER_PORT;

	@Value("${clientDomain}")
	private String CLIENT_DOMAIN;

	@Value("${clientSocketProtocol}")
	private String CLIENT_SOCKET_PROTOCOL;

	@Value("${clientProtocol}")
	private String CLIENT_PROTOCOL;

	// group.jsp 로 이동
	@GetMapping(value = "")
	public ModelAndView groupMainPage(HttpSession session, ModelMap map) {

		logger.info("GROUP PAGE");

		Member sessionMember = (Member) session.getAttribute("user");
		System.out.println("\n## user in session : " + sessionMember);

		if (sessionMember == null) {
			System.out.println("------------session이 null이잖아요~~~~?");
			return new ModelAndView("redirect:/");
		}
		
		Member dbMember = memberService.getUser(sessionMember.getMemberid());

		if (dbMember.getTokenId() == null) {

			System.out.println("------------session은 null이 아닌데 token id가 null이라서 세션에셔 user 까잖아요~~~?");
			session.removeAttribute("user");
			return new ModelAndView("redirect:/");

		} else {

			System.out.println("------------세션도 있고 token도 null이 아니에요~~~~~~");
			map.put("user", dbMember);

			// 프로필 정보 session에 셋팅
			session.setAttribute("profile", memberService.getMemberProfile(dbMember.getMembercode()));

			// properties를 session에 셋팅하여 jsp 페이지에서 사용한다.
			session.setAttribute("SERVER_PORT", SERVER_PORT);
			session.setAttribute("CLIENT_DOMAIN", CLIENT_DOMAIN);
			session.setAttribute("CLIENT_SOCKET_PROTOCOL", CLIENT_SOCKET_PROTOCOL);
			session.setAttribute("CLIENT_PROTOCOL", CLIENT_PROTOCOL);
		}

		return new ModelAndView("group");
	}

	@GetMapping("webrtc")
	public ModelAndView webRtcMainPage() {

		logger.info("WEBRTC PAGE");

		return new ModelAndView("webrtc");
	}

}

package com.devs.group.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/rtc/*")
public class RtcController {

	private static final Logger logger = LoggerFactory.getLogger(RtcController.class);

	/*
	 * webrtc 페이지 접근
	 */
	@GetMapping("webrtc")
	public ModelAndView webRtcMainPage() {

		logger.info("WEBRTC PAGE");

		return new ModelAndView("webrtc");
	}

}

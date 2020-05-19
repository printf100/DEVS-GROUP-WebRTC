package com.devs.group.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/.well-known")
public class CertBotAuthController {

	@RequestMapping("/acme-challenge/nQcHG8MRBxC-mnc01KoSIYiDRTry6Shg3yk4OGQDrfU")
	public ModelAndView auth(Model model) {

		String returnValue = "nQcHG8MRBxC-mnc01KoSIYiDRTry6Shg3yk4OGQDrfU.uEZYqqEohY2tEIFf2m8RMbZC5qhyvcPMKMLm_rwyE9s";
		model.addAttribute("data", returnValue);

		System.out.println(">>>>>>> certbot request!!!! return String : " + returnValue);

		return new ModelAndView("certbotcheck");
	}
}
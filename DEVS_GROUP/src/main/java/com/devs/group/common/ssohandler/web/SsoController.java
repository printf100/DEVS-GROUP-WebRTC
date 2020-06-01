package com.devs.group.common.ssohandler.web;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.devs.group.common.ssohandler.domain.vo.Response;
import com.devs.group.common.ssohandler.domain.vo.TokenRequestResult;
import com.devs.group.common.ssohandler.service.OAuthService;

@Controller
@RequestMapping("/ssoclient")
public class SsoController {

	private static final Logger log = LoggerFactory.getLogger(SsoController.class);

	@Autowired
	private OAuthService oauthService;

	@Value("${server.port}")
	private int SERVER_PORT;

	@Value("${ssoDomain}")
	private String SSO_DOMAIN;

	@Value("${ssoServerPort}")
	private String SSO_SERVER_PORT;

	@Value("${clientDomain}")
	private String CLIENT_DOMAIN;

	@Value("${systemName}")
	private String SYSTEM_NAME;

	private String getOAuthClientId() {
		return SYSTEM_NAME + "_id";
	}

	private String getOAuthRedirectUri() {

		// 로컬 테스트용
		return "http://" + CLIENT_DOMAIN + ":" + SERVER_PORT + "/ssoclient/oauthCallback";

		// 서버 배포용
//	    return "https://" + CLIENT_DOMAIN + "/ssoclient/oauthCallback";
	}

	@RequestMapping(value = "/oauthCallback", method = RequestMethod.GET)
	public String oauthCallback(@RequestParam(name = "code") String code, @RequestParam(name = "state") String state,
			HttpServletRequest request, ModelMap map) {

		String oauthState = (String) request.getSession().getAttribute("oauthState");
		request.getSession().removeAttribute("oauthState");
		log.debug("\n## code, oauthState, state : " + code + "," + oauthState + "," + state);

		TokenRequestResult tokenRequestResult = null;

		if (oauthState == null || oauthState.equals(state) == false) {

			tokenRequestResult = new TokenRequestResult();
			tokenRequestResult.setError("not matched state");
		} else {

			tokenRequestResult = oauthService.requestAccessTokenToAuthServer(code, request);
		}

		if (tokenRequestResult.getError() == null) {

			return "redirect:/group/";
		} else {

			map.put("result", tokenRequestResult);
			return "/group/";
		}
	}

	@RequestMapping(value = "/sso", method = RequestMethod.GET)
	public String sso(HttpServletRequest request) {

		System.out.println("----------------------------------------/sso-----------------------------");

		String state = UUID.randomUUID().toString();
		request.getSession().setAttribute("oauthState", state);

		StringBuilder builder = new StringBuilder();
		builder.append("redirect:");
		builder.append("http://" + SSO_DOMAIN + ":" + SSO_SERVER_PORT + "/oauth/authorize");
		builder.append("?response_type=code");
		builder.append("&client_id=");
		builder.append(getOAuthClientId());
		builder.append("&redirect_uri=");
		builder.append(getOAuthRedirectUri());
		builder.append("&scope=");
		builder.append("read");
		builder.append("&state=");
		builder.append(state);

		System.out.println(builder.toString());
		return builder.toString();
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout() {

		System.out.println("client가 나가겠대요~~~~ 제발 좀 나가라");
		return "redirect:http://" + SSO_DOMAIN + ":" + SSO_SERVER_PORT + "/userLogout?clientId=" + getOAuthClientId();
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	@ResponseBody
	public Response logoutFromAuthServer(@RequestParam(name = "tokenId") String tokenId,
			@RequestParam(name = "userName") String userName) {

		System.out.println("야호 server가 나가래요~~~~ 지금 나가잖아요~~~~?");

		Response response = oauthService.logout(tokenId, userName);

		log.debug("\n## logout secceeded {}", userName);
		System.out.println("\n## logout secceeded " + userName);

		return response;
	}

}

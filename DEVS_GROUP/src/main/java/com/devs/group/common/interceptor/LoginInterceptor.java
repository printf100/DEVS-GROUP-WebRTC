package com.devs.group.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoginInterceptor implements HandlerInterceptor {

	private Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

	// controller로 보내기 전에 처리하는 인터셉터
	// 반환이 false라면 controller로 요청을 안함
	// 매개변수 Object는 핸들러 정보를 의미한다. ( RequestMapping , DefaultServletHandler )
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println(
				"=========================================================== preHandle =========================================================");
		HttpSession session = request.getSession();

		if (session.getAttribute("user") != null //
				|| request.getRequestURI().contains("/ssoclient/sso")//
				|| request.getRequestURI().contains("/ssoclient/oauthCallback")//
				|| request.getRequestURI().contains("/ssoclient/logout")//
				|| request.getRequestURI().contains("/.well-known/acme-challenge/")// ssl (certbot) test 용
				//
//		            || request.getRequestURI().contains("/member/login") //
//		            || request.getRequestURI().contains("/member/ajaxemailcheck") //
//		            || request.getRequestURI().contains("/member/ajaxidcheck") //
//		            || request.getRequestURI().contains("/member/join") //
//		            || request.getRequestURI().contains("/member/oauth") //
				//
				|| request.getRequestURI().contains("/resources/") //
				|| request.getRequestURI().contains("/views/") //
		) {
			return true;
		}

		if (session.getAttribute("user") == null) {
			response.sendRedirect("/ssoclient/sso");
		}

		return false;
	}

	// controller의 handler가 끝나면 처리됨
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		System.out.println(
				"============================================================= postHandle =======================================================");
		// logger.info("[INTERCEPTOR] : postHandle");
	}

	// view까지 처리가 끝난 후에 처리됨
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		System.out.println(
				"=========================================================== afterCompletion ======================================================");
		// logger.info("[INTERCEPTOR] : afterCompletion");
	}

}

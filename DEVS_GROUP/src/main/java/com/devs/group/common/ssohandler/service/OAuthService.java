package com.devs.group.common.ssohandler.service;

import javax.servlet.http.HttpServletRequest;

import com.devs.group.common.ssohandler.domain.vo.Response;
import com.devs.group.common.ssohandler.domain.vo.TokenRequestResult;

public interface OAuthService {

	TokenRequestResult requestAccessTokenToAuthServer(String code, HttpServletRequest request);

	Response logout(String tokenId, String userName);

}
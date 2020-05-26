package com.devs.group.common.ssohandler.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.devs.group.common.ssohandler.domain.entity.Member;
import com.devs.group.common.ssohandler.domain.vo.Response;
import com.devs.group.common.ssohandler.domain.vo.TokenRequestResult;
import com.devs.group.common.ssohandler.domain.vo.UserInfoResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OAuthServiceImpl implements OAuthService {

	private static final Logger log = LoggerFactory.getLogger(OAuthServiceImpl.class);

	@Autowired
	private MemberService userService;

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

	private String authorizationRequestHeader;

	private String getOAuthClientId() {
		return SYSTEM_NAME + "_id";
	}

	private String getOAuthClientSecret() {
		return SYSTEM_NAME + "_secret";
	}

	private String getOAuthRedirectUri() {

		// 로컬 테스트용
		return "http://" + CLIENT_DOMAIN + ":" + SERVER_PORT + "/ssoclient/oauthCallback";

		// 서버 배포용
//		return "https://" + CLIENT_DOMAIN + "/ssoclient/oauthCallback";
	}

	@Override
	public TokenRequestResult requestAccessTokenToAuthServer(String code, HttpServletRequest request) {
		log.info("\n## requestAccessTokenToAuthServer()");

		TokenRequestResult tokenRequestResult = requestAccessTokenToAuthServer(code);
		log.info("\n## tokenResult : '{}'\n", tokenRequestResult);

		if (tokenRequestResult.getError() != null) {
			return tokenRequestResult;
		}

		UserInfoResponse userInfoResponse = requestUserInfoToAuthServer(tokenRequestResult.getAccessToken());

		if (userInfoResponse.getResult() == false) {
			tokenRequestResult.setError(userInfoResponse.getMessage());
			return tokenRequestResult;
		}

		Member member = userService.getUser(userInfoResponse.getUserName());
		request.getSession().setAttribute("user", member);

		String userName = userInfoResponse.getUserName();

		userService.updateTokenId(userName, extractTokenId(tokenRequestResult.getAccessToken()));

		return tokenRequestResult;
	}

	@Override
	public Response logout(String tokenId, String userName) {
		log.info("\n## logout()");
		System.out.println("\n## logout()");

		Response response = new Response();

		log.info("\n## logout {}", userName);
		System.out.println("\n## logout " + userName);

		Member member = userService.getUser(userName);

		if (member == null || member.getTokenId() == null) {
			return response;
		}

		String savedTokenId = member.getTokenId();
		log.info("\n## in logout savedTokenId, tokenId : '{}', '{}'", savedTokenId, tokenId);
		System.out.println("\n## in logout savedTokenId, tokenId : " + savedTokenId + ", " + tokenId);

		if (tokenId.equals(savedTokenId) == false) {
			return response;
		}

		userService.updateTokenId(userName, null);

		return response;
	}

	private TokenRequestResult requestAccessTokenToAuthServer(String code) {
		log.info("\n## requestAccessTokenToAuthServer() : code {}", code);

		String reqUrl = "http://" + SSO_DOMAIN + ":" + SSO_SERVER_PORT + "/oauth/token";

		String authorizationHeader = getAuthorizationRequestHeader();

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("grant_type", "authorization_code");
		paramMap.put("redirect_uri", getOAuthRedirectUri());
		paramMap.put("code", code);

		HttpPost post = buildHttpPost(reqUrl, paramMap, authorizationHeader);

		TokenRequestResult result = executePostAndParseResult(post, TokenRequestResult.class);

		return result;
	}

	private UserInfoResponse requestUserInfoToAuthServer(String token) {
		log.info("\n## requestUserInfoToAuthServer()");

		String reqUrl = "http://" + SSO_DOMAIN + ":" + SSO_SERVER_PORT + "/userInfo";
		String authorizationHeader = getAuthorizationRequestHeader();

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("token", token);
		paramMap.put("clientId", getOAuthClientId());

		HttpPost post = buildHttpPost(reqUrl, paramMap, authorizationHeader);

		UserInfoResponse result = executePostAndParseResult(post, UserInfoResponse.class);

		return result;
	}

	private <T> T executePostAndParseResult(HttpPost post, Class<T> clazz) {
		log.info("\n## executePostAndParseResult() : httpPost {}", post);

		T result = null;
		try {
			HttpClient client = HttpClientBuilder.create().build();

			HttpResponse response = client.execute(post);
			log.info("\n## executePostAndParseResult() : response {}", response);

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			log.info("\n## executePostAndParseResult() : read response {}", rd);

			StringBuffer resultBuffer = new StringBuffer();
			String line = "";

			while ((line = rd.readLine()) != null) {
				resultBuffer.append(line);
			}

			log.info("\n## response body : '{}'", resultBuffer.toString());

			ObjectMapper mapper = new ObjectMapper();
			result = mapper.readValue(resultBuffer.toString(), clazz);

		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		return result;
	}

	private HttpPost buildHttpPost(String reqUrl, Map<String, String> paramMap, String authorizationHeader) {
		log.info("\n## in buildHttpPost() reqUrl : {}", reqUrl);

		HttpPost post = new HttpPost(reqUrl);

		if (authorizationHeader != null) {
			post.addHeader("Authorization", authorizationHeader);
		}

		List<NameValuePair> urlParameters = new ArrayList<>();

		for (Map.Entry<String, String> entry : paramMap.entrySet()) {
			urlParameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		try {
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}

		log.info("\n## final buildHttpPost() reqUrl : {}", post);

		return post;
	}

	private String getAuthorizationRequestHeader() {
		//
		if (authorizationRequestHeader == null) {
			//
			setAuthroizationRequestHeader();
		}

		return authorizationRequestHeader;
	}

	private void setAuthroizationRequestHeader() {
		//
		Encoder encoder = Base64.getEncoder();

		try {
			String toEncodeString = String.format("%s:%s", getOAuthClientId(), getOAuthClientSecret());
			authorizationRequestHeader = "Basic " + encoder.encodeToString(toEncodeString.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}
	}

	private String extractTokenId(String value) {
		//
		if (value == null) {
			//
			return null;
		}

		try {
			//
			MessageDigest digest = MessageDigest.getInstance("MD5");

			byte[] bytes = digest.digest(value.getBytes("UTF-8"));
			return String.format("%032x", new BigInteger(1, bytes));
		} catch (NoSuchAlgorithmException e) {
			//
			throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
		} catch (UnsupportedEncodingException e) {
			//
			throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
		}
	}
}

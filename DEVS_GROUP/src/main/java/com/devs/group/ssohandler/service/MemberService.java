package com.devs.group.ssohandler.service;

import com.devs.group.ssohandler.domain.entity.Member;
import com.devs.group.ssohandler.domain.entity.MemberProfile;

public interface MemberService {

	Member getUser(String userName);

	boolean updateTokenId(String userName, String token);

	MemberProfile getMemberProfile(int membercode);
}
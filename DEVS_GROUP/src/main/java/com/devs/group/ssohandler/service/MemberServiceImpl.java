package com.devs.group.ssohandler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devs.group.ssohandler.domain.entity.Member;
import com.devs.group.ssohandler.domain.entity.MemberProfile;
import com.devs.group.ssohandler.domain.repository.MemberProfileRepository;
import com.devs.group.ssohandler.domain.repository.UserRepository;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private UserRepository memberRepository;

	@Autowired
	private MemberProfileRepository memberProfileRepository;

	@Override
	public Member getUser(String userName) {
		return memberRepository.findByMemberid(userName);
	}

	@Override
	public boolean updateTokenId(String userName, String tokenId) {
		Member member = memberRepository.findByMemberid(userName);
		member.setTokenId(tokenId);

		memberRepository.save(member);
		return true;
	}

	// 로그인 한 멤버 프로필 가져오기
	@Override
	public MemberProfile getMemberProfile(int membercode) {

		MemberProfile memberProfile = memberProfileRepository.findByMembercode(membercode);

		return memberProfile;
	}
}

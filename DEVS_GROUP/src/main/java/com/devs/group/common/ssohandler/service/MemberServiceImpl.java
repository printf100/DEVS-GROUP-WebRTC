package com.devs.group.common.ssohandler.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devs.group.common.ssohandler.domain.entity.Member;
import com.devs.group.common.ssohandler.domain.entity.MemberJoinProfile;
import com.devs.group.common.ssohandler.domain.entity.MemberProfile;
import com.devs.group.common.ssohandler.domain.repository.MemberJoinProfileRepository;
import com.devs.group.common.ssohandler.domain.repository.MemberProfileRepository;
import com.devs.group.common.ssohandler.domain.repository.UserRepository;
import com.devs.group.model.vo.MemberJoinProfileSimpleVo;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private UserRepository memberRepository;

	@Autowired
	private MemberProfileRepository memberProfileRepository;

	@Autowired
	private MemberJoinProfileRepository memberJoinProfileRepository;

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

	@Override
	public List<MemberJoinProfileSimpleVo> nameSearchAutoComplete(int my_member_code, String emain_id_name) {

		List<MemberJoinProfile> memberJoinProfileList = memberJoinProfileRepository
				.findAllByMemberemailOrMembernameOrMemberidAndMembercodeNot(emain_id_name, emain_id_name, emain_id_name,
						my_member_code);

		List<MemberJoinProfileSimpleVo> memberJoinProfileSimpleVoList = null;

		if (memberJoinProfileList != null) {
			memberJoinProfileSimpleVoList = new ArrayList<>();

			for (MemberJoinProfile memberJoinProfile : memberJoinProfileList) {
				MemberJoinProfileSimpleVo memberJoinProfileSimple = new MemberJoinProfileSimpleVo();

				memberJoinProfileSimple.setMembercode(memberJoinProfile.getMembercode());
				memberJoinProfileSimple.setMemberemail(memberJoinProfile.getMemberemail());
				memberJoinProfileSimple.setMemberid(memberJoinProfile.getMemberid());
				memberJoinProfileSimple
						.setMemberimgservername(memberJoinProfile.getMemberProfile().getMemberImgServerName());
				memberJoinProfileSimple.setMembername(memberJoinProfile.getMembername());
				memberJoinProfileSimple.setMemberphone(memberJoinProfile.getMemberphone());

				memberJoinProfileSimpleVoList.add(memberJoinProfileSimple);
			}
		}

		return memberJoinProfileSimpleVoList;
	}

	@Override
	public List<MemberJoinProfileSimpleVo> selectMemberList(List<Integer> codeList) {

		List<MemberJoinProfile> memberJoinProfileList = memberJoinProfileRepository.findAllByMembercodeIn(codeList);

		List<MemberJoinProfileSimpleVo> memberJoinProfileSimpleVoList = null;

		if (memberJoinProfileList != null) {
			memberJoinProfileSimpleVoList = new ArrayList<>();

			for (MemberJoinProfile memberJoinProfile : memberJoinProfileList) {
				MemberJoinProfileSimpleVo memberJoinProfileSimple = new MemberJoinProfileSimpleVo();

				memberJoinProfileSimple.setMembercode(memberJoinProfile.getMembercode());
				memberJoinProfileSimple.setMemberemail(memberJoinProfile.getMemberemail());
				memberJoinProfileSimple.setMemberid(memberJoinProfile.getMemberid());
				memberJoinProfileSimple
						.setMemberimgservername(memberJoinProfile.getMemberProfile().getMemberImgServerName());
				memberJoinProfileSimple.setMembername(memberJoinProfile.getMembername());
				memberJoinProfileSimple.setMemberphone(memberJoinProfile.getMemberphone());

				memberJoinProfileSimpleVoList.add(memberJoinProfileSimple);
			}
		}

		return memberJoinProfileSimpleVoList;
	}

	// 멤버 프로필 이미지 수정하기
	@Override
	public MemberProfile updateMemberProfileImage(int membercode, MemberProfile newMemberProfile) {
		
		MemberProfile oldMemberProfile = memberProfileRepository.findByMembercode(membercode);

		oldMemberProfile.setMemberImgOriginalName(newMemberProfile.getMemberImgOriginalName());
		oldMemberProfile.setMemberImgServerName(newMemberProfile.getMemberImgServerName());
		oldMemberProfile.setMemberImgPath(newMemberProfile.getMemberImgPath());
		
		MemberProfile changedMemberProfile = memberProfileRepository.save(oldMemberProfile);
		
		return changedMemberProfile;
	}

}

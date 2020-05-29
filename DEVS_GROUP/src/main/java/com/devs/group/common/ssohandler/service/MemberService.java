package com.devs.group.common.ssohandler.service;

import java.util.List;

import com.devs.group.common.ssohandler.domain.entity.Member;
import com.devs.group.common.ssohandler.domain.entity.MemberProfile;
import com.devs.group.model.vo.MemberJoinProfileSimpleVo;

public interface MemberService {

	Member getUser(String userName);

	boolean updateTokenId(String userName, String token);

	// 로그인 한 멤버 프로필 가져오기
	MemberProfile getMemberProfile(int membercode);

	// 멤버 프로필 이미지 수정하기
	MemberProfile updateMemberProfileImage(int membercode, MemberProfile newMemberProfile);

	List<MemberJoinProfileSimpleVo> nameSearchAutoComplete(int my_member_code, String id_name);

	List<MemberJoinProfileSimpleVo> selectMemberList(List<Integer> codeList);
}
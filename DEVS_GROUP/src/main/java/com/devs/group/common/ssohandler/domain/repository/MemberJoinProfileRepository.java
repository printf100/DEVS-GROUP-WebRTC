package com.devs.group.common.ssohandler.domain.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.devs.group.common.ssohandler.domain.entity.MemberJoinProfile;

public interface MemberJoinProfileRepository extends CrudRepository<MemberJoinProfile, String> {

	public List<MemberJoinProfile> findAllByMemberemailOrMembernameOrMemberidAndMembercodeNot(String memberemail,
			String membername, String memberid, int membercode);

	public List<MemberJoinProfile> findAllByMembercodeIn(List<Integer> membercodes);
}

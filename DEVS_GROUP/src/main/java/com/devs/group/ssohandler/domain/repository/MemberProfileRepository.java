package com.devs.group.ssohandler.domain.repository;

import org.springframework.data.repository.CrudRepository;

import com.devs.group.ssohandler.domain.entity.MemberProfile;

public interface MemberProfileRepository extends CrudRepository<MemberProfile, String>  {

	public MemberProfile findByMembercode(int membercode);
}

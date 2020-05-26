package com.devs.group.common.ssohandler.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.devs.group.common.ssohandler.domain.entity.Member;

@Repository
public interface UserRepository extends CrudRepository<Member, String> {

	public Member findByMemberid(String memberid);

}

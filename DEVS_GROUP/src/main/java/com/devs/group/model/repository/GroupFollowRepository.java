package com.devs.group.model.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.devs.group.model.entity.GroupFollow;

@Repository
public interface GroupFollowRepository extends CrudRepository<GroupFollow, String> {

	public List<GroupFollow> findAllByMembercode(int membercode);

	public GroupFollow findByChannelcodeAndMembercode(int channelcode, int membercode);

	public List<GroupFollow> findByChannelcodeAndFollowerrole(int channelcode, String followerrole);
}

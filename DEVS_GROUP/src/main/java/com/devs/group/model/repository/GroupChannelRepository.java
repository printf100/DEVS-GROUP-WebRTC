package com.devs.group.model.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.devs.group.model.entity.GroupChannel;

@Repository
public interface GroupChannelRepository extends CrudRepository<GroupChannel, Integer> {

	public List<GroupChannel> findByMembercodeAndChanneltype(int membercode, String channeltype);

	public List<GroupChannel> findAllByChannelcodeIn(List<Integer> channelcodeList);

	public GroupChannel findByChannelcode(int channelcode);

}

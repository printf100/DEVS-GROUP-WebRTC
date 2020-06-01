package com.devs.group.model.repository;

import org.springframework.data.repository.CrudRepository;

import com.devs.group.model.entity.GroupChannelBoard;

public interface GroupChannelBoardRepository extends CrudRepository<GroupChannelBoard, Integer> {

	public GroupChannelBoard findByChannelcode(int channelcode);
}

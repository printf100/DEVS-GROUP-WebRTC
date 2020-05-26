package com.devs.group.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devs.group.model.entity.GroupChannel;
import com.devs.group.model.repository.GroupChannelRepository;

@Service
public class GroupChannelServiceImpl implements GroupChannelService {

	@Autowired
	private GroupChannelRepository groupChannelRepository;

	@Override
	public GroupChannel createGroupChannel(GroupChannel groupChannel) {
		return groupChannelRepository.save(groupChannel);
	}

	@Override
	public GroupChannel updateGroupChannelDescription(int channelcode, GroupChannel newGroupChannel) {
		// spring data jpa 에서의 update
		// Repository find - setXxx - save
		GroupChannel oldGroupChannel = groupChannelRepository.findByChannelcode(channelcode);

		oldGroupChannel.setChannelname(newGroupChannel.getChannelname());
		oldGroupChannel.setChannelwebsite(newGroupChannel.getChannelwebsite());
		oldGroupChannel.setChannelintroduce(newGroupChannel.getChannelintroduce());

		GroupChannel changedGroupChannel = groupChannelRepository.save(oldGroupChannel);
		return changedGroupChannel;
	}

	@Override
	public GroupChannel updateGroupChannelImage(int channelcode, GroupChannel newGroupChannel) {
		// spring data jpa 에서의 update
		// Repository find - setXxx - save
		GroupChannel oldGroupChannel = groupChannelRepository.findByChannelcode(channelcode);

		oldGroupChannel.setChannelimgoriginalname(newGroupChannel.getChannelimgoriginalname());
		oldGroupChannel.setChannelimgservername(newGroupChannel.getChannelimgservername());
		oldGroupChannel.setChannelimgpath(newGroupChannel.getChannelimgpath());

		GroupChannel changedGroupChannel = groupChannelRepository.save(oldGroupChannel);
		return changedGroupChannel;
	}

}

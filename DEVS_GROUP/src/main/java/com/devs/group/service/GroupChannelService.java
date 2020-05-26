package com.devs.group.service;

import com.devs.group.model.entity.GroupChannel;

public interface GroupChannelService {

	public GroupChannel createGroupChannel(GroupChannel groupChannel);

	public GroupChannel updateGroupChannelDescription(int channelcode, GroupChannel newGroupChannel);

	public GroupChannel updateGroupChannelImage(int channelcode, GroupChannel newGroupChannel);

}

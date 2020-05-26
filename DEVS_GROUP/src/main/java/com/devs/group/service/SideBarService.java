package com.devs.group.service;

import java.util.List;

import com.devs.group.model.entity.GroupChannel;
import com.devs.group.model.entity.GroupFollow;
import com.devs.group.model.vo.MemberJoinProfileSimpleVo;

public interface SideBarService {

	public static final String GROUP_CHANNEL_TYPE = "G ";

	public List<GroupChannel> selectMyGroupChannelList(int membercode);

	public List<GroupChannel> selectFollowGroupChannelList(int membercode);

	public GroupChannel selectChannel(int channelcode);

	public GroupFollow selectGroupFollow(int channelcode, int membercode);

	public List<MemberJoinProfileSimpleVo> selectFollowerRoleEditor(int channelcode);

	public List<MemberJoinProfileSimpleVo> selectFollowerRoleReader(int channelcode);
}

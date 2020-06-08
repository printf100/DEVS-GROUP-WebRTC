package com.devs.group.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devs.group.common.ssohandler.domain.entity.MemberJoinProfile;
import com.devs.group.common.ssohandler.domain.repository.MemberJoinProfileRepository;
import com.devs.group.model.entity.GroupChannel;
import com.devs.group.model.entity.GroupFollow;
import com.devs.group.model.repository.GroupChannelRepository;
import com.devs.group.model.repository.GroupFollowRepository;
import com.devs.group.model.vo.MemberJoinProfileSimpleVo;

@Service
public class SideBarServiceImpl implements SideBarService {

	@Autowired
	private MemberJoinProfileRepository memberJoinProfileRepository;

	@Autowired
	private GroupFollowRepository groupFollowRepository;

	@Autowired
	private GroupChannelRepository groupChannelRepository;

	public static final String FOLLOW_EDITOR_ROLE = "E";
	public static final String FOLLOW_READER_ROLE = "R";

	@Override
	public List<GroupChannel> selectMyGroupChannelList(int membercode) {
		System.out.println(membercode + " 가 만든 그룹 채널 리스트");

		// 내가만든 그룹 채널 리스트 가져오기
		return groupChannelRepository.findByMembercodeAndChanneltype(membercode, GROUP_CHANNEL_TYPE);
	}

	@Override
	public List<GroupChannel> selectFollowGroupChannelList(int membercode) {
		System.out.println(membercode + " 가 팔로우한 그룹 채널 리스트");

		// 팔로우한 그룹 채널 리스트 가져오기
		List<GroupFollow> groupFollowList = groupFollowRepository.findAllByMembercode(membercode);

		if (groupFollowList != null) {
			List<Integer> channelcodeList = new ArrayList<>();

			for (GroupFollow groupFollow : groupFollowList) {
				channelcodeList.add(groupFollow.getChannelcode());
			}

			return groupChannelRepository.findAllByChannelcodeIn(channelcodeList);
		} else {
			return null;
		}
	}

	@Override
	public GroupChannel selectChannel(int channelcode) {
		return groupChannelRepository.findByChannelcode(channelcode);
	}

	@Override
	public GroupFollow selectGroupFollow(int channelcode, int membercode) {
		return groupFollowRepository.findByChannelcodeAndMembercode(channelcode, membercode);
	}

	@Override
	public List<MemberJoinProfileSimpleVo> selectFollowerRoleEditor(int channelcode) {
		List<GroupFollow> followerList = groupFollowRepository.findByChannelcodeAndFollowerrole(channelcode,
				FOLLOW_EDITOR_ROLE);

		List<Integer> membercodeList = new ArrayList<Integer>();
		for (GroupFollow follower : followerList) {
			membercodeList.add(follower.getMembercode());
		}

		List<MemberJoinProfile> memberJoinProfileList = memberJoinProfileRepository
				.findAllByMembercodeIn(membercodeList);

		List<MemberJoinProfileSimpleVo> memberJoinProfileSimpleVoList = null;

		if (memberJoinProfileList != null) {
			memberJoinProfileSimpleVoList = new ArrayList<>();

			for (MemberJoinProfile memberJoinProfile : memberJoinProfileList) {
				MemberJoinProfileSimpleVo memberJoinProfileSimple = new MemberJoinProfileSimpleVo();

				memberJoinProfileSimple.setMembercode(memberJoinProfile.getMembercode());
				memberJoinProfileSimple.setMemberemail(memberJoinProfile.getMemberemail());
				memberJoinProfileSimple.setMemberid(memberJoinProfile.getMemberid());
				memberJoinProfileSimple
						.setMemberimgservername(memberJoinProfile.getMemberProfile().getMemberImgServerName());
				memberJoinProfileSimple.setMembername(memberJoinProfile.getMembername());
				memberJoinProfileSimple.setMemberphone(memberJoinProfile.getMemberphone());

				memberJoinProfileSimpleVoList.add(memberJoinProfileSimple);
			}
		}

		return memberJoinProfileSimpleVoList;
	}

	@Override
	public List<MemberJoinProfileSimpleVo> selectFollowerRoleReader(int channelcode) {
		List<GroupFollow> followerList = groupFollowRepository.findByChannelcodeAndFollowerrole(channelcode,
				FOLLOW_READER_ROLE);

		List<Integer> membercodeList = new ArrayList<Integer>();
		for (GroupFollow follower : followerList) {
			membercodeList.add(follower.getMembercode());
		}

		List<MemberJoinProfile> memberJoinProfileList = memberJoinProfileRepository
				.findAllByMembercodeIn(membercodeList);

		List<MemberJoinProfileSimpleVo> memberJoinProfileSimpleVoList = null;

		if (memberJoinProfileList != null) {
			memberJoinProfileSimpleVoList = new ArrayList<>();

			for (MemberJoinProfile memberJoinProfile : memberJoinProfileList) {
				MemberJoinProfileSimpleVo memberJoinProfileSimple = new MemberJoinProfileSimpleVo();

				memberJoinProfileSimple.setMembercode(memberJoinProfile.getMembercode());
				memberJoinProfileSimple.setMemberemail(memberJoinProfile.getMemberemail());
				memberJoinProfileSimple.setMemberid(memberJoinProfile.getMemberid());
				memberJoinProfileSimple
						.setMemberimgservername(memberJoinProfile.getMemberProfile().getMemberImgServerName());
				memberJoinProfileSimple.setMembername(memberJoinProfile.getMembername());
				memberJoinProfileSimple.setMemberphone(memberJoinProfile.getMemberphone());

				memberJoinProfileSimpleVoList.add(memberJoinProfileSimple);
			}
		}

		return memberJoinProfileSimpleVoList;
	}

	@Override
	public void changeFollowerRole(int membercode, int channelcode) {
		GroupFollow groupFollow = groupFollowRepository.findByChannelcodeAndMembercode(channelcode, membercode);

		groupFollow.setFollowerrole(FOLLOW_EDITOR_ROLE);

		groupFollowRepository.save(groupFollow);
	}

	@Override
	public void deleteGroupFollow(int membercode, int channelcode) {
		groupFollowRepository.deleteByMembercodeAndChannelcode(membercode, channelcode);
	}

}

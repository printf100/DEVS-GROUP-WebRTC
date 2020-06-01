package com.devs.group.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devs.group.model.dao.MongoChatDao;
import com.devs.group.model.vo.ChatVo;
import com.devs.group.model.vo.RtcVo;

@Service
public class MongoChatServiceImpl implements MongoChatService {

	@Autowired
	private MongoChatDao dao;

	/*
	 * Chatting
	 */

	@Override
	public ChatVo insertChatRoom(ChatVo newRoom) {
		return dao.insertChatRoom(newRoom);
	}

	@Override
	public List<ChatVo> findGroupChanelChatRoomList(int channelcode) {
		return dao.findGroupChanelChatRoomList(channelcode);
	}

	@Override
	public ChatVo findRecentChat(int room_code) {
		return dao.findRecentChat(room_code);
	}

	@Override
	public ChatVo insertChat(ChatVo newChat) {
		return dao.insertChat(newChat);
	}

	@Override
	public List<ChatVo> selectChatList(int room_code, int startNo) {
		return dao.selectChatList(room_code, startNo);
	}

	@Override
	public ChatVo selectOneChatRoom(int room_code) {
		return dao.selectOneChatRoom(room_code);
	}

	@Override
	public void removeUnreadMemberCodeList(int room_code, int member_code) {
		dao.removeUnreadMemberCodeList(room_code, member_code);
	}

	/*
	 * WebRTC
	 */

	@Override
	public RtcVo insertRtcRoom(RtcVo newRtcRoom) {
		return dao.insertRtcRoom(newRtcRoom);
	}

	@Override
	public List<RtcVo> selectRtcList(int channel_code) {
		return dao.selectRtcList(channel_code);
	}

	@Override
	public int deleteRtcRoom(int room_code) {
		return dao.deleteRtcRoom(room_code);
	}

	@Override
	public RtcVo selectOneRtcRoom(int room_code) {
		return dao.selectOneRtcRoom(room_code);
	}

}
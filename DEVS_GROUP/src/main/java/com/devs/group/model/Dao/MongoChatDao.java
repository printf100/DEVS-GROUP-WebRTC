package com.devs.group.model.dao;

import java.util.List;

import com.devs.group.model.vo.ChatVo;
import com.devs.group.model.vo.RtcVo;

public interface MongoChatDao {

	/*
	 * Chatting
	 */

	public ChatVo insertChatRoom(ChatVo newRoom);

	public List<ChatVo> findGroupChanelChatRoomList(int channelcode);

	public ChatVo findRecentChat(int room_code);

	public ChatVo insertChat(ChatVo newChat);

	public List<ChatVo> selectChatList(int room_code);

	public void removeUnreadMemberCodeList(int room_code, int member_code);

	/*
	 * WebRTC
	 */

	public RtcVo insertRtcRoom(RtcVo newRtcRoom);

	public List<RtcVo> selectRtcList(int channel_code);
	
	public RtcVo selectOneRtcRoom(int room_code);
	
	public int deleteRtcRoom(int room_code);
}

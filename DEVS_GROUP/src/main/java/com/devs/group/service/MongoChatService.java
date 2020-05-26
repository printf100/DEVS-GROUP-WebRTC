package com.devs.group.service;

import java.util.List;

import com.devs.group.model.vo.ChatVo;

public interface MongoChatService {

	public ChatVo insertChatRoom(ChatVo newRoom);

	public List<ChatVo> findGroupChanelChatRoomList(int channelcode);

	public ChatVo findRecentChat(int room_code);

	public ChatVo insertChat(ChatVo newChat);

	public List<ChatVo> selectChatList(int room_code);

	public void removeUnreadMemberCodeList(int room_code, int member_code);
}
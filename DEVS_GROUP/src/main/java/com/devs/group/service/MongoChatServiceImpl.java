package com.devs.group.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devs.group.model.Dao.MongoChatDao;
import com.devs.group.model.vo.ChatVo;

@Service
public class MongoChatServiceImpl implements MongoChatService {

	@Autowired
	private MongoChatDao dao;

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
	public List<ChatVo> selectChatList(int room_code) {
		return dao.selectChatList(room_code);
	}

	@Override
	public void removeUnreadMemberCodeList(int room_code, int member_code) {
		dao.removeUnreadMemberCodeList(room_code, member_code);
	}

}

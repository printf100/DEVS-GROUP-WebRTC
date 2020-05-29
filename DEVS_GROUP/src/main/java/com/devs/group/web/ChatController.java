package com.devs.group.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.devs.group.common.ssohandler.service.MemberService;
import com.devs.group.model.entity.GroupChannel;
import com.devs.group.model.vo.ChatVo;
import com.devs.group.service.MongoChatService;

@RestController
@RequestMapping("/chat/*")
public class ChatController {

	private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

	@Autowired
	private MemberService memberService;

	@Autowired
	private MongoChatService mongoChatService;

	// 채팅방 리스트 가져오기
	@PostMapping(value = "findGroupChannelChatRoomList")
	public List<ChatVo> findMyChatRoomList(@RequestBody Map<String, Integer> map) {
		logger.info("chat/findGroupChannelChatRoomList.POST");

		System.out.println(map.get("channelcode"));

		List<ChatVo> list = mongoChatService.findGroupChanelChatRoomList(map.get("channelcode"));
		for (ChatVo d : list) {
			System.out.println(d);
		}
		return list;
	}

	// 채팅방 입장
	@GetMapping(value = "chatroom")
	public ModelAndView enterChatroom(Model model, int room_code) {
		logger.info("chatroom");
		model.addAttribute("room_code", room_code);
		return new ModelAndView("chatroom");
	}

	// 채팅방 만들기
	@PostMapping(value = "makeChatRoom")
	public Map<String, Object> makeChatRoom(HttpSession session, @RequestBody List<Map<String, Object>> memberList, @RequestBody String room_name) {
		logger.info("chat/makeChatRoom.POST");

		ChatVo newRoom = new ChatVo();

		newRoom.setChannel_code(((GroupChannel) session.getAttribute("channel")).getChannelcode());
		newRoom.setRoom_name(room_name);

		List<Integer> codeList = new ArrayList<>();
		for (Map<String, Object> member : memberList) {
			codeList.add((Integer) member.get("membercode"));
		}
		newRoom.setMember_list(memberService.selectMemberList(codeList));

		ChatVo insertedChatRoom = mongoChatService.insertChatRoom(newRoom);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("insertedChatRoom", insertedChatRoom);

		return resultMap;
	}

	// 채팅리스트 가져오기
	@PostMapping(value = "selectChatList")
	public List<ChatVo> selectChatList(@RequestBody Map<String, Integer> room_code) {
		logger.info("chat/selectChatList.POST");

		return mongoChatService.selectChatList(room_code.get("room_code"));
	}

}

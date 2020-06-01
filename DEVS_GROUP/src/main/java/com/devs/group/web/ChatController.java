package com.devs.group.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.devs.group.common.ssohandler.domain.entity.Member;
import com.devs.group.common.ssohandler.domain.entity.MemberProfile;
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
			System.out.println("findGroupChannelChatRoomList >> " + d);
		}
		return list;
	}

	// 채팅방 입장
	@GetMapping(value = "chatroom")
	public ModelAndView enterChatroom(Model model, int room_code, HttpSession session) {
		logger.info("chatroom");

		ChatVo enterChat = mongoChatService.selectOneChatRoom(room_code);
		System.out.println(enterChat.getRoom_code() + " 번 : " + enterChat.getRoom_name() + " 방 입장!!");
		session.setAttribute("chatRoomInfo", enterChat);
		return new ModelAndView("chatroom");
	}

	// 채팅방 만들기
	@PostMapping(value = "makeChatRoom")
	public Map<String, Object> makeChatRoom(HttpSession session, @RequestBody List<Map<String, Object>> list) {
		logger.info("chat/makeChatRoom.POST");

		List<Integer> codeList = new ArrayList<>();
		String room_name = "chattingRoom";
		for (Map<String, Object> map : list) {
			if (map.get("membercode") != null) {
				codeList.add((Integer) map.get("membercode"));
			} else {
				room_name = (String) map.get("room_name");
			}

		}
		System.out.println(codeList);
		System.out.println(room_name);

		ChatVo newRoom = new ChatVo();

		String member_id = ((Member) session.getAttribute("user")).getMemberid(); // 방을 생성한 멤버 아이디

		newRoom.setChannel_code(((GroupChannel) session.getAttribute("channel")).getChannelcode());
		newRoom.setRoom_name(room_name);
		newRoom.setMessage(member_id + "가 방을 생성했습니다.");
		newRoom.setMember_list(memberService.selectMemberList(codeList));

		ChatVo insertedChatRoom = mongoChatService.insertChatRoom(newRoom);

		System.out.println("방을 만들엇따아아아아ㅏ아아아아ㅏ아앙");
		System.out.println(insertedChatRoom);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("insertedChatRoom", insertedChatRoom);

		return resultMap;
	}

	// 채팅리스트 가져오기
	@PostMapping(value = "selectChatList")
	public List<ChatVo> selectChatList(@RequestBody Map<String, Integer> map) {
		logger.info("chat/selectChatList.POST");

		int room_code = map.get("room_code");
		int startNo = map.get("startNo");

		return mongoChatService.selectChatList(room_code, startNo);
	}

	// 사진 업로드
	@PostMapping(value = "sendImage")
	private Map<String, Object> updateProfileImage(@RequestParam("write_image") MultipartFile multi,
			HttpServletRequest request, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();

		String filePath = "/resources/images/chatimgupload/";

		String FILE_PATH = session.getServletContext().getRealPath(filePath);
		System.out.println("절대경로 : " + FILE_PATH);

		File file;
		if (!(file = new File(FILE_PATH)).isDirectory()) {
			file.mkdirs();
		}

		String uuid = UUID.randomUUID().toString(); // 파일 랜덤번호 생성

		String IMG_SERVER_NAME = null;
		String IMG_ORIGINAL_NAME = null;

		IMG_ORIGINAL_NAME = multi.getOriginalFilename();
		System.out.println("original : " + IMG_ORIGINAL_NAME);

		if (IMG_ORIGINAL_NAME != null) {

			IMG_SERVER_NAME = uuid + IMG_ORIGINAL_NAME;

			File targetFile = new File(FILE_PATH, IMG_SERVER_NAME);

			try {
				InputStream fileStream = multi.getInputStream();
				FileUtils.copyInputStreamToFile(fileStream, targetFile);
			} catch (IOException e) {
				FileUtils.deleteQuietly(targetFile);
				e.printStackTrace();
			}
		}

		// 태그를 그대로 리턴
		map.put("send_image", "<img src='/resources/images/chatimgupload/" + IMG_SERVER_NAME + "'>");

		return map;
	}

	// 채팅방에 추가하기
	@PostMapping(value = "findChannelChatRoomList")
	public List<ChatVo> updateChatRoomMember(@RequestBody Map<String, Integer> map) {
		logger.info("chat/selectChatList.POST");

		int channelcode = map.get("channelcode");

		List<ChatVo> chatRoomList = mongoChatService.findGroupChanelChatRoomList(channelcode);

		return chatRoomList;
	}

}
package com.devs.group.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.devs.group.model.entity.GroupChannel;
import com.devs.group.model.vo.RtcVo;
import com.devs.group.service.MongoChatService;

@RestController
@RequestMapping("/rtc/*")
public class RtcController {

	private static final Logger logger = LoggerFactory.getLogger(RtcController.class);

	@Autowired
	private MongoChatService mongoChatService;

	// 채팅방 리스트 가져오기
	@PostMapping(value = "findGroupChannelRtcRoomList")
	public List<RtcVo> findMyChatRoomList(@RequestBody Map<String, Integer> map) {
		logger.info("rtc/findGroupChannelRtcRoomList.POST");

		System.out.println(map.get("channelcode"));

		List<RtcVo> list = mongoChatService.selectRtcList(map.get("channelcode"));
		for (RtcVo d : list) {
			System.out.println(d);
		}

		return list;
	}

	// 채팅방 입장
	@GetMapping(value = "rtcroom")
	public ModelAndView enterChatroom(HttpSession session, int room_code) {
		logger.info("rtc/rtcroom.GET");

		RtcVo roomInfo = mongoChatService.selectOneRtcRoom(room_code);
		session.setAttribute("roomInfo", roomInfo);

		return new ModelAndView("webrtc");
	}

	// 채팅방 만들기
	@PostMapping(value = "makeRtcRoom")
	public Map<String, Object> makeChatRoom(HttpSession session, String room_name) {
		logger.info("rtc/makeRtcRoom.POST");

		RtcVo newRtcRoom = new RtcVo();

		newRtcRoom.setChannel_code(((GroupChannel) session.getAttribute("channel")).getChannelcode());
		newRtcRoom.setRoom_name(room_name);

		RtcVo insertedRtcRoom = mongoChatService.insertRtcRoom(newRtcRoom);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("insertedRtcRoom", insertedRtcRoom);

		return resultMap;
	}

}

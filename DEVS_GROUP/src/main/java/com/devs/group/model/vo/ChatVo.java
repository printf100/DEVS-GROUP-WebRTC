package com.devs.group.model.vo;

import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatVo {

	public static final String ROOM_CODE_SEQ_NAME = "room_code_seq";
	public static final String CHAT_CODE_SEQ_NAME = "chat_code_seq";

	@Id
	private String id; // mongoDB id
	
	private String chat_type; // 채팅 타입
	private int channel_code; // 채널번호
	private int room_code; // 방번호
	private String room_name; // 방 이름

	private List<MemberJoinProfileSimpleVo> member_list; // 참여인원
	private List<Integer> unread_member_code_list; // 참여인원

	private int chat_code; // 작성글 코드
	private int member_code; // 작성자 회원번호
	private String member_id;	// 작성자 아이디
	private String message; // 작성한 글
	private String imagetag; // 작성한 사진 <img> 태그

	private String message_date;

}

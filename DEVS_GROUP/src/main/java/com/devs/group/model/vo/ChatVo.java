package com.devs.group.model.vo;

import java.util.List;

import org.springframework.data.annotation.Id;

public class ChatVo {

	public static final String ROOM_CODE_SEQ_NAME = "room_code_seq";
	public static final String CHAT_CODE_SEQ_NAME = "chat_code_seq";

	@Id
	private String id; // mongoDB id
	private int channel_code; // 채널번호
	private int room_code; // 방번호
	private String room_name; // 방 이름

	private List<MemberJoinProfileSimpleVo> member_list; // 참여인원
	private List<Integer> unread_member_code_list; // 참여인원

	private int chat_code; // 작성글 코드
	private int member_code; // 작성자
	private String message; // 작성한 글

	private String message_date;

	public ChatVo() {
		super();
	}

	public ChatVo(String id, int channel_code, int room_code, String room_name,
			List<MemberJoinProfileSimpleVo> member_list, List<Integer> unread_member_code_list, int chat_code,
			int member_code, String message, String message_date) {
		super();
		this.id = id;
		this.channel_code = channel_code;
		this.room_code = room_code;
		this.room_name = room_name;
		this.member_list = member_list;
		this.unread_member_code_list = unread_member_code_list;
		this.chat_code = chat_code;
		this.member_code = member_code;
		this.message = message;
		this.message_date = message_date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getChannel_code() {
		return channel_code;
	}

	public void setChannel_code(int channel_code) {
		this.channel_code = channel_code;
	}

	public int getRoom_code() {
		return room_code;
	}

	public void setRoom_code(int room_code) {
		this.room_code = room_code;
	}

	public String getRoom_name() {
		return room_name;
	}

	public void setRoom_name(String room_name) {
		this.room_name = room_name;
	}

	public List<MemberJoinProfileSimpleVo> getMember_list() {
		return member_list;
	}

	public void setMember_list(List<MemberJoinProfileSimpleVo> member_list) {
		this.member_list = member_list;
	}

	public List<Integer> getUnread_member_code_list() {
		return unread_member_code_list;
	}

	public void setUnread_member_code_list(List<Integer> unread_member_code_list) {
		this.unread_member_code_list = unread_member_code_list;
	}

	public int getChat_code() {
		return chat_code;
	}

	public void setChat_code(int chat_code) {
		this.chat_code = chat_code;
	}

	public int getMember_code() {
		return member_code;
	}

	public void setMember_code(int member_code) {
		this.member_code = member_code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage_date() {
		return message_date;
	}

	public void setMessage_date(String message_date) {
		this.message_date = message_date;
	}

	public static String getRoomCodeSeqName() {
		return ROOM_CODE_SEQ_NAME;
	}

	public static String getChatCodeSeqName() {
		return CHAT_CODE_SEQ_NAME;
	}

	@Override
	public String toString() {
		return "ChatVo [id=" + id + ", channel_code=" + channel_code + ", room_code=" + room_code + ", room_name="
				+ room_name + ", member_list=" + member_list + ", unread_member_code_list=" + unread_member_code_list
				+ ", chat_code=" + chat_code + ", member_code=" + member_code + ", message=" + message
				+ ", message_date=" + message_date + "]";
	}

}

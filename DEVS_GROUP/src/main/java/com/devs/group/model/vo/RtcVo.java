package com.devs.group.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RtcVo {

	public static final String RTC_CODE_SEQ_NAME = "rtc_code_seq";

	private int channel_code; // 채널 번호
	private int room_code; // 방 번호
	private String room_name;	// 방 이름

}

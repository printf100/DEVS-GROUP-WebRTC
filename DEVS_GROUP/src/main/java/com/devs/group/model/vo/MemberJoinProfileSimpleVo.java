package com.devs.group.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class MemberJoinProfileSimpleVo {

	@NonNull
	private int membercode;

	private String memberemail;
	private String memberphone;
	private String membername;
	private String memberid;

	private String memberimgservername;

}

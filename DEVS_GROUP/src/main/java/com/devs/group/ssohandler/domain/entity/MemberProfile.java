package com.devs.group.ssohandler.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "MEMBER_PROFILE")
@Entity
public class MemberProfile {

	@Id
	@Column(name = "MEMBER_CODE")
	private int membercode;

	@Column(name = "MEMBER_IMG_ORIGINAL_NAME")
	private String memberImgOriginalName;

	@Column(name = "MEMBER_IMG_SERVER_NAME")
	private String memberImgServerName;

	@Column(name = "MEMBER_IMG_PATH")
	private String memberImgPath;

	@Column(name = "MEMBER_WEBSITE")
	private String memberWebsite;

	@Column(name = "MEMBER_INTRODUCE")
	private String memberIntroduce;

	@Column(name = "MEMBER_GENDER")
	private String memberGender;

}

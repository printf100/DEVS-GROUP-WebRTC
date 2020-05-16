package com.devs.group.ssohandler.domain.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "MEMBER")
@Entity
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_CODE_SEQ_GENERATOR")
	@Column(name = "MEMBER_CODE")
	@SequenceGenerator(name = "MEMBER_CODE_SEQ_GENERATOR", sequenceName = "MEMBER_CODE_SEQ", initialValue = 1, allocationSize = 1)
	private int membercode;

	@Column(name = "MEMBER_EMAIL")
	private String memberemail;

	@Column(name = "MEMBER_PHONE")
	private String memberphone;

	@Column(name = "MEMBER_NAME")
	private String membername;

	@Column(name = "MEMBER_ID")
	private String memberid;

	@Column(name = "MEMBER_PASSWORD")
	private String memberpassword;

	@Column(name = "SNS_TYPE")
	private String snstype;

	@Column(name = "SNS_ID")
	private String snsid;

	@Column(name = "TOKEN_ID")
	private String tokenId;

	@Builder
	public Member(String memberemail, String memberphone, String membername, String memberid, String memberpassword,
			String snstype, String snsid) {
		super();
		this.memberemail = memberemail;
		this.memberphone = memberphone;
		this.membername = membername;
		this.memberid = memberid;
		this.memberpassword = memberpassword;
		this.snstype = snstype;
		this.snsid = snsid;
	}

	public Member(String memberemail, String memberphone, String membername, String memberid, String memberpassword) {
		this.memberemail = memberemail;
		this.memberphone = memberphone;
		this.membername = membername;
		this.memberid = memberid;
		this.memberpassword = memberpassword;
	}

}

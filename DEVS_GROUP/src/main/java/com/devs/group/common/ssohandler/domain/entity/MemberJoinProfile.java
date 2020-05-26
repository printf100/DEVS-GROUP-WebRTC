package com.devs.group.common.ssohandler.domain.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "MEMBER")
@Entity
public class MemberJoinProfile {

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

	@OneToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "MEMBER_CODE", insertable = false)
	private MemberProfile memberProfile;

}

package com.devs.group.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CHANNEL")
@Entity
public class GroupChannel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHANNEL_CODE_SEQ_GENERATOR")
	@SequenceGenerator(name = "CHANNEL_CODE_SEQ_GENERATOR", sequenceName = "CHANNEL_CODE_SEQ", initialValue = 1, allocationSize = 1)
	@Column(name = "CHANNEL_CODE")
	private int channelcode;

	@Column(name = "MEMBER_CODE")
	private int membercode;

	@Column(name = "CHANNEL_TYPE", nullable = false, length = 2, columnDefinition = "CHAR")
	private String channeltype;

	@Column(name = "CHANNEL_NAME")
	private String channelname;

	@Column(name = "CHANNEL_IMG_ORIGINAL_NAME")
	private String channelimgoriginalname;

	@Column(name = "CHANNEL_IMG_SERVER_NAME")
	private String channelimgservername;

	@Column(name = "CHANNEL_IMG_PATH")
	private String channelimgpath;

	@Column(name = "CHANNEL_WEBSITE")
	private String channelwebsite;

	@Column(name = "CHANNEL_PAY")
	private String channelpay;

	@Column(name = "CHANNEL_INTRODUCE")
	private String channelintroduce;
}

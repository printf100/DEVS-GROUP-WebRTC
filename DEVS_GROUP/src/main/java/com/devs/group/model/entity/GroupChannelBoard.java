package com.devs.group.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BOARD")
@Entity
public class GroupChannelBoard {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_CODE_SEQ_GENERATOR")
	@SequenceGenerator(name = "BOARD_CODE_SEQ_GENERATOR", sequenceName = "BOARD_CODE_SEQ", allocationSize = 1)
	@Column(name = "BOARD_CODE")
	private int boardcode;

	@Column(name = "MEMBER_CODE")
	private int membercode;

	@Column(name = "CHANNEL_CODE")
	private int channelcode;

	@Column(name = "BOARD_CONTENT")
	private String boardcontent;

	@Column(name = "BOARD_REGDATE")
	@CreatedDate
	private Date boardregdate;
}

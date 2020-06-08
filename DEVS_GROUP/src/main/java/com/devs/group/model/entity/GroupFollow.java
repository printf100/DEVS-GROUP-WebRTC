package com.devs.group.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "GROUP_FOLLOW")
@IdClass(GroupFollowId.class)
@Entity
public class GroupFollow {

	@Id
	@Column(name = "MEMBER_CODE")
	private int membercode;

	@Id
	@Column(name = "CHANNEL_CODE")
	private int channelcode;

	@Column(name = "FOLLOWER_ROLE")
	private String followerrole;

}

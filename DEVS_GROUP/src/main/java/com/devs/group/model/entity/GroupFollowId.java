package com.devs.group.model.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class GroupFollowId implements Serializable {

	private int membercode;

	private int channelcode;

}

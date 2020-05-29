package com.devs.group.model.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignalMessage {

	private String type;
	private String toId;
	private String fromId;
	private List<String> loginIds;
	private Object data;
}
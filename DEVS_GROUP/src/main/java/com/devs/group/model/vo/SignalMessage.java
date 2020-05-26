package com.devs.group.model.vo;

import java.util.List;

public class SignalMessage {

	private String type;
	private String dest;
	private String enterId;
	private int connections;
	private List<String> loginIds;
	private Object data;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public String getEnterId() {
		return enterId;
	}

	public void setEnterId(String enterId) {
		this.enterId = enterId;
	}

	public int getConnections() {
		return connections;
	}

	public void setConnections(int connections) {
		this.connections = connections;
	}

	public List<String> getLoginIds() {
		return loginIds;
	}

	public void setLoginIds(List<String> loginIds) {
		this.loginIds = loginIds;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
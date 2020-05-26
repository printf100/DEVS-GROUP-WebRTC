package com.devs.group.model.vo;

import org.springframework.data.annotation.Id;

public class MongoSequence {

	@Id
	private String id;
	private long seq;

	public MongoSequence() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MongoSequence(String id, long seq) {
		super();
		this.id = id;
		this.seq = seq;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getSeq() {
		return seq;
	}

	public void setSeq(long seq) {
		this.seq = seq;
	}

	@Override
	public String toString() {
		return "ChatRoomSeq [id=" + id + ", seq=" + seq + "]";
	}

}

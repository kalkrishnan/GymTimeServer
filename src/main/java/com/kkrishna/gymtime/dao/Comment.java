package com.kkrishna.gymtime.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.Builder;
import lombok.experimental.Tolerate;

@Entity
@Builder
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long Id;
	private String comment;
	private String userId;
	private String gymId;
	private String time;
	@Column(length = 1000000)
	@Lob
	private String commentImage;

	@Tolerate
	Comment() {

	}

	public String getComment() {
		return comment;
	}

	public String getTime() {
		return time;
	}

	public String getUserId() {
		return userId;
	}

	public String getCommentImage() {
		return commentImage;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	public String getGymId() {
		return gymId;
	}

}

package com.kkrishna.gymtime.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import lombok.Builder;
import lombok.experimental.Tolerate;

@Entity
@Builder
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long Id;
	@Expose
	private String comment;
	@Expose
	private String userId;
	@Expose
	private String gymId;
	@Expose
	private String time;
	@Column(length = 1000000)
	@Lob
	@Expose
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
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
	}

	public String getGymId() {
		return gymId;
	}

}

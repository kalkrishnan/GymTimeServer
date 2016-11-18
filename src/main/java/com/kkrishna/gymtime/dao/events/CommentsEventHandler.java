package com.kkrishna.gymtime.dao.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.kkrishna.gymtime.dao.Comment;

@RepositoryEventHandler(Comment.class)
@RestController
public class CommentsEventHandler {

	@Autowired
	private SimpMessagingTemplate gymTimePushClient;

	@HandleAfterCreate(Comment.class)
	public void handleCommentSave(Comment comment) {
		this.gymTimePushClient.convertAndSend("/topic/comments/"+comment.getGymId(), comment.toString());
	}
}

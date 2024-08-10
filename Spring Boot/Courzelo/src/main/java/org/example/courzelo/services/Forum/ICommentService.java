package org.example.courzelo.services.Forum;

import org.example.courzelo.models.Forum.Comment;
import org.example.courzelo.models.User;


import java.util.List;

public interface ICommentService {
    List<Comment> getCommentByPost(String postid);

    List<Comment> getCommentByUser(User user);
    void deleteComment(String id);

    Comment saveComment(Comment comment);

    Comment updateComment(Comment comment);
    Comment updateComment1(String id, String text);
    Comment getCommentById(String id);
}

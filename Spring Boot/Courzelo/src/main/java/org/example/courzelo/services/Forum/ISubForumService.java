package org.example.courzelo.services.Forum;

import org.example.courzelo.models.Forum.Post;
import org.example.courzelo.models.Forum.SubForum;
import org.example.courzelo.models.User;

import java.util.List;
import java.util.Optional;

public interface ISubForumService {
    List<SubForum> getSubForums();
    List<SubForum> getUserSubs(User user);

    void deleteSubForum(String id);

    SubForum getSubForum(String id);

    SubForum saveSub(SubForum SubForum);

    SubForum updateSubForum(SubForum SubForum);
    SubForum updateSubForum1(String id, String name, String description);
}

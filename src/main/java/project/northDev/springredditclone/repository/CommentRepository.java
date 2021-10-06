package project.northDev.springredditclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.northDev.springredditclone.model.Comment;
import project.northDev.springredditclone.model.Post;
import project.northDev.springredditclone.model.User;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


    List<Comment> findAllByPost(Post post);
    List<Comment> findAllByUser(User user);

}

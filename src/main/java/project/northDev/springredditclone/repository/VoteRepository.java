package project.northDev.springredditclone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.northDev.springredditclone.model.Post;
import project.northDev.springredditclone.model.User;
import project.northDev.springredditclone.model.Vote;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByPostAndUser(Post post, User user
    );
}

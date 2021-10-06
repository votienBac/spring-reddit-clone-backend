package project.northDev.springredditclone.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import project.northDev.springredditclone.dto.PostRequest;
import project.northDev.springredditclone.dto.PostResponse;
import project.northDev.springredditclone.model.*;
import project.northDev.springredditclone.repository.CommentRepository;
import project.northDev.springredditclone.repository.VoteRepository;
import project.northDev.springredditclone.service.AuthService;

import java.util.Optional;

import static project.northDev.springredditclone.model.VoteType.DOWNVOTE;
import static project.northDev.springredditclone.model.VoteType.UPVOTE;

@Mapper(componentModel = "spring", implementationPackage = "project.northDev.springredditclone")

public abstract class PostMapper {



    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private CommentRepository commentRepository;


    @Mapping(target = "createDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "user", source = "user")
    public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    @Mapping(target = "upVote", expression = "java(isUpVoted(post))")
    @Mapping(target = "downVote", expression = "java(isDownVoted(post))")

    public abstract PostResponse mapToDto(Post post);

     public Integer commentCount(Post post){
        return commentRepository.findAllByPost(post).size();
    }

     public String getDuration(@NotNull Post post){
        return TimeAgo.using(post.getCreateDate().toEpochMilli());

    }
    public boolean isUpVoted(Post post){
         return checkVoted(post, UPVOTE);
    }
    public boolean isDownVoted(Post post){
        return checkVoted(post, DOWNVOTE);
    }

    private boolean checkVoted(Post post, VoteType voteType) {
         if(authService.isLoggedIn()){
             Optional<Vote> voteForPostByUser= voteRepository.findByPostAndUser(post, authService.getCurrentUser());
             return voteForPostByUser.filter(vote->vote.getVoteType().equals(voteType)).isPresent();
         }
         return false;
    }

}

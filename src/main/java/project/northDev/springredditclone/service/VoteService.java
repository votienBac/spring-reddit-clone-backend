package project.northDev.springredditclone.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.northDev.springredditclone.dto.VoteDto;
import project.northDev.springredditclone.exceptions.PostNotFoundException;
import project.northDev.springredditclone.exceptions.SpringRedditException;
import project.northDev.springredditclone.model.Post;
import project.northDev.springredditclone.model.Vote;
import project.northDev.springredditclone.model.VoteType;
import project.northDev.springredditclone.repository.PostRepository;
import project.northDev.springredditclone.repository.VoteRepository;

import java.util.Optional;

import static project.northDev.springredditclone.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor

public class VoteService {

    private final PostRepository postRepository;
    private final VoteRepository voteRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(()-> new PostNotFoundException("Post not found!"));
        Optional<Vote> vote = voteRepository.findByPostAndUser(post, authService.getCurrentUser());
        if(vote.isPresent() && vote.get().getVoteType().equals(voteDto.getVoteType())){
            throw new SpringRedditException("You have already "+voteDto.getVoteType()+" for this post");
        }
        if(UPVOTE.equals(voteDto.getVoteType())){
            post.setVoteCount(post.getVoteCount()+1);
        }else post.setVoteCount(post.getVoteCount()-1);

        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);

    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder().voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}

package project.northDev.springredditclone.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.northDev.springredditclone.dto.CommentDto;
import project.northDev.springredditclone.exceptions.PostNotFoundException;
import project.northDev.springredditclone.mapper.CommentMapper;
import project.northDev.springredditclone.model.Comment;
import project.northDev.springredditclone.model.Post;
import project.northDev.springredditclone.model.User;
import project.northDev.springredditclone.repository.CommentRepository;
import project.northDev.springredditclone.repository.PostRepository;
import project.northDev.springredditclone.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Transactional
    public void save(CommentDto commentDto) {

        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(()-> new PostNotFoundException("The post with postID "+commentDto.getPostId()+" not found!"));
        User user = userRepository.findByUsername(commentDto.getUserName())
                .orElseThrow(()-> new UsernameNotFoundException("User with name "+commentDto.getUserName()+" not found!!"));
        Comment comment = commentMapper.map(commentDto, post, user);
        commentRepository.save(comment);


    }
    @Transactional(readOnly = true)
    public List<CommentDto> getAllForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new PostNotFoundException("The post with postID "+postId+" not found!"));
        return commentRepository.findAllByPost(post).stream().map(commentMapper::mapToDto).collect(toList());
    }
    @Transactional(readOnly = true)
    public List<CommentDto> getAllWithUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(()-> new UsernameNotFoundException("User with name "+userName+" not found!!"));
        return commentRepository.findAllByUser(user).stream().map(commentMapper::mapToDto).collect(toList());
    }
}

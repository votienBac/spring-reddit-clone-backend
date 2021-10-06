package project.northDev.springredditclone.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.northDev.springredditclone.dto.PostRequest;
import project.northDev.springredditclone.dto.PostResponse;
import project.northDev.springredditclone.exceptions.PostNotFoundException;
import project.northDev.springredditclone.exceptions.SpringRedditException;
import project.northDev.springredditclone.exceptions.SubredditNotFoundException;
import project.northDev.springredditclone.mapper.PostMapper;
import project.northDev.springredditclone.model.Post;
import project.northDev.springredditclone.model.Subreddit;
import project.northDev.springredditclone.model.User;
import project.northDev.springredditclone.repository.PostRepository;
import project.northDev.springredditclone.repository.SubredditRepository;
import project.northDev.springredditclone.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final PostMapper postMapper;
    private final AuthService authService;
    private final UserRepository userRepository;
    public void save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(()->new SubredditNotFoundException("Subreddit with name "+postRequest.getSubredditName()+" not found!"));
        Post post = postMapper.map(postRequest, subreddit, authService.getCurrentUser());
        post.setUser(authService.getCurrentUser());
        post.setSubreddit(subreddit);
        postRepository.save(post);


    }
    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new PostNotFoundException(""));

        return postMapper.mapToDto(post);
    }
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(()->new SubredditNotFoundException(id.toString()));


        return  postRepository.findAllBySubreddit(subreddit).stream().map(postMapper::mapToDto).collect(toList());
    }
    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String name) {
        User user = userRepository.findByUsername(name)
                .orElseThrow(()->  new UsernameNotFoundException(name));

        return postRepository.findByUser(user).stream().map(postMapper::mapToDto).collect(toList());

    }
}

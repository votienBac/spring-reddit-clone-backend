package project.northDev.springredditclone.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.northDev.springredditclone.dto.CommentDto;
import project.northDev.springredditclone.service.CommentService;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentDto commentDto){
        commentService.save(commentDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("by-postId/{postId}")
    public ResponseEntity<List<CommentDto>> getAllCommentForPost(@PathVariable Long postId){
        return status(HttpStatus.OK).body(commentService.getAllForPost(postId));
    }
    @GetMapping("by-user/{userName}")
    public ResponseEntity<List<CommentDto>> getAllCommentWithUser(@PathVariable String userName){
        return status(HttpStatus.OK).body(commentService.getAllWithUser
                (userName));
    }


}

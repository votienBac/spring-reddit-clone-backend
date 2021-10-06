package project.northDev.springredditclone.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.northDev.springredditclone.dto.Subredditdto;
import project.northDev.springredditclone.model.Subreddit;
import project.northDev.springredditclone.service.SubredditService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/subreddit")
@AllArgsConstructor
@Slf4j
public class SubredditController {

    private final SubredditService subredditService;

    @PostMapping
    public ResponseEntity<Subredditdto> createSubreddit(@RequestBody Subredditdto subredditdto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subredditService.save(subredditdto));
    }

    @GetMapping
    public ResponseEntity<List<Subredditdto>> getAllSubreddits(){
        return ResponseEntity.status(HttpStatus.OK).body(subredditService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subredditdto> getSubreddit(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(subredditService.getSubreddit(id));
    }

}

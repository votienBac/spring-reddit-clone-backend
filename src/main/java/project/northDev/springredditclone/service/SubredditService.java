package project.northDev.springredditclone.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.northDev.springredditclone.dto.Subredditdto;
import project.northDev.springredditclone.exceptions.SpringRedditException;
import project.northDev.springredditclone.mapper.SubredditMapper;
import project.northDev.springredditclone.model.Subreddit;
import project.northDev.springredditclone.repository.SubredditRepository;

import java.util.List;

import static java.util.stream.Collectors.*;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {
    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;
    @Transactional
    public Subredditdto save(Subredditdto subredditdto){
        Subreddit subreddit = subredditMapper.mapDtoToSubreddit(subredditdto);
        subredditdto.setId(subreddit.getId());
        subreddit.setName(subredditdto.getSubredditName());
        subredditRepository.save(subreddit);
        return subredditdto;
    }


    @Transactional(readOnly = true)
    public List<Subredditdto> getAll() {
       return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(toList());
    }

    public Subredditdto getSubreddit(Long id){
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(()-> new SpringRedditException("No subreddit found with id "+id));
        return subredditMapper.mapSubredditToDto(subreddit);
    }


}

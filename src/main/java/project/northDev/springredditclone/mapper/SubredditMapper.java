package project.northDev.springredditclone.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.northDev.springredditclone.dto.Subredditdto;
import project.northDev.springredditclone.model.Post;
import project.northDev.springredditclone.model.Subreddit;

import java.util.List;

@Mapper(componentModel = "spring", implementationPackage = "project.northDev.springredditclone")
public interface SubredditMapper {
    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    @Mapping(target = "subredditName", source = "subreddit.name")
    Subredditdto mapSubredditToDto(Subreddit subreddit);



    default Integer mapPosts(List<Post> numberOfPosts){
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Subreddit mapDtoToSubreddit(Subredditdto subredditdto);
}

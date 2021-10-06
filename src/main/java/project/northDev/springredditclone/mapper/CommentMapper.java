package project.northDev.springredditclone.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.northDev.springredditclone.dto.CommentDto;
import project.northDev.springredditclone.model.Comment;
import project.northDev.springredditclone.model.Post;
import project.northDev.springredditclone.model.User;

@Mapper(componentModel = "spring", implementationPackage = "project.northDev.springredditclone")
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTime", expression = "java(java.time.Instant.now())")
    @Mapping(target = "text", source = "commentDto.text")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    Comment map(CommentDto commentDto, Post post, User user);

    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
    CommentDto mapToDto(Comment comment);




}

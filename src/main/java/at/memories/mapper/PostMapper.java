package at.memories.mapper;

import at.memories.dto.PostDto;
import at.memories.model.Post;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "jakarta")
public interface PostMapper {
    List<PostDto> toResource(List<Post> post);
    Post toResource(PostDto post);
}

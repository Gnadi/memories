package at.memories.mapper;

import at.memories.dto.PostDto;
import at.memories.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "jakarta")
public interface PostMapper {
    @Mapping(target = "description", source = "description")
    List<PostDto> toResource(List<Post> post);
}

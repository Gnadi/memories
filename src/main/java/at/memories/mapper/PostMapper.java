package at.memories.mapper;

import at.memories.dto.PostDto;
import at.memories.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper()
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    List<PostDto> toResource(List<Post> post);
    Post toResource(PostDto post);
}

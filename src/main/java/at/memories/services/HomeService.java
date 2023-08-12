package at.memories.services;

import at.memories.dto.HomeDto;
import at.memories.dto.PostDto;
import at.memories.dto.PostsDto;

public interface HomeService {
    void addHome(HomeDto homeDto) throws Exception;

    void addPost(PostDto postDto) throws Exception;

    PostsDto getPosts(Long userId, int pageNumber);
}

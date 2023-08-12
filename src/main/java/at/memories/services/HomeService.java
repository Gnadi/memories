package at.memories.services;

import at.memories.dto.HomeDto;
import at.memories.dto.PostDto;

public interface HomeService {
    void addHome(HomeDto homeDto) throws Exception;

    void addPost(PostDto postDto) throws Exception;

    void getPosts(Long homeId, Long userId);
}

package at.memories.services;

import at.memories.dto.HomeDto;
import at.memories.dto.PostDto;

public interface HomeService {
    void addHome(HomeDto homeDto);

    void addPost(PostDto postDto);

}

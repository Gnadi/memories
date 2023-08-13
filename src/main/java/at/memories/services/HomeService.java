package at.memories.services;

import at.memories.dto.HomeDto;
import at.memories.dto.PostDto;
import at.memories.dto.PostsDto;
import at.memories.model.PostImage;
import org.graalvm.collections.Pair;

import java.io.InputStream;
import java.util.List;

public interface HomeService {
    void addHome(HomeDto homeDto) throws Exception;
    void addPost(PostDto postDto) throws Exception;
    PostsDto getPosts(Long userId, int pageNumber);

    List<Pair<Long, InputStream>> loadImagesToPost(List<PostImage> postIdImageSource);
}

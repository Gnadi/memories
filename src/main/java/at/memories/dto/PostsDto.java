package at.memories.dto;

import java.util.List;

public class PostsDto {
    private List<PostDto> posts;
    private long totalPostCount;

    public List<PostDto> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDto> posts) {
        this.posts = posts;
    }

    public long getTotalPostCount() {
        return totalPostCount;
    }

    public void setTotalPostCount(long totalPostCount) {
        this.totalPostCount = totalPostCount;
    }
}

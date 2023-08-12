package at.memories.dao;

import at.memories.model.Home;
import at.memories.model.Post;

import java.util.List;

public interface HomeDao {
    void addHome(Home home);

    void addPost(Post post);

    Home findHomebyAdmin(Long user);
    Home findHomebyUser(Long user);

    List<Post> getPostsByPage(int pageNumber, int pageSize, long homeId);

    long getTotalPostCount(long homeId);

}

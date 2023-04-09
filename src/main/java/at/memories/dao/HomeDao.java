package at.memories.dao;

import at.memories.model.Home;
import at.memories.model.Post;

public interface HomeDao {
    void addHome(Home home);

    void addPost(Post post);

}

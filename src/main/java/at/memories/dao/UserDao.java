package at.memories.dao;

import at.memories.model.User;

public interface UserDao {
    void addUser(User user);

    User findUserByUsername(String username);
}

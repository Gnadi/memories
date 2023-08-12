package at.memories.dao;

import at.memories.model.User;

public interface UserDao {
    void addUser(User user);

    boolean userExists(String username);
    User findUserByUsername(String username);
    User findUserById(Long userId);


}

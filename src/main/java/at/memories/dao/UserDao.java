package at.memories.dao;

import at.memories.model.User;

import java.util.List;

public interface UserDao {
    void addUser(User user);

    boolean userExists(String username);
    List<User> findUserByUsername(String username);
    User findUserById(Long userId);


}

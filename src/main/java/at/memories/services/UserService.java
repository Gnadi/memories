package at.memories.services;

import at.memories.model.User;

import java.util.List;

public interface UserService {
    void addUser(User user);
    List<User> findUserByUsername(String username);
    User findUserById(Long userId);

    boolean existsUsername(String username);
}

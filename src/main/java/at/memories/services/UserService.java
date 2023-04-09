package at.memories.services;

import at.memories.model.User;

public interface UserService {
    void addUser(User user);

    User findUserByUsername(String username);
}

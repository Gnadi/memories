package at.memories.services;

import at.memories.impl.UserRepository;
import at.memories.model.User;
import io.quarkus.elytron.security.common.BcryptUtil;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

@RequestScoped
public class UserServiceBean implements UserService {

    @Inject
    UserRepository userRepository;

    @Override
    public void addUser(User user) {
        user.password = BcryptUtil.bcryptHash(user.password);
        userRepository.addUser(user);
    }
    @Override
    public List<User> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findUserById(userId);
    }

    @Override
    public boolean existsUsername(String username) {
        return userRepository.userExists(username);
    }


}

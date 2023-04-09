package at.memories.services;

import at.memories.impl.UserRepository;
import at.memories.mapper.UserMapper;
import at.memories.model.User;
import io.quarkus.elytron.security.common.BcryptUtil;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

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
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
}

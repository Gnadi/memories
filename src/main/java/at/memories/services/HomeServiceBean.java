package at.memories.services;

import at.memories.dto.HomeDto;
import at.memories.dto.PostDto;
import at.memories.impl.HomeRepository;
import at.memories.impl.UserRepository;
import at.memories.mapper.UserMapper;
import at.memories.model.Home;
import at.memories.model.Post;
import at.memories.model.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@RequestScoped
@Transactional
public class HomeServiceBean implements HomeService {
    @Inject
    HomeRepository homeRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    UserMapper userMapper;
    @Override
    public void addHome(HomeDto homeDto) {
        User admin = userMapper.toResource(homeDto.getAdmin());
        User user = userMapper.toResource(homeDto.getUser());
        userRepository.addUser(admin);
        userRepository.addUser(user);
        Home home = new Home();
        home.setAdminId(admin.getId());
        home.setUserId(user.getId());
        home.setName(homeDto.getHomeName());
        homeRepository.addHome(home);
    }

    @Override
    public void addPost(PostDto postDto) {
        Post post = new Post();
        post.setDescription(postDto.getDescription());
        // TODO Upload image to GCP
        post.setHome(new Home());
        homeRepository.addPost(post);
    }
}

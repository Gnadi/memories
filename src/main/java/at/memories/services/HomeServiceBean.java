package at.memories.services;

import at.memories.dto.HomeDto;
import at.memories.dto.PostDto;
import at.memories.impl.HomeRepository;
import at.memories.impl.UserRepository;
import at.memories.mapper.UserMapper;
import at.memories.model.Home;
import at.memories.model.Post;
import at.memories.model.User;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.UUID;

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
    public void addPost(PostDto postDto) throws Exception {
        Post post = new Post();
        post.setDescription(postDto.getDescription());
        Home home = homeRepository.findHomebyAdmin(Long.valueOf(postDto.getAdmin()));
        if (home != null) {
            post.setHome(home);
            Storage storage = StorageOptions.getDefaultInstance().getService();
            Bucket bucket = storage.get("storage name");
            Blob blob = bucket.create(home.getId().toString() + UUID.randomUUID(), postDto.getImage());
            post.setImageSource(blob.getBlobId().toString());
            homeRepository.addPost(post);
        } else {
            throw new Exception("no home available");
        }
    }

    @Override
    public void getPosts(Long homeId, Long userId) {

    }

    // TODO load posts of Home
}

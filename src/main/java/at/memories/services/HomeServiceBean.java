package at.memories.services;

import at.memories.dto.HomeDto;
import at.memories.dto.PostDto;
import at.memories.dto.PostsDto;
import at.memories.impl.HomeRepository;
import at.memories.impl.UserRepository;
import at.memories.mapper.PostMapper;
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

import java.util.List;
import java.util.UUID;

@RequestScoped
@Transactional
public class HomeServiceBean implements HomeService {
    @Inject
    HomeRepository homeRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    UserService userService;

    @Inject
    UserMapper userMapper;

    @Inject
    PostMapper postMapper;
    @Override
    public void addHome(HomeDto homeDto) throws Exception {
        User admin = userMapper.toResource(homeDto.getAdmin());
        User user = userMapper.toResource(homeDto.getUser());
        userService.addUser(admin);
        userService.addUser(user);
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
            Bucket bucket = storage.get("moments-data");
            if (bucket == null) {
                bucket = storage.create(Bucket.newBuilder(home.getName()).build());
            }
            Blob blob = bucket.create(home.getId().toString() + UUID.randomUUID(), postDto.getImage());
            post.setImageSource(blob.getBlobId().getName());
            post.setBucket(blob.getBlobId().getBucket());
            post.setGeneration(blob.getBlobId().getGeneration().toString());
            homeRepository.addPost(post);
        } else {
            throw new Exception("no home available");
        }
    }

    @Override
    public PostsDto getPosts (Long userId, int pageNumber) {
        User userById = userService.findUserById(userId);
        Home home;
        if (userById.role.equalsIgnoreCase("admin")) {
            home = homeRepository.findHomebyAdmin(userId);
        } else {
            home = homeRepository.findHomebyUser(userId);
        }
        if (home != null) {
            long homeId = home.getId();
            int pageSize = 10;
            long totalPostCount = homeRepository.getTotalPostCount(homeId);
            PostsDto postsDto = new PostsDto();
            List<Post> postsByPage = homeRepository.getPostsByPage(pageNumber, pageSize, homeId);
            List<PostDto> posts = postMapper.toResource(postsByPage);
            postsDto.setPosts(posts);
            postsDto.setTotalPostCount(totalPostCount);
            return postsDto;
        }
        return new PostsDto();
    }

    // TODO create dynamically buckets for each home
}

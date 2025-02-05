package at.memories.services;

import at.memories.dto.HomeDto;
import at.memories.dto.PostDto;
import at.memories.dto.PostsDto;
import at.memories.impl.HomeRepository;
import at.memories.mapper.PostMapper;
import at.memories.mapper.UserMapper;
import at.memories.model.Home;
import at.memories.model.Post;
import at.memories.model.PostImage;
import at.memories.model.User;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.tuple.Pair;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequestScoped
@Transactional
public class HomeServiceBean implements HomeService {
    private static final int MAX_SIZE_BYTES = 50 * 1024 * 1024;
    @Inject
    HomeRepository homeRepository;

    @Inject
    UserService userService;

    @Override
    public void addHome(HomeDto homeDto) throws Exception {
        User admin = UserMapper.INSTANCE.toResource(homeDto.getAdmin());
        User user = UserMapper.INSTANCE.toResource(homeDto.getUser());
        checkUsernames(homeDto);
        userService.addUser(admin);
        userService.addUser(user);
        Home home = new Home();
        home.setAdminId(admin.getId());
        home.setUserId(user.getId());
        home.setName(homeDto.getHomeName());
        homeRepository.addHome(home);
    }

    private void checkUsernames(HomeDto homeDto) throws Exception {
        // Username wird durch den URL namen angegeben und muss gleich sein ->
        // dadurch ist ein login mit ausschließlich passwort möglich. Passwörter müssen unterschiedlich sein
        if (!homeDto.getAdmin().getUsername().equalsIgnoreCase(homeDto.getUser().getUsername()) ||
                userService.existsUsername(homeDto.getAdmin().getUsername()) ||
                homeDto.getAdmin().getPassword().equalsIgnoreCase(homeDto.getUser().getPassword())) {
            throw new Exception("Username already taken");
        }
    }

    @Override
    public void addPost(PostDto postDto) throws Exception {
        Post post = PostMapper.INSTANCE.toResource(postDto);
        Home home = homeRepository.findHomebyAdmin(Long.valueOf(postDto.getAdmin()));
        if (home != null) {
            post.setHome(home);
            Storage storage = StorageOptions.getDefaultInstance().getService();
            BlobId blobId = BlobId.of("moments-data", home.getId().toString() + UUID.randomUUID());
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
            byte[] content = postDto.getImage();
            if (content.length <= MAX_SIZE_BYTES) {
                try (WriteChannel writer = storage.writer(blobInfo)) {
                    writer.write(ByteBuffer.wrap(content));
                } finally {
                    post.setImageSource(blobId.getName());
                    post.setBucket(blobId.getBucket());
                    homeRepository.addPost(post);
                }
            } else {
                throw new Exception("image too large");
            }

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
            List<PostDto> posts = PostMapper.INSTANCE.toResource(postsByPage);
            postsDto.setPosts(posts);
            postsDto.setTotalPostCount(totalPostCount);
            return postsDto;
        }
        return new PostsDto();
    }

    @Override
    public List<Pair<Long, InputStream>> loadImagesToPost(List<PostImage> postIdImageSource) {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        List<Pair<Long, InputStream>> imagesToPosts = new ArrayList<>();
        postIdImageSource.forEach(post ->{
            Blob blob = storage.get(BlobId.of(post.getBucketName(), post.getImageSource()));
            byte[] content = blob.getContent();
            imagesToPosts.add(Pair.of(post.getPostId(), new ByteArrayInputStream(content)));
        });
        return imagesToPosts;
    }

    // TODO create dynamically buckets for each home
}

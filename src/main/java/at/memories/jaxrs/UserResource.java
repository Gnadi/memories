package at.memories.jaxrs;

import at.memories.dto.PostsDto;
import at.memories.model.PostImage;
import at.memories.services.HomeService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.graalvm.collections.Pair;

import java.io.InputStream;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Path("/api/users")
public class UserResource {
    @Inject
    JsonWebToken jwt;

    @Inject
    HomeService homeService;
    @GET
    @RolesAllowed("user")
    @Path("/me")
    public String me(@Context SecurityContext securityContext) {
        return securityContext.getUserPrincipal().getName();
    }
    @GET
    @RolesAllowed({"user", "admin"})
    @Path("/home/posts") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
    public PostsDto getPosts(@QueryParam("pageNumber") int pageNumber){
        if (hasNoJwt()) {
            return new PostsDto();
        }
        try {
            Long userId = Long.valueOf(jwt.getClaim("sub").toString());
            return homeService.getPosts(userId, pageNumber);
        } catch (Exception e) {
            return new PostsDto();
        }
    }

    @POST
    @RolesAllowed({"user", "admin"})
    @Path("/home/posts/images") @Produces(MediaType.APPLICATION_OCTET_STREAM) @Consumes(MediaType.APPLICATION_JSON)
    public Response getImagesToPosts(List<PostImage> postIdImageSource){
        if (hasNoJwt()) {
            return Response.serverError().build();
        }
        try {
            List<Pair<Long, InputStream>> imagesToPost = homeService.loadImagesToPost(postIdImageSource);
            return Response.ok(imagesToPost).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    private boolean hasNoJwt() {
        return jwt.getClaimNames() == null;
    }

}

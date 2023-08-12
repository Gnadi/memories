package at.memories.jaxrs;

import at.memories.dto.PostsDto;
import at.memories.services.HomeService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;

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
        if (!hasJwt()) {
            return new PostsDto();
        }
        try {
            Long userId = Long.valueOf(jwt.getClaim("sub").toString());
            return homeService.getPosts(userId, pageNumber);
        } catch (Exception e) {
            return new PostsDto();
        }
    }
    private boolean hasJwt() {
        return jwt.getClaimNames() != null;
    }

}

package at.memories.jaxrs;

import at.memories.dto.PostDto;
import at.memories.services.HomeService;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/api/admin")
public class AdminResource {
    @Inject
    HomeService homeService;

    @Inject
    JsonWebToken jwt;

    @GET
    @RolesAllowed("admin")
    @Produces(MediaType.TEXT_PLAIN)
    public String adminResource() {
        return "admin";
    }

    @POST
    @RolesAllowed("admin")
    @Path("/home/post") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
    public Response addPost(@Context SecurityContext securityContext, PostDto post){
        if (!hasJwt()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        post.setAdmin(jwt.getClaim("sub").toString());
        try {
            homeService.addPost(post);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().status(Response.Status.CREATED).build();
    }

    private boolean hasJwt() {
        return jwt.getClaimNames() != null;
    }
}

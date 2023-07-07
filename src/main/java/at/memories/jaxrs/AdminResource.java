package at.memories.jaxrs;

import at.memories.dto.PostDto;
import at.memories.services.HomeService;
import jakarta.annotation.security.RolesAllowed;
import org.eclipse.microprofile.jwt.JsonWebToken;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

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

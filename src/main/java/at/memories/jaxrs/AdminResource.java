package at.memories.jaxrs;

import at.memories.dto.PostDto;
import at.memories.services.HomeService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

@Path("/api/admin")
public class AdminResource {
    @Inject
    HomeService homeService;

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
        // TODO check if the post is for the home of the admin (security)
        // TODO nachladen vom home damit das im Post gesetzt wird
        Principal userPrincipal = securityContext.getUserPrincipal();
        homeService.addPost(post);
        return Response.ok().build();
    }
}

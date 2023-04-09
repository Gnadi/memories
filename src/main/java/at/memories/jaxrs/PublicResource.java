package at.memories.jaxrs;

import at.memories.dto.HomeDto;
import at.memories.dto.PostDto;
import at.memories.dto.UserDto;
import at.memories.mapper.UserMapper;
import at.memories.model.request.AuthRequest;
import at.memories.model.Home;
import at.memories.model.request.ResponseRequest;
import at.memories.model.User;
import at.memories.services.HomeService;
import at.memories.services.UserService;
import at.memories.util.TokenGenerator;
import io.quarkus.elytron.security.common.BcryptUtil;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/api/public")
public class PublicResource {

    @Inject
    UserService userService;

    @Inject
    HomeService homeService;

    @Inject
    UserMapper userMapper;

    @ConfigProperty(name = "com.ard333.quarkusjwt.jwt.duration") public Long duration;
    @ConfigProperty(name = "mp.jwt.verify.issuer") public String issuer;

    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public String publicResource() {
        return "public";
    }

    @POST
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void addUser(UserDto user) {
        userService.addUser(userMapper.toResource(user));
    }

    @PermitAll
    @POST
    @Path("/login") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@Context SecurityContext securityContext, AuthRequest authRequest) {
        User user = this.userService.findUserByUsername(authRequest.getUsername());
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        if(BcryptUtil.matches(authRequest.getPassword(), user.password)) {
            try {
                String token = new TokenGenerator().generateToken(user.username, user.role, duration, issuer);
                return Response.ok(new ResponseRequest(token)).build();
            } catch (Exception e) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }


    @PermitAll
    @POST
    @Path("/home") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
    public Response addHome(HomeDto home){
        homeService.addHome(home);
        return Response.ok().build();
    }
}


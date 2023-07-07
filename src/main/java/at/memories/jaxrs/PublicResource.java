package at.memories.jaxrs;

import at.memories.dto.HomeDto;
import at.memories.dto.UserDto;
import at.memories.mapper.UserMapper;
import at.memories.model.User;
import at.memories.model.request.AuthRequest;
import at.memories.model.request.ResponseRequest;
import at.memories.services.HomeService;
import at.memories.services.UserService;
import at.memories.util.TokenGenerator;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.annotation.security.PermitAll;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/api/public")
public class PublicResource {

    @Inject
    UserService userService;

    @Inject
    HomeService homeService;

    @Inject
    UserMapper userMapper;

    @ConfigProperty(name = "at.memories.quarkusjwt.jwt.duration") public Long duration;
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
    public Response login(@Context SecurityContext securityContext, AuthRequest authRequest) throws Exception {
        User user;
        try {
            user = this.userService.findUserByUsername(authRequest.getUsername());
        } catch (NoResultException | NonUniqueResultException e) {
            throw new Exception("No User found");
        }
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        if(BcryptUtil.matches(authRequest.getPassword(), user.password)) {
            try {
                String token = new TokenGenerator().generateToken(user.getId(), user.role, duration, issuer);
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


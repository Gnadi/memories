package at.memories.mapper;

import at.memories.dto.UserDto;
import at.memories.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta")
public interface UserMapper {
    @Mapping(target = "username", source = "username")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "password", source = "password")
    User toResource(UserDto person);
}

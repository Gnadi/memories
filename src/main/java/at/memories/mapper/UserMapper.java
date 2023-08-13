package at.memories.mapper;

import at.memories.dto.UserDto;
import at.memories.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta")
public interface UserMapper {
    User toResource(UserDto person);
}

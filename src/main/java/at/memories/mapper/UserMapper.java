package at.memories.mapper;

import at.memories.dto.UserDto;
import at.memories.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toResource(UserDto person);
}

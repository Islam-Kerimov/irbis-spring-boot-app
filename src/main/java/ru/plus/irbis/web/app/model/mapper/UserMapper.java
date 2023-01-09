package ru.plus.irbis.web.app.model.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import ru.plus.irbis.web.app.model.dto.UserDto;
import ru.plus.irbis.web.app.model.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Named("User")
    UserDto entityUserToDto(User user);

    @IterableMapping(qualifiedByName = "User")
    List<UserDto> entityUserListToDtoList(List<User> users);
}

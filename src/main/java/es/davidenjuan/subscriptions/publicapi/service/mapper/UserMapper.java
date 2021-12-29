package es.davidenjuan.subscriptions.publicapi.service.mapper;

import org.mapstruct.Mapper;

import es.davidenjuan.subscriptions.publicapi.domain.User;
import es.davidenjuan.subscriptions.publicapi.service.dto.UserDTO;

@Mapper(componentModel = "spring", uses = {})
public interface UserMapper {

    User toEntity(UserDTO dto);

    UserDTO toDto(User entity);
}

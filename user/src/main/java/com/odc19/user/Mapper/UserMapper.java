package com.odc19.user.Mapper;

import com.odc19.clients.user.UserDTO;
import com.odc19.user.entity.UserEntity;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
@DecoratedWith(UserMapperDecorator.class)
public interface UserMapper {
    UserEntity userDtoToEntity(UserDTO userDTO);

    UserDTO userEntityToDto(UserEntity userEntity);

    List<UserDTO> userEntitiesToUserDTOS(List<UserEntity> userEntities);

    @Mappings({
            @Mapping(target = "userId", ignore = true),
            @Mapping(target = "userName", ignore = true),
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "createdDate", ignore = true),
            @Mapping(target = "lastedModifiedDate", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "lastedModifiedBy", ignore = true),
    })
    UserEntity updateUserMapper(@MappingTarget() UserEntity userOld, UserEntity userNew);
}

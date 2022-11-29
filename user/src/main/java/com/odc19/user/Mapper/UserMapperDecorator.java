package com.odc19.user.Mapper;

import com.odc19.clients.user.UserDTO;
import com.odc19.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

public abstract class UserMapperDecorator implements UserMapper {
    @Autowired
    @Qualifier("delegate")
    private UserMapper delegate;

    @Override
    public UserDTO userEntityToDto(UserEntity userEntity) {
        UserDTO userDTO = delegate.userEntityToDto(userEntity);
        List<String> roleNameList = new ArrayList<>();
        userEntity.getRoleEntities().forEach(roleEntity -> roleNameList.add(roleEntity.getRoleName()));
        userDTO.setRoleNames(roleNameList);
        return userDTO;
    }

    @Override
    public List<UserDTO> userEntitiesToUserDTOS(List<UserEntity> userEntities) {
        List<UserDTO> userDTOS = new ArrayList<>();
        for (UserEntity userEntity : userEntities) {
            UserDTO userDTO = delegate.userEntityToDto(userEntity);
            List<String> roleNameList = new ArrayList<>();
            userEntity.getRoleEntities().forEach(roleEntity -> roleNameList.add(roleEntity.getRoleName()));
            userDTO.setRoleNames(roleNameList);
            userDTOS.add(userDTO);
        }
        return userDTOS;
    }
}

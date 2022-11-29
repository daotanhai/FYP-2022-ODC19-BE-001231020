package com.odc19.user.Service.iService;

import com.odc19.clients.user.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IUserService {
    void saveUser(UserDTO userDTO, List<MultipartFile> multipartFileList) throws IOException;

    void saveNewShipper(UserDTO userDTO);

    void deleteUserById(String userId);

    void recoverUserById(String userId);

    UserDTO updateUser(UserDTO userDTO, List<MultipartFile> multipartFiles) throws IOException;

    UserDTO getUser(String userId);

    List<UserDTO> getUserList(String filterParams);

    void updatePasswordForUser(UserDTO userDTO);

    void resetPasswordBasedOnToken(UserDTO userDTO);

    void generateResetPasswordToken(UserDTO userDTO);

    void updateShipperLocation(UserDTO userDTO);

    List<UserDTO> getShipperList();
}

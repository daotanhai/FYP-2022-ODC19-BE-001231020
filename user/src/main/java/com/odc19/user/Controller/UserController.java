package com.odc19.user.Controller;

import com.odc19.clients.user.UserDTO;
import com.odc19.user.Service.iService.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    IUserService userService;

    @PostMapping(value = "/new", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public void saveNewUser(@RequestPart UserDTO userDTO,
                            @RequestPart(required = false) List<MultipartFile> multipartFiles) throws IOException {
        log.info("UserDTO received: {}", userDTO);
        userService.saveUser(userDTO, multipartFiles);
    }

    @PostMapping(value = "/new/shipper")
    public void saveNewUserDto(@RequestBody UserDTO userDTO) {
        userService.saveNewShipper(userDTO);
    }

    @PostMapping(value = "/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public UserDTO updateUser(@RequestPart UserDTO userDTO,
                           @RequestPart(required = false) List<MultipartFile> multipartFiles) throws IOException {
        return userService.updateUser(userDTO, multipartFiles);
    }

    @PutMapping(value = "/delete/{userId}")
    public void deleteUserById(@PathVariable("userId") String userId) {
        userService.deleteUserById(userId);
    }

    @PutMapping(value = "/recover/{userId}")
    public void recoverUserById(@PathVariable("userId") String userId) {
        userService.recoverUserById(userId);
    }

    @PutMapping("/password/update")
    public void updatePasswordForUser(@RequestBody UserDTO userDTO) {
        userService.updatePasswordForUser(userDTO);
    }

    @PutMapping("/password/reset")
    public void resetPasswordBasedOnToken(@RequestBody(required = false) UserDTO userDTO) {
        if (userDTO.getRandomTokenResetPassword() != null && userDTO.getUserName() == null) {
            userService.resetPasswordBasedOnToken(userDTO);
        }
        if (userDTO.getUserName() != null && userDTO.getRandomTokenResetPassword() == null) {
            userService.generateResetPasswordToken(userDTO);
        }
    }

    @GetMapping("/list")
    public List<UserDTO> getListUser(@RequestParam(name = "filterParams", required = false) String filterParams) {
        return userService.getUserList(filterParams);
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable(name = "id") String id) {
        return userService.getUser(id);
    }

    @GetMapping("/list/shipper")
    public List<UserDTO> getShipperList() {
        return userService.getShipperList();
    }

    @PostMapping("/update/shipper/location")
    public void updateShipperLocation(@RequestBody UserDTO userDTO) {
        userService.updateShipperLocation(userDTO);
    }
}

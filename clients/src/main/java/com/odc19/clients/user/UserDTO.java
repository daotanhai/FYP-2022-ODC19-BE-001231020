package com.odc19.clients.user;

import com.odc19.clients.BaseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDTO extends BaseDTO {
    private String userId;

    private String userName;

    private String password;

    private String oldPassword;

    private String identityNumber;

    private String phoneNumber;

    private String email;

    private String fullName;

    private String address;

    private String streetNumber;

    private String postCode;

    private Double longitude;

    private Double latitude;

    private String identityCardUrl;

    private String randomTokenResetPassword;

    private List<String> roleNames;

    private boolean isDeleted;
}

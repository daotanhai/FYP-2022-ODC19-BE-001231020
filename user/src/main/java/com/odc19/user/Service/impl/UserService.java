package com.odc19.user.Service.impl;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.maps.model.LatLng;
import com.odc19.AmazonCredentials.AmazonKeys;
import com.odc19.amqp.RabbitMQMessageProducer;
import com.odc19.baseEntity.BaseEntity;
import com.odc19.clients.notification.NotificationDTO;
import com.odc19.clients.user.UserDTO;
import com.odc19.geocode.GeocodeDTO;
import com.odc19.geocode.GeocodeService;
import com.odc19.typeofmessage.TypeOfMessage;
import com.odc19.user.CustomException.DuplicatedUsernameException;
import com.odc19.user.CustomException.IncorrectPasswordException;
import com.odc19.user.CustomException.RoleEmptyException;
import com.odc19.user.CustomException.TokenExpiredException;
import com.odc19.user.Mapper.UserMapper;
import com.odc19.user.PasswordEncoder.PasswordEncoderCustom;
import com.odc19.user.Repository.readRepository.RoleReadRepository;
import com.odc19.user.Repository.readRepository.UserReadRepository;
import com.odc19.user.Repository.writeRepository.UserWriteRepository;
import com.odc19.user.Service.iService.IUserService;
import com.odc19.user.entity.RoleEntity;
import com.odc19.user.entity.UserEntity;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(transactionManager = "transactionManagerWrite")
public class UserService implements IUserService, UserDetailsService {

    @Autowired
    UserWriteRepository userWriteRepository;

    @Autowired
    UserReadRepository userReadRepository;

    @Autowired
    RoleReadRepository roleReadRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordEncoderCustom passwordEncoderCustom;

    @Autowired
    RabbitMQMessageProducer rabbitMQMessageProducer;

    private AmazonS3 s3Client;

    @PostConstruct
    private void initializeAmazon() {
        this.s3Client = new AmazonS3Client(new BasicAWSCredentials(AmazonKeys.ACCESS_KEY, AmazonKeys.SECRET_KEY));
    }

    @Override
    public void saveUser(UserDTO userDTO, List<MultipartFile> multipartFiles) throws IOException {
        if (!isDuplicatedUserNameForSaveNew(userDTO.getUserName())) {
            UserEntity userEntity = userMapper.userDtoToEntity(userDTO);
            userEntity.setDeleted(false);
            if (userEntity.getRoleEntities() == null) {
                userEntity.setRoleEntities(roleReadRepository.findAllByRoleNameIn(Collections.singletonList("NormalUser")));
            } else {
                userEntity.setRoleEntities(roleReadRepository.findAllByRoleNameIn(userDTO.getRoleNames()));
            }
            if (userEntity.getRoleEntities().isEmpty()) {
                throw new RoleEmptyException();
            }
//            set up save file and save url of identity card
            if (multipartFiles != null) {
                List<String> urlList = new ArrayList<>();
                for (MultipartFile multipartFile : multipartFiles) {
                    File file = convertMultipartToFile(multipartFile);
                    String fileName = new Date().getTime() + "-" + multipartFile.getOriginalFilename().replace(" ", "_");
                    String fileUrl = AmazonKeys.END_POINT_URL + "/" + AmazonKeys.BUCKET_NAME + "/" + fileName;
                    s3Client.putObject(new PutObjectRequest(AmazonKeys.BUCKET_NAME, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
                    urlList.add(fileUrl);
                }
                userEntity.setIdentityCardUrl(String.valueOf(urlList));
            }
//            get latitude and longitude of user
            GeocodeDTO geocodeDTO = new GeocodeDTO();
            geocodeDTO.setDetailAddress(userDTO.getAddress());
            geocodeDTO.setStreetNumber(userDTO.getStreetNumber());
            geocodeDTO.setPostCode(userDTO.getPostCode());
            GeocodeService geocodeService = new GeocodeService();
            LatLng latLng = geocodeService.getLatitudeLongitudeBasedOnDetailsAddress(geocodeDTO);
            userEntity.setLatitude(latLng.lat);
            userEntity.setLongitude(latLng.lng);
            userEntity.setPassword(passwordEncoderCustom.getDecoder().encode(userDTO.getPassword()));
            userWriteRepository.save(userEntity);
//            let's publish message to queue with payload is notificationDTO
            publishMessageToQueue(sendNotification(userEntity, TypeOfMessage.SAVE_NEW_USER));
        } else {
            throw new DuplicatedUsernameException();
        }
    }

    @Override
    public void saveNewShipper(UserDTO userDTO) {
        if (!isDuplicatedUserNameForSaveNew(userDTO.getUserName())) {
            UserEntity userEntity = userMapper.userDtoToEntity(userDTO);
            userEntity.setDeleted(false);
            if (userEntity.getRoleEntities() == null) {
                userEntity.setRoleEntities(roleReadRepository.findAllByRoleNameIn(Collections.singletonList("Shipper")));
            } else {
                userEntity.setRoleEntities(roleReadRepository.findAllByRoleNameIn(userDTO.getRoleNames()));
            }
            if (userEntity.getRoleEntities().isEmpty()) {
                throw new RoleEmptyException();
            }
//            get latitude and longitude of user
            GeocodeDTO geocodeDTO = new GeocodeDTO();
            geocodeDTO.setDetailAddress(userDTO.getAddress());
            geocodeDTO.setStreetNumber(userDTO.getStreetNumber());
            geocodeDTO.setPostCode(userDTO.getPostCode());
            GeocodeService geocodeService = new GeocodeService();
            LatLng latLng = geocodeService.getLatitudeLongitudeBasedOnDetailsAddress(geocodeDTO);
            userEntity.setLatitude(latLng.lat);
            userEntity.setLongitude(latLng.lng);
            userEntity.setPassword(passwordEncoderCustom.getDecoder().encode(userDTO.getPassword()));
            userWriteRepository.save(userEntity);
//            let's publish message to queue with payload is notificationDTO
            publishMessageToQueue(sendNotification(userEntity, TypeOfMessage.SAVE_NEW_USER));
        } else {
            throw new DuplicatedUsernameException();
        }
    }

    @Override
    public void deleteUserById(String userId) {
        UserEntity userEntity = userReadRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        userEntity.setDeleted(true);
        userWriteRepository.save(userEntity);
    }

    @Override
    public void recoverUserById(String userId) {
        UserEntity userEntity = userReadRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        userEntity.setDeleted(false);
        userWriteRepository.save(userEntity);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, List<MultipartFile> multipartFiles) throws IOException {
        UserEntity oldUser = userReadRepository.findById(userDTO.getUserId()).orElseThrow(EntityNotFoundException::new);
        UserEntity user = userMapper.updateUserMapper(oldUser, userMapper.userDtoToEntity(userDTO));
        Set<RoleEntity> roleEntities = roleReadRepository.findAllByRoleNameIn(userDTO.getRoleNames());
        user.setRoleEntities(roleEntities);
        if (multipartFiles != null) {
            List<String> urlList = new ArrayList<>();
            for (MultipartFile multipartFile : multipartFiles) {
                File file = convertMultipartToFile(multipartFile);
                String fileName = new Date().getTime() + "-" + multipartFile.getOriginalFilename().replace(" ", "_");
                String fileUrl = AmazonKeys.END_POINT_URL + "/" + AmazonKeys.BUCKET_NAME + "/" + fileName;
                s3Client.putObject(new PutObjectRequest(AmazonKeys.BUCKET_NAME, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
                urlList.add(fileUrl);
            }
            user.setIdentityCardUrl(String.valueOf(urlList));
        }
//            get latitude and longitude of user
        GeocodeDTO geocodeDTO = new GeocodeDTO();
        geocodeDTO.setDetailAddress(userDTO.getAddress());
        geocodeDTO.setStreetNumber(userDTO.getStreetNumber());
        geocodeDTO.setPostCode(userDTO.getPostCode());
        GeocodeService geocodeService = new GeocodeService();
        LatLng latLng = geocodeService.getLatitudeLongitudeBasedOnDetailsAddress(geocodeDTO);
        user.setLatitude(latLng.lat);
        user.setLongitude(latLng.lng);
        userWriteRepository.save(user);
        publishMessageToQueue(sendNotification(user, TypeOfMessage.UPDATE_USER));
        return userMapper.userEntityToDto(user);
    }

    @Override
    public UserDTO getUser(String userId) {
        return userMapper.userEntityToDto(userWriteRepository.findById(userId).orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public List<UserDTO> getUserList(String filterParams) {
        if (filterParams != null) {
            if (filterParams.equalsIgnoreCase("isDeleted")) {
                return userMapper.userEntitiesToUserDTOS(userReadRepository.findAll()
                        .stream()
                        .filter(userEntity -> userEntity.isDeleted())
                        .collect(Collectors.toList()));
            }
        } else {
            return userMapper.userEntitiesToUserDTOS(
                    userReadRepository.findAll()
                            .stream()
                            .filter(userEntity -> !userEntity.isDeleted())
                            .collect(Collectors.toList()));
        }
        return null;
    }

    @Override
    public void updatePasswordForUser(UserDTO userDTO) {
        UserEntity user = userReadRepository.findById(userDTO.getUserId()).orElseThrow(EntityNotFoundException::new);
        if (passwordEncoderCustom.getDecoder().matches(userDTO.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoderCustom.getDecoder().encode(userDTO.getPassword()));
            userWriteRepository.save(user);
            publishMessageToQueue(sendNotification(user, TypeOfMessage.CHANGE_PASSWORD));
        } else {
            throw new IncorrectPasswordException();
        }
    }

    @Override
    public void resetPasswordBasedOnToken(UserDTO userDTO) {
        UserEntity userEntity = userReadRepository.getUserEntityByRandomTokenResetPassword(userDTO.getRandomTokenResetPassword(), false);
        if (userEntity == null) {
            throw new TokenExpiredException();
        } else {
            userEntity.setPassword(passwordEncoderCustom.getDecoder().encode(userDTO.getPassword()));
            userEntity.setTokenUsed(true);
            userWriteRepository.save(userEntity);
            publishMessageToQueue(sendNotification(userEntity, TypeOfMessage.CHANGE_PASSWORD));
        }

    }

    @Override
    public void generateResetPasswordToken(UserDTO userDTO) {
        String randomToken = RandomString.make(50);
        UserEntity user = userReadRepository.getUserEntityByUserName(userDTO.getUserName());
        user.setRandomTokenResetPassword(randomToken);
        user.setTokenUsed(false);
        userWriteRepository.save(user);
        publishMessageToQueue(sendNotification(user, TypeOfMessage.GENERATE_RESET_PASSWORD_TOKEN));
    }

    @Override
    public void updateShipperLocation(UserDTO userDTO) {
        UserEntity userEntity = userReadRepository.getUserEntityByUserName(userDTO.getUserName());
        userEntity.setLatitude(userDTO.getLatitude());
        userEntity.setLongitude(userDTO.getLongitude());
        userWriteRepository.save(userEntity);
    }

    @Override
    public List<UserDTO> getShipperList() {
        return userReadRepository.findAll()
                .stream()
                .map(userEntity ->  userMapper.userEntityToDto(userEntity))
                .filter(userDTO -> userDTO.getRoleNames().contains("Shipper"))
                .collect(Collectors.toList());
    }

    private boolean isDuplicatedUserNameForSaveNew(String userName) {
        UserEntity user = userReadRepository.checkExistedUserNameForSaveNew(userName);
        return user != null;
    }

    private File convertMultipartToFile(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convertFile);
        fos.write(file.getBytes());
        fos.close();
        return convertFile;
    }

    //    function to publish message to notification.queue
    private void publishMessageToQueue(Object payloadParam) {
        rabbitMQMessageProducer.publish(payloadParam, "internal.exchange", "internal.notification.routing-key");
    }

    private NotificationDTO sendNotification(UserEntity user, String typeOfMessage) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setToCustomerEmail(user.getEmail());
        notificationDTO.setToCustomerId(user.getUserId());
        if (typeOfMessage.equals(TypeOfMessage.SAVE_NEW_USER)) {
            notificationDTO.setMessage(user.getEmail()+" have created an account in our service");
        }
        if (typeOfMessage.equals(TypeOfMessage.UPDATE_USER)) {
            notificationDTO.setMessage(user.getEmail()+" updated information");
        }
        if (typeOfMessage.equals(TypeOfMessage.CHANGE_PASSWORD)) {
            notificationDTO.setMessage(user.getEmail()+" changed password");
        }
        if (typeOfMessage.equals(TypeOfMessage.GENERATE_RESET_PASSWORD_TOKEN)) {
            notificationDTO.setMessage(user.getEmail()+" generated new reset password token");
        }
        notificationDTO.setResetPasswordToken(user.getRandomTokenResetPassword());
        notificationDTO.setTypeOfMessage(typeOfMessage);
        return notificationDTO;
    }


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity userEntity = userReadRepository.getUserEntityAndRolesByUserName(userName);
        if (userEntity != null && !userEntity.isDeleted()) {
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            userEntity.getRoleEntities().forEach(userRoleEntity -> {
                authorities.add(new SimpleGrantedAuthority(userRoleEntity.getRoleName()));
            });
            return new org.springframework.security.core.userdetails.User(userEntity.getUserName(), userEntity.getPassword(), authorities);
        }
        if (userEntity == null) {
            throw new UsernameNotFoundException("Can not find userName:" + userName + " in the database");
        }
        if (userEntity.isDeleted()) {
            throw new UsernameNotFoundException("This user:" + userName + " has been deleted!");
        } else {
            throw new RuntimeException();
        }
    }
}

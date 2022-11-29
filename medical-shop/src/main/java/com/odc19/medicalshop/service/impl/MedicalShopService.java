package com.odc19.medicalshop.service.impl;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.maps.model.LatLng;
import com.odc19.AmazonCredentials.AmazonKeys;
import com.odc19.clients.medicalShop.MedicalShopDTO;
import com.odc19.clients.user.UserDTO;
import com.odc19.geocode.GeocodeDTO;
import com.odc19.geocode.GeocodeService;
import com.odc19.medicalshop.entity.MedicalShopEntity;
import com.odc19.medicalshop.mapper.GoodsMapper;
import com.odc19.medicalshop.mapper.MedicalShopMapper;
import com.odc19.medicalshop.repository.readRepository.GoodsReadRepository;
import com.odc19.medicalshop.repository.readRepository.MedicalShopReadRepository;
import com.odc19.medicalshop.repository.writeRepository.MedicalShopWriteRepository;
import com.odc19.medicalshop.service.iService.IMedicalShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MedicalShopService implements IMedicalShopService {

    @Autowired
    MedicalShopReadRepository readRepository;

    @Autowired
    MedicalShopWriteRepository writeRepository;

    @Autowired
    MedicalShopMapper medicalShopMapper;

    @Autowired
    GoodsReadRepository goodsReadRepository;

    @Autowired
    GoodsMapper goodsMapper;

    private AmazonS3 s3Client;

    @PostConstruct
    private void initializeAmazon() {
        this.s3Client = new AmazonS3Client(new BasicAWSCredentials(AmazonKeys.ACCESS_KEY, AmazonKeys.SECRET_KEY));
    }

    @Override
    public void saveMedicalShop(MedicalShopDTO medicalShopDTO, List<MultipartFile> multipartFiles) throws IOException {
        if (multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                File file = convertMultipartToFile(multipartFile);
                String fileName = new Date().getTime() + "-" + multipartFile.getOriginalFilename().replace(" ", "_");
                String fileUrl = AmazonKeys.END_POINT_URL + "/" + AmazonKeys.BUCKET_NAME + "/" + fileName;
                s3Client.putObject(new PutObjectRequest(AmazonKeys.BUCKET_NAME, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
                medicalShopDTO.setMedicalShopUrlImage(fileUrl);
            }
        }
        writeRepository.save(medicalShopMapper.dtoToEntity(findingLatitudeAndLongitude(medicalShopDTO)));
    }

    @Override
    public void updateMedicalShop(MedicalShopDTO medicalShopDTO, List<MultipartFile> multipartFiles) throws IOException {
        MedicalShopEntity old = readRepository.getById(medicalShopDTO.getMedicalShopId());
        if (multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                File file = convertMultipartToFile(multipartFile);
                String fileName = new Date().getTime() + "-" + multipartFile.getOriginalFilename().replace(" ", "_");
                String fileUrl = AmazonKeys.END_POINT_URL + "/" + AmazonKeys.BUCKET_NAME + "/" + fileName;
                s3Client.putObject(new PutObjectRequest(AmazonKeys.BUCKET_NAME, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
                medicalShopDTO.setMedicalShopUrlImage(fileUrl);
            }
        }
        writeRepository.save(medicalShopMapper.updateShop(old, medicalShopMapper.dtoToEntity(findingLatitudeAndLongitude(medicalShopDTO))));
    }

    @Override
    public List<MedicalShopDTO> getNearestShopBasedOnUserLatitudeAndLongitude(UserDTO userDTO) {
        GeocodeService geocodeService = new GeocodeService();
        LatLng latLng = findingUserLatitudeAndLongitude(userDTO);
        Double latitude = latLng.lat;
        Double longitude = latLng.lng;
        List<MedicalShopDTO> medicalShopDTOS = readRepository.findMedicalShopEntitiesByPostCode(reverseGeocodeLatLong(latitude,longitude)).stream()
                .map(medicalShopEntity -> medicalShopMapper.entityToDto(medicalShopEntity))
                .collect(Collectors.toList());
//        get post code by reverse
        reverseGeocodeLatLong(latitude, longitude);
        return geocodeService.findNearestShopBasedOnUserLatitudeAndLongitude(latLng, medicalShopDTOS);
    }

    @Override
    public List<MedicalShopDTO> getAll() {
        return readRepository.findAll().stream()
                .map(medicalShopEntity -> medicalShopMapper.entityToDto(medicalShopEntity))
                .collect(Collectors.toList());
    }

    @Override
    public MedicalShopDTO getMedicalShopById(String medicalShopId) {
        MedicalShopEntity medicalShopEntity = readRepository.getMedicalShopAndGoods(medicalShopId);
        MedicalShopDTO medicalShopDTO = medicalShopMapper.entityToDto(medicalShopEntity);
        medicalShopDTO.setGoodsDTOS(
                goodsReadRepository.findGoodsByMedicalShopId(medicalShopId)
                        .stream().map(goodsEntity -> goodsMapper.entityToDto(goodsEntity))
                        .collect(Collectors.toList()));
        return medicalShopDTO;
    }

    @Override
    public List<MedicalShopDTO> getAllMedicalShopInPostCode(String postCode) {
        return readRepository.findMedicalShopEntitiesByPostCode(postCode).stream()
                .map(medicalShopEntity -> medicalShopMapper.entityToDto(medicalShopEntity))
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalShopDTO> getMedicalShopByUserId(String userId) {
        return readRepository.findMedicalShopEntitiesByUserId(userId).stream()
                .map(medicalShopEntity -> medicalShopMapper.entityToDto(medicalShopEntity))
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalShopDTO> searchMedicalShop(String searchParam) {
        return readRepository.findByMedicalShopNameStartingWith(searchParam)
                .stream()
                .map(medicalShopEntity -> medicalShopMapper.entityToDto(medicalShopEntity))
                .collect(Collectors.toList());
    }

    private MedicalShopDTO findingLatitudeAndLongitude(MedicalShopDTO medicalShopDTO) {
        GeocodeService geocodeService = new GeocodeService();
        GeocodeDTO geocodeDTO = new GeocodeDTO();
        geocodeDTO.setShopName(medicalShopDTO.getMedicalShopName());
        geocodeDTO.setStreetNumber(medicalShopDTO.getStreetNumber());
        geocodeDTO.setPostCode(medicalShopDTO.getPostCode());
        geocodeDTO.setDetailAddress(medicalShopDTO.getDetailAddress());
        LatLng latLng = geocodeService.getLatitudeLongitudeBasedOnDetailsAddress(geocodeDTO);
        medicalShopDTO.setLatitude(latLng.lat);
        medicalShopDTO.setLongitude(latLng.lng);
        return medicalShopDTO;
    }

    private LatLng findingUserLatitudeAndLongitude(UserDTO userDTO) {
        GeocodeService geocodeService = new GeocodeService();
        GeocodeDTO geocodeDTO = new GeocodeDTO();
        geocodeDTO.setDetailAddress(userDTO.getAddress());
        geocodeDTO.setPostCode(userDTO.getPostCode());
        geocodeDTO.setStreetNumber(userDTO.getStreetNumber());
        return geocodeService.getLatitudeLongitudeBasedOnDetailsAddress(geocodeDTO);
    }

    private File convertMultipartToFile(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convertFile);
        fos.write(file.getBytes());
        fos.close();
        return convertFile;
    }

    private String reverseGeocodeLatLong(Double latitude,
                                       Double longitude) {
        GeocodeService geocodeService = new GeocodeService();
        LatLng latLng = new LatLng(latitude, longitude);
        return geocodeService.reverseGeocodeByLatLong(latLng);
    }
}

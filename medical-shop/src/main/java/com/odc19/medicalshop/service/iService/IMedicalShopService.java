package com.odc19.medicalshop.service.iService;

import com.odc19.clients.medicalShop.MedicalShopDTO;
import com.odc19.clients.user.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IMedicalShopService {
    void saveMedicalShop(MedicalShopDTO medicalShopDTO, List<MultipartFile> multipartFiles) throws IOException;
    void updateMedicalShop(MedicalShopDTO medicalShopDTO , List<MultipartFile> multipartFiles) throws IOException;
    List<MedicalShopDTO> getNearestShopBasedOnUserLatitudeAndLongitude(UserDTO userDTO);
    List<MedicalShopDTO> getAll();
    MedicalShopDTO getMedicalShopById(String medicalShopId);
    List<MedicalShopDTO> getAllMedicalShopInPostCode(String postCode);
    List<MedicalShopDTO> getMedicalShopByUserId(String userId);
    List<MedicalShopDTO> searchMedicalShop(String searchParam);
}

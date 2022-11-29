package com.odc19.medicalshop.controller;

import com.odc19.clients.medicalShop.MedicalShopDTO;
import com.odc19.clients.user.UserDTO;
import com.odc19.medicalshop.service.iService.IMedicalShopService;
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

@RestController
@RequestMapping("/api/v1/medical-shop")
public class MedicalShopController {
    @Autowired
    IMedicalShopService medicalShopService;

    @PostMapping(value = "/new", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    void saveNewGoods(@RequestPart MedicalShopDTO medicalShopDTO,
                      @RequestPart(required = false) List<MultipartFile> multipartFiles) throws IOException {
        medicalShopService.saveMedicalShop(medicalShopDTO, multipartFiles);
    }

    @PutMapping(value = "/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void updateMedicalShop(@RequestPart MedicalShopDTO medicalShopDTO,
                                  @RequestPart(required = false) List<MultipartFile> multipartFiles) throws IOException {
        medicalShopService.updateMedicalShop(medicalShopDTO,multipartFiles);
    }

    @GetMapping("/list")
    public List<MedicalShopDTO> getAll() {
        return medicalShopService.getAll();
    }

    @GetMapping("/{medicalShopId}")
    public MedicalShopDTO getMedicalShopById(@PathVariable String medicalShopId) {
        return medicalShopService.getMedicalShopById(medicalShopId);
    }

    @GetMapping("/list/{postCode}")
    public List<MedicalShopDTO> getAllMedicalShopInPostCode(@PathVariable String postCode) {
        return medicalShopService.getAllMedicalShopInPostCode(postCode);
    }

    @PostMapping("/nearest")
    public List<MedicalShopDTO> getNearestMedicalShop(@RequestBody UserDTO userDTO) {
        return medicalShopService.getNearestShopBasedOnUserLatitudeAndLongitude(userDTO);
    }

    @GetMapping("/owner/{userId}")
    public List<MedicalShopDTO> getMedicalShopBasedOnUserId(@PathVariable String userId) {
        return medicalShopService.getMedicalShopByUserId(userId);
    }

    @GetMapping("/search")
    public List<MedicalShopDTO> searchMedicalShop(@RequestParam(name = "searchParam") String searchParam) {
        return medicalShopService.searchMedicalShop(searchParam);
    }
}

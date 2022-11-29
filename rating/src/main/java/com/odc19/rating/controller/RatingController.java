package com.odc19.rating.controller;

import com.odc19.clients.rating.RatingDTO;
import com.odc19.rating.service.RatingService;
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
@RequestMapping("/api/v1/rating")
public class RatingController {
    @Autowired
    RatingService ratingService;

    @GetMapping("/list")
    public List<RatingDTO> listRating() {
        return ratingService.ratingList();
    }

    @GetMapping("/{ratingId}")
    public RatingDTO getRatingById(@PathVariable String ratingId) {
        return ratingService.getRatingById(ratingId);
    }

    @GetMapping("/list/medicalshop/{medicalShopId}")
    public List<RatingDTO> getRatingListByMedicalShopId(@PathVariable String medicalShopId) {
        return ratingService.getRatingListByMedicalShopId(medicalShopId);
    }

    @GetMapping("/list/shipper/{shipperId}")
    public List<RatingDTO> getRatingListByShipperId(@PathVariable String shipperId) {
        return ratingService.getRatingListByShipperId(shipperId);
    }

    @PostMapping(value = "/new", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public RatingDTO saveRating(@RequestPart RatingDTO ratingDTO,
                                @RequestPart(required = false) List<MultipartFile> multipartFiles) throws IOException {
        return ratingService.newRating(ratingDTO, multipartFiles);
    }

    @PutMapping(value = "/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public RatingDTO updateRating(@RequestPart RatingDTO ratingDTO,
                                  @RequestPart(required = false) List<MultipartFile> multipartFiles) throws IOException{
        return ratingService.updateRating(ratingDTO, multipartFiles);
    }
}

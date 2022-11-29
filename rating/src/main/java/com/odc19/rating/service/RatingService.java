package com.odc19.rating.service;

import com.odc19.clients.rating.RatingDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RatingService {
    RatingDTO newRating(RatingDTO ratingDTO, List<MultipartFile> multipartFiles) throws IOException;
    List<RatingDTO> ratingList();
    List<RatingDTO> getRatingListByMedicalShopId(String medicalShopId);
    List<RatingDTO> getRatingListByShipperId(String shipperId);
    RatingDTO updateRating(RatingDTO ratingDTO, List<MultipartFile> multipartFiles) throws IOException;
    RatingDTO getRatingById(String ratingId);
}

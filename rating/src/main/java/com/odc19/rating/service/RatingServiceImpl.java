package com.odc19.rating.service;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.odc19.AmazonCredentials.AmazonKeys;
import com.odc19.clients.rating.RatingDTO;
import com.odc19.rating.entity.RatingEntity;
import com.odc19.rating.mapper.RatingMapper;
import com.odc19.rating.repository.readRepository.ReadRatingRepository;
import com.odc19.rating.repository.writeRepository.WriteRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    ReadRatingRepository readRatingRepository;

    @Autowired
    WriteRatingRepository writeRatingRepository;

    @Autowired
    RatingMapper ratingMapper;

    private AmazonS3 s3Client;

    @PostConstruct
    private void initializeAmazon() {
        this.s3Client = new AmazonS3Client(new BasicAWSCredentials(AmazonKeys.ACCESS_KEY, AmazonKeys.SECRET_KEY));
    }

    @Override
    public RatingDTO newRating(RatingDTO ratingDTO, List<MultipartFile> multipartFiles) throws IOException {
        if (multipartFiles !=null) {
            for (MultipartFile multipartFile : multipartFiles) {
                File file = convertMultipartToFile(multipartFile);
                String fileName = new Date().getTime() + "-" + multipartFile.getOriginalFilename().replace(" ", "_");
                String fileUrl = AmazonKeys.END_POINT_URL + "/" + AmazonKeys.BUCKET_NAME + "/" + fileName;
                s3Client.putObject(new PutObjectRequest(AmazonKeys.BUCKET_NAME, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
                ratingDTO.setFeedbackPictureUrl(fileUrl);
            }
        }
        return ratingMapper.entityToDto(writeRatingRepository.save(ratingMapper.dtoToEntity(ratingDTO))) ;
    }

    @Override
    public RatingDTO updateRating(RatingDTO ratingDTO, List<MultipartFile> multipartFiles) throws IOException {
        RatingEntity oldRating = readRatingRepository.findById(ratingDTO.getRatingId()).orElseThrow(EntityNotFoundException::new);
        if (multipartFiles !=null) {
            for (MultipartFile multipartFile : multipartFiles) {
                File file = convertMultipartToFile(multipartFile);
                String fileName = new Date().getTime() + "-" + multipartFile.getOriginalFilename().replace(" ", "_");
                String fileUrl = AmazonKeys.END_POINT_URL + "/" + AmazonKeys.BUCKET_NAME + "/" + fileName;
                s3Client.putObject(new PutObjectRequest(AmazonKeys.BUCKET_NAME, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
                ratingDTO.setFeedbackPictureUrl(fileUrl);
            }
        }
        return ratingMapper.entityToDto(
                writeRatingRepository.save(
                        ratingMapper.updateRating(oldRating, ratingMapper.dtoToEntity(ratingDTO))));
    }

    @Override
    public List<RatingDTO> ratingList() {
        return readRatingRepository
                .findAll()
                .stream().map(ratingEntity -> ratingMapper.entityToDto(ratingEntity))
                .collect(Collectors.toList());
    }

    @Override
    public List<RatingDTO> getRatingListByMedicalShopId(String medicalShopId) {
        return readRatingRepository
                .findRatingEntitiesByMedicalShopId(medicalShopId)
                .stream().map(ratingEntity -> ratingMapper.entityToDto(ratingEntity))
                .collect(Collectors.toList());
    }

    @Override
    public List<RatingDTO> getRatingListByShipperId(String shipperId) {
        return readRatingRepository
                .findRatingEntitiesByShipperId(shipperId)
                .stream().map(ratingEntity -> ratingMapper.entityToDto(ratingEntity))
                .collect(Collectors.toList());
    }

    @Override
    public RatingDTO getRatingById(String ratingId) {
        return ratingMapper.entityToDto(readRatingRepository.findById(ratingId).orElseThrow(EntityNotFoundException::new)) ;
    }

    private File convertMultipartToFile(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convertFile);
        fos.write(file.getBytes());
        fos.close();
        return convertFile;
    }
}

package com.odc19.medicalshop.service.impl;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.odc19.AmazonCredentials.AmazonKeys;
import com.odc19.clients.medicalShop.GoodsDTO;
import com.odc19.medicalshop.customException.GoodsNameDuplicatedException;
import com.odc19.medicalshop.entity.GoodsEntity;
import com.odc19.medicalshop.mapper.GoodsMapper;
import com.odc19.medicalshop.repository.readRepository.GoodsReadRepository;
import com.odc19.medicalshop.repository.writeRepository.GoodsWriteRepository;
import com.odc19.medicalshop.service.iService.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GoodsService implements IGoodsService {
    @Autowired
    GoodsReadRepository readRepository;

    @Autowired
    GoodsWriteRepository writeRepository;

    @Autowired
    GoodsMapper goodsMapper;

    private AmazonS3 s3Client;

    @PostConstruct
    private void initializeAmazon() {
        this.s3Client = new AmazonS3Client(new BasicAWSCredentials(AmazonKeys.ACCESS_KEY, AmazonKeys.SECRET_KEY));
    }

    @Override
    public List<GoodsDTO> listAllGoods() {
        return readRepository.findAll().stream()
                .map(goodsEntity -> goodsMapper.entityToDto(goodsEntity))
                .collect(Collectors.toList());
    }

    @Override
    public GoodsDTO getGoodsById(String goodsId) {
        return goodsMapper.entityToDto(readRepository.findGoodsById(goodsId));
    }

    @Override
    public List<GoodsDTO> getGoodsListByIds(String goodsIdListString) {
        return null;
    }

    @Override
    public void saveNewGoods(GoodsDTO goodsDTO, List<MultipartFile> multipartFiles) throws IOException {
        if (isGoodsNameUnique(goodsDTO)) {
            GoodsEntity goodsEntity = goodsMapper.dtoToEntity(goodsDTO);
            if (multipartFiles != null) {
                for (MultipartFile multipartFile : multipartFiles) {
                    File file = convertMultipartToFile(multipartFile);
                    String fileName = new Date().getTime() + "-" + multipartFile.getOriginalFilename().replace(" ", "_");
                    String fileUrl = AmazonKeys.END_POINT_URL + "/" + AmazonKeys.BUCKET_NAME + "/" + fileName;
                    s3Client.putObject(new PutObjectRequest(AmazonKeys.BUCKET_NAME, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
                    goodsEntity.setGoodsUrlImage(fileUrl);
                }
            }
            goodsEntity.setMedicalShopId(goodsEntity.getMedicalShopId());
            writeRepository.save(goodsEntity);
        } else {
            throw new GoodsNameDuplicatedException();
        }
    }

    @Override
    public GoodsDTO updateGoods(GoodsDTO goodsDTO, List<MultipartFile> multipartFiles) throws IOException {
        GoodsEntity goodsEntity = readRepository.getById(goodsDTO.getGoodsId());
        if (!goodsDTO.getGoodsName().equals(goodsEntity.getGoodsName())) {
            GoodsEntity newGoodsEntity = goodsMapper.updateGoods(goodsEntity, goodsMapper.dtoToEntity(goodsDTO));
            if (multipartFiles != null) {
                for (MultipartFile multipartFile : multipartFiles) {
                    File file = convertMultipartToFile(multipartFile);
                    String fileName = new Date().getTime() + "-" + multipartFile.getOriginalFilename().replace(" ", "_");
                    String fileUrl = AmazonKeys.END_POINT_URL + "/" + AmazonKeys.BUCKET_NAME + "/" + fileName;
                    s3Client.putObject(new PutObjectRequest(AmazonKeys.BUCKET_NAME, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
                    newGoodsEntity.setGoodsUrlImage(fileUrl);
                }
            }
            return goodsMapper.entityToDto(writeRepository.save(newGoodsEntity));
        } else {
            throw new GoodsNameDuplicatedException();
        }
    }

    @Override
    public void deleteGoods(String goodsId) {
        GoodsEntity goodsEntity = readRepository.getById(goodsId);
        goodsEntity.setDeleted(true);
        writeRepository.save(goodsEntity);
    }

    private boolean isGoodsNameUnique(GoodsDTO goodsDTO) {
        return !readRepository.checkGoodsNameIsExistInMedicalShop(goodsDTO.getMedicalShopId(), goodsDTO.getGoodsName());
    }

    private File convertMultipartToFile(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convertFile);
        fos.write(file.getBytes());
        fos.close();
        return convertFile;
    }
}

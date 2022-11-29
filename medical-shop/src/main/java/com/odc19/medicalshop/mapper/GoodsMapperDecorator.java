package com.odc19.medicalshop.mapper;

import com.odc19.clients.medicalShop.GoodsDTO;
import com.odc19.medicalshop.entity.GoodsEntity;
import com.odc19.medicalshop.repository.readRepository.MedicalShopReadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class GoodsMapperDecorator implements GoodsMapper {
    @Autowired
    @Qualifier("delegate")
    private GoodsMapper delegate;

    @Autowired
    MedicalShopReadRepository readRepository;

    @Override
    public GoodsDTO entityToDto(GoodsEntity goodsEntity) {
        GoodsDTO goodsDTO = delegate.entityToDto(goodsEntity);
        goodsDTO.setMedicalShopId(readRepository.findMedicalShopByIdCustom(goodsEntity.getMedicalShopId()).getMedicalShopId());
        return goodsDTO;
    }

    @Override
    public GoodsEntity dtoToEntity(GoodsDTO goodsDTO) {
        GoodsEntity goodsEntity = delegate.dtoToEntity(goodsDTO);
        goodsEntity.setMedicalShopId(readRepository.findMedicalShopByIdCustom(goodsDTO.getMedicalShopId()).getMedicalShopId());
        return goodsEntity;
    }
}

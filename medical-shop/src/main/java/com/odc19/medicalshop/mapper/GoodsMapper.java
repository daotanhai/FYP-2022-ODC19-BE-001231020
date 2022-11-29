package com.odc19.medicalshop.mapper;

import com.odc19.clients.medicalShop.GoodsDTO;
import com.odc19.medicalshop.entity.GoodsEntity;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
@DecoratedWith(GoodsMapperDecorator.class)
public interface GoodsMapper {
    GoodsDTO entityToDto(GoodsEntity goodsEntity);
    GoodsEntity dtoToEntity(GoodsDTO goodsDTO);
    GoodsEntity updateGoods(@MappingTarget GoodsEntity oldGoods, GoodsEntity newGoods);
}

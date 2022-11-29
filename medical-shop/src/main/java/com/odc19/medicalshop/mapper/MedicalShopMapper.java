package com.odc19.medicalshop.mapper;

import com.odc19.clients.medicalShop.MedicalShopDTO;
import com.odc19.medicalshop.entity.MedicalShopEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MedicalShopMapper {
    MedicalShopEntity dtoToEntity(MedicalShopDTO medicalShopDTO);

    MedicalShopDTO entityToDto(MedicalShopEntity medicalShopEntity);

    @Mapping(target = "userId", ignore = true)
    MedicalShopEntity updateShop(@MappingTarget MedicalShopEntity oldEntity, MedicalShopEntity newEntity);
}

package com.odc19.rating.mapper;

import com.odc19.clients.rating.RatingDTO;
import com.odc19.rating.entity.RatingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RatingMapper {
    RatingEntity dtoToEntity (RatingDTO dto);
    RatingDTO entityToDto (RatingEntity entity);
    @Mapping(target = "ratingId", ignore = true)
    RatingEntity updateRating(@MappingTarget RatingEntity oldEntity, RatingEntity newEntity);
}

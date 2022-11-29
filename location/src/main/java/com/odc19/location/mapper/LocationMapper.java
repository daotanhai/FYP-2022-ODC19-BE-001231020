package com.odc19.location.mapper;

import com.odc19.clients.location.LocationDTO;
import com.odc19.location.entity.LocationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationEntity dtoToEntity(LocationDTO locationDTO);

    LocationDTO entityToDto(LocationEntity locationEntity);
}

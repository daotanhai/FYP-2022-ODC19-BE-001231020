package com.odc19.billing.mapper;

import com.odc19.billing.entity.BillEntity;
import com.odc19.clients.billing.BillDTO;
import com.odc19.clients.billing.BillDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface BillMapper {
    @Mapping(target = "billId", ignore = true)
    BillEntity dtoToEntity(BillDTO billDTO);

    BillDTO entityToDto(BillEntity billEntity);

    @Mappings({
            @Mapping(target = "billId", ignore = true)
    })
    BillEntity updateBillMapper(@MappingTarget BillEntity billOld, BillEntity billNew);

    BillDetailsDTO convertBillDtoToBillDetails(BillDTO billDTO);
}

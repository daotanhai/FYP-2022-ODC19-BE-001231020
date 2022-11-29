package com.odc19.booking.mapper;

import com.odc19.booking.entity.BookingEntity;
import com.odc19.clients.booking.BookingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingEntity dtoToEntity(BookingDTO bookingDTO);
    BookingDTO entityToDto(BookingEntity bookingEntity);
    @Mapping(target = "bookingId", ignore = true)
    BookingEntity updateBooking(@MappingTarget BookingEntity oldBooking, BookingEntity newBooking);
}

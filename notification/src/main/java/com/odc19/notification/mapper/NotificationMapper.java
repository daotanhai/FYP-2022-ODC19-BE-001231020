package com.odc19.notification.mapper;

import com.odc19.clients.notification.NotificationDTO;
import com.odc19.notification.entity.NotificationEntity;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
@DecoratedWith(NotificationMapperDecorator.class)
public interface NotificationMapper {
    NotificationDTO entityToDto(NotificationEntity notification);

    @Mapping(target = "sentAt", ignore = true)
    NotificationEntity dtoToEntity(NotificationDTO dto);
}

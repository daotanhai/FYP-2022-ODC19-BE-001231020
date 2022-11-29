package com.odc19.notification.mapper;

import com.odc19.clients.notification.NotificationDTO;
import com.odc19.notification.entity.NotificationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDateTime;

public abstract class NotificationMapperDecorator implements NotificationMapper {

    @Autowired
    @Qualifier("delegate")
    private NotificationMapper delegate;

    public NotificationEntity dtoToEntity(NotificationDTO dto) {
        NotificationEntity notification = delegate.dtoToEntity(dto);
        notification.setSentAt(LocalDateTime.now());
        return notification;
    }

}

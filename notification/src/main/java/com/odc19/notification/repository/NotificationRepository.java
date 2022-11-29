package com.odc19.notification.repository;

import com.odc19.notification.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, String> {
    List<NotificationEntity> findNotificationEntitiesByToCustomerId(String customerId);
}

package com.odc19.clients.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("notification")
public interface NotificationClient {
    @PostMapping("/api/v1/notification/new")
    void saveNotification(@RequestBody NotificationDTO notificationDTO);

    @GetMapping("/api/v1/notification/list")
    List<NotificationDTO> getAll();
}

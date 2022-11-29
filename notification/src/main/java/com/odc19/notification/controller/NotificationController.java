package com.odc19.notification.controller;

import com.odc19.clients.notification.NotificationDTO;
import com.odc19.notification.service.iService.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    @PostMapping("/new")
    public void saveNotification(@RequestBody NotificationDTO notificationDTO) {
        notificationService.saveNotification(notificationDTO);
    }

    @DeleteMapping("/{notificationId}")
    public void deleteNotification(@PathVariable(name = "notificationId") String notificationId) {
        notificationService.deleteNotificationById(notificationId);
    }

    @GetMapping("/list")
    public List<NotificationDTO> getAll() {
        return notificationService.getAll();
    }

    @GetMapping("/list/{customerId}")
    public List<NotificationDTO> getNotificationByCustomerId(@PathVariable(name = "customerId") String customerId) {
        return notificationService.getNotificationCustomerId(customerId);
    }
}

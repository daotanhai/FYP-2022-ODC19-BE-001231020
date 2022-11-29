package com.odc19.notification.service.iService;

import com.odc19.clients.notification.NotificationDTO;

import java.util.List;

public interface NotificationService {
    void saveNotification(NotificationDTO notificationDTO);

    void deleteNotificationById(String notificationId);

    List<NotificationDTO> getAll();

    List<NotificationDTO> getNotificationCustomerId(String customerId);

    void sendEmailNewUser(NotificationDTO notificationDTO);

    void sendEmailUpdateUser(NotificationDTO notificationDTO);

    void sendEmailChangePassword(NotificationDTO notificationDTO);

    void sendEmailResetPasswordWithToken(NotificationDTO notificationDTO);

    void sendEmailBookingConfirmed(NotificationDTO notificationDTO);

    void sendEmailShipBillToCustomer(NotificationDTO notificationDTO);

    void sendEmailToShipper(NotificationDTO notificationDTO);
}

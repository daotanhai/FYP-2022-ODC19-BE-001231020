package com.odc19.notification.messageQueue.consumermq;

import com.odc19.clients.notification.NotificationDTO;
import com.odc19.notification.service.iService.NotificationService;
import com.odc19.typeofmessage.TypeOfMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationConsumer {

    @Autowired
    NotificationService notificationService;

    //    queues: the queue name of notification
//    this function defined a consumer to take message(s) from the queue
    @RabbitListener(queues = "notification.queue")
    public void consumer(NotificationDTO notificationDTO) {

        if (notificationDTO.getTypeOfMessage() == null) {
            log.info("Failed to consume notification: {}", notificationDTO);
        }
        else {
            try {
                if (notificationDTO.getTypeOfMessage().equals(TypeOfMessage.SAVE_NEW_USER)) {
                    notificationService.sendEmailNewUser(notificationDTO);
                    notificationService.saveNotification(notificationDTO);
                    log.info("Saved notification: {} from queue", notificationDTO);
                }
                if (notificationDTO.getTypeOfMessage().equals(TypeOfMessage.UPDATE_USER)) {
                    notificationService.sendEmailUpdateUser(notificationDTO);
                    notificationService.saveNotification(notificationDTO);
                    log.info("Saved notification: {} from queue", notificationDTO);
                }
                if (notificationDTO.getTypeOfMessage().equals(TypeOfMessage.CHANGE_PASSWORD)) {
                    notificationService.sendEmailChangePassword(notificationDTO);
                    notificationService.saveNotification(notificationDTO);
                    log.info("Saved notification: {} from queue", notificationDTO);
                }
                if (notificationDTO.getTypeOfMessage().equals(TypeOfMessage.GENERATE_RESET_PASSWORD_TOKEN)) {
                    notificationService.sendEmailResetPasswordWithToken(notificationDTO);
                    notificationService.saveNotification(notificationDTO);
                    log.info("Saved notification: {} from queue", notificationDTO);
                }
                if (notificationDTO.getTypeOfMessage().equals(TypeOfMessage.BILL_PAYMENT_CONFIRMED)) {
                    notificationService.sendEmailShipBillToCustomer(notificationDTO);
                    notificationService.saveNotification(notificationDTO);
                    log.info("Saved notification: {} from queue", notificationDTO);
                }
                if (notificationDTO.getTypeOfMessage().equals(TypeOfMessage.BOOKING_CONFIRMED)){
                    notificationService.sendEmailBookingConfirmed(notificationDTO);
                    notificationService.saveNotification(notificationDTO);
                    log.info("Saved notification: {} from queue", notificationDTO);
                }
                if (notificationDTO.getTypeOfMessage().equals(TypeOfMessage.SHIPPER_MAIL_CONFIRMED)) {
                    notificationService.sendEmailToShipper(notificationDTO);
                    notificationService.saveNotification(notificationDTO);
                    log.info("Saved notification: {} from queue", notificationDTO);
                }
            } catch (Exception e) {
                log.info("exception: ",e);
            }
//        notificationService.saveNotification(notificationDTO);
            log.info("Consumed {} from queue", notificationDTO);
        }
    }
}

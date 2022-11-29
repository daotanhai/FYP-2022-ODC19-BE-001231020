package com.odc19.notification.service.impl;

import com.odc19.clients.notification.NotificationDTO;
import com.odc19.notification.entity.NotificationEntity;
import com.odc19.notification.mapper.NotificationMapper;
import com.odc19.notification.repository.NotificationRepository;
import com.odc19.notification.service.iService.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    public void saveNotification(NotificationDTO notificationDTO) {
        notificationRepository.save(notificationMapper.dtoToEntity(notificationDTO));
    }

    @Override
    public void deleteNotificationById(String notificationId) {
        NotificationEntity notification = notificationRepository.findById(notificationId).orElseThrow(EntityNotFoundException::new);
        notification.setDeleted(true);
        notificationRepository.save(notification);
    }

    public List<NotificationDTO> getAll() {
        return notificationRepository.findAll()
                .stream()
                .filter(notificationEntity -> !notificationEntity.isDeleted())
                .map(notificationMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getNotificationCustomerId(String customerId) {
        return notificationRepository.findNotificationEntitiesByToCustomerId(customerId)
                .stream()
                .filter(notificationEntity -> !notificationEntity.isDeleted())
                .map(notificationMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void sendEmailNewUser(NotificationDTO notificationDTO) {
        String subject = "Welcome to Online Doctor for Covid-19 Application!";
        String emailContent = "Dear " + notificationDTO.getToCustomerEmail() + " you have created an account in our service";
        sendMail(notificationDTO.getToCustomerEmail(), emailContent, subject);
    }

    @Override
    public void sendEmailUpdateUser(NotificationDTO notificationDTO) {
        String subject = "Your personal information has been updated!";
        String emailContent = "Dear " + notificationDTO.getToCustomerEmail() + " your information has been updated in our service!";
        sendMail(notificationDTO.getToCustomerEmail(), emailContent, subject);
    }

    @Override
    public void sendEmailChangePassword(NotificationDTO notificationDTO) {
        String subject = "Changed password notification";
        String emailContent = "Dear " + notificationDTO.getToCustomerEmail() + " your password is updated recently.";
        sendMail(notificationDTO.getToCustomerEmail(), emailContent, subject);
    }

    @Override
    public void sendEmailResetPasswordWithToken(NotificationDTO notificationDTO) {
        String subject = "Reset password";
        String emailContent = "Dear " + notificationDTO.getToCustomerEmail() + ", you have been requested to reset your password.\n " +
                "Please click the link below to continue on reset your password!\n" +
                " Link to reset password: http://localhost:8083/api/v1/user/password/reset/" + notificationDTO.getResetPasswordToken();
        sendMail(notificationDTO.getToCustomerEmail(), emailContent, subject);
    }

    @Override
    public void sendEmailBookingConfirmed(NotificationDTO notificationDTO) {
        String subject = "Your booking has been confirmed!";
        String emailContent = "Dear " + notificationDTO.getToCustomerEmail() + " your booking has been confirmed!\n This is your booking details: \n"
                + " \nStart time: " + notificationDTO.getBookingDTO().getStartDate()
                + " \nAt medical shop: " + notificationDTO.getMedicalShopDTO().getMedicalShopName()
                + "\nThanks!";
        sendMail(notificationDTO.getToCustomerEmail(), emailContent, subject);
    }

    @Override
    public void sendEmailShipBillToCustomer(NotificationDTO notificationDTO) {
        String subject = "Payment successfully, your bill will be shipped soon!";
        String emailContent = "Dear " + notificationDTO.getToCustomerEmail()
                + " \nyour bill :" + notificationDTO.getBillDTO().getGoodsIdStringList()
                + " \nwill be shipped by: " + notificationDTO.getShipperDTO().getFullName()
                + " \nwith payment method is: " + notificationDTO.getBillDTO().getPaymentMethod()
                + "\nThanks!";
        sendMail(notificationDTO.getToCustomerEmail(), emailContent, subject);
    }

    @Override
    public void sendEmailToShipper(NotificationDTO notificationDTO) {
        String subject = "You have a bill to ship!";
        String emailContent = "Dear " + notificationDTO.getShipperDTO().getEmail()
                + " \nyou have bill to be shipped from:" + notificationDTO.getMedicalShopDTO().getMedicalShopName()
                + " \nto: " + notificationDTO.getCustomerDTO().getFullName()
                + " \nwith address: " + notificationDTO.getCustomerDTO().getStreetNumber() + " " + notificationDTO.getCustomerDTO().getAddress()
                + " \nwith phone number: " + notificationDTO.getCustomerDTO().getPhoneNumber()
                + " \nwith payment method is: " + notificationDTO.getBillDTO().getPaymentMethod()
                + "\nThanks!";
        sendMail(notificationDTO.getShipperDTO().getEmail(), emailContent, subject);
    }

    private void sendMail(String toUser, String emailContent, String subject) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("passmondongiap@gmail.com");
        mailMessage.setTo(toUser);
        mailMessage.setText(emailContent);
        mailMessage.setSubject(subject);
        try {
            log.info("Email will be sent to: " + toUser + "\n");
            this.javaMailSender.send(mailMessage);
        } catch (MailException e) {
            log.info("There is a problem with mail sending: " + e.getMessage());
        }
    }

}

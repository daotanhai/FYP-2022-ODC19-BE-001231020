package com.odc19.clients.notification;

import com.odc19.clients.BaseDTO;
import com.odc19.clients.billing.BillDTO;
import com.odc19.clients.booking.BookingDTO;
import com.odc19.clients.medicalShop.MedicalShopDTO;
import com.odc19.clients.user.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NotificationDTO extends BaseDTO implements Serializable {
    private String notificationId;
    private String toCustomerId;
    private String toCustomerEmail;
    private String sender;
    private LocalDateTime sentAt;
    private String message;
    private String typeOfMessage;
    private String resetPasswordToken;
    private BillDTO billDTO;
    private BookingDTO bookingDTO;
    private UserDTO shipperDTO;
    private UserDTO customerDTO;
    private MedicalShopDTO medicalShopDTO;
}

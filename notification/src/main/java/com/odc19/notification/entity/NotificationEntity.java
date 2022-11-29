package com.odc19.notification.entity;

import com.odc19.baseEntity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "notification")
public class NotificationEntity extends BaseEntity {
    @Id
    @GenericGenerator(name = "sequence_notification_id",
            parameters = @Parameter(name = "prefix", value = "notification"),
            strategy = "com.odc19.baseEntity.CustomIdGenerator.CustomIdGenerator")
    @GeneratedValue(generator = "sequence_notification_id")
    @Column(name = "notification_id")
    private String notificationId;
    private String toCustomerId;
    private String toCustomerEmail;
    private String sender;
    private String message;
    private String typeOfMessage;
    private LocalDateTime sentAt;
}

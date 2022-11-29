package com.odc19.booking.service.impl;

import com.odc19.amqp.RabbitMQMessageProducer;
import com.odc19.booking.customException.DuplicatedBookingException;
import com.odc19.booking.entity.BookingEntity;
import com.odc19.booking.mapper.BookingMapper;
import com.odc19.booking.repository.readRepository.BookingReadRepository;
import com.odc19.booking.repository.writeRepository.BookingWriteRepository;
import com.odc19.booking.service.iService.BookingService;
import com.odc19.clients.booking.BookingDTO;
import com.odc19.clients.medicalShop.MedicalShopClient;
import com.odc19.clients.medicalShop.MedicalShopDTO;
import com.odc19.clients.notification.NotificationDTO;
import com.odc19.clients.user.UserClient;
import com.odc19.clients.user.UserDTO;
import com.odc19.typeofmessage.TypeOfMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    BookingReadRepository bookingReadRepository;

    @Autowired
    BookingWriteRepository bookingWriteRepository;

    @Autowired
    BookingMapper bookingMapper;

    @Autowired
    RabbitMQMessageProducer rabbitMQMessageProducer;

    @Autowired
    UserClient userClient;

    @Autowired
    MedicalShopClient medicalShopClient;

    @Override
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        if (checkIsDuplicatedBookingTimeInSameShop(bookingDTO)) {
            BookingEntity bookingEntity = bookingMapper.dtoToEntity(bookingDTO);
            bookingEntity.setConfirmed(false);
            bookingWriteRepository.save(bookingEntity);
            return bookingMapper.entityToDto(bookingEntity);
        }
        else {
            throw new DuplicatedBookingException();
        }
    }

    @Override
    public BookingDTO updateBooking(BookingDTO bookingDTO) {
        if (checkIsDuplicatedBookingTimeInSameShop(bookingDTO)) {
            BookingEntity oldBooking = bookingReadRepository.findById(bookingDTO.getBookingId()).orElseThrow(EntityNotFoundException::new);
            BookingEntity newBooking = bookingMapper.updateBooking(oldBooking, bookingMapper.dtoToEntity(bookingDTO));
            if (newBooking.isConfirmed()) {
                UserDTO userDTO = userClient.getUserById(bookingDTO.getCustomerId());
                MedicalShopDTO medicalShopDTO = medicalShopClient.getMedicalShopById(bookingDTO.getMedicalShopId());
//                send email to user with details schedule
                publishMessageToQueue(sendBookingNotification(bookingDTO,userDTO, medicalShopDTO));
            }
            return bookingMapper.entityToDto(bookingWriteRepository.save(newBooking));
        }
        else {
            throw new DuplicatedBookingException();
        }
    }

    @Override
    public List<BookingDTO> getBookingList() {
        return bookingReadRepository
                .findAll()
                .stream()
                .map(bookingEntity -> bookingMapper.entityToDto(bookingEntity))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingListByCustomerId(String customerId) {
        return bookingReadRepository
                .findBookingEntitiesByCustomerId(customerId)
                .stream()
                .map(bookingEntity -> bookingMapper.entityToDto(bookingEntity))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingListByMedicalShopId(String medicalShopId) {
        return bookingReadRepository
                .findBookingEntitiesByMedicalShopId(medicalShopId)
                .stream()
                .map(bookingEntity -> bookingMapper.entityToDto(bookingEntity))
                .collect(Collectors.toList());
    }

    @Override
    public BookingDTO getById(String bookingId) {
        return bookingMapper.entityToDto(bookingReadRepository.findById(bookingId).orElseThrow(EntityNotFoundException::new));
    }

    private boolean checkIsDuplicatedBookingTimeInSameShop(BookingDTO bookingDTO) {
        BookingEntity bookingEntity = bookingReadRepository.findBookingEntityByCustomerIdAndMedicalShopIdAndStartDate(bookingDTO.getCustomerId(), bookingDTO.getMedicalShopId(), bookingDTO.getStartDate());
        return bookingEntity == null;
    };

    //    function to publish message to notification.queue
    private void publishMessageToQueue(Object payloadParam) {
        rabbitMQMessageProducer.publish(payloadParam, "internal.exchange", "internal.notification.routing-key");
    };

    private NotificationDTO sendBookingNotification(BookingDTO bookingDTO, UserDTO userDTO, MedicalShopDTO medicalShopDTO) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setToCustomerEmail(userDTO.getEmail());
        notificationDTO.setToCustomerId(userDTO.getUserId());
        notificationDTO.setBookingDTO(bookingDTO);
        notificationDTO.setTypeOfMessage(TypeOfMessage.BOOKING_CONFIRMED);
        notificationDTO.setMedicalShopDTO(medicalShopDTO);
        notificationDTO.setMessage("Booking confirmed by "+medicalShopDTO.getMedicalShopName());
        notificationDTO.setSender("System");
        return notificationDTO;
    }
}

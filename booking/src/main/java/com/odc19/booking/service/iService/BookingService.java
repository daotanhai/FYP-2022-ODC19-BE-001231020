package com.odc19.booking.service.iService;

import com.odc19.clients.booking.BookingDTO;

import java.util.List;

public interface BookingService {
    BookingDTO createBooking(BookingDTO bookingDTO);
    BookingDTO updateBooking(BookingDTO bookingDTO);
    List<BookingDTO> getBookingList();
    List<BookingDTO> getBookingListByCustomerId(String customerId);
    List<BookingDTO> getBookingListByMedicalShopId(String medicalShopId);
    BookingDTO getById(String bookingId);
}

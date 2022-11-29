package com.odc19.booking.controller;

import com.odc19.booking.service.iService.BookingService;
import com.odc19.clients.booking.BookingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {
    @Autowired
    BookingService bookingService;

    @PostMapping(value = "/new")
    BookingDTO saveBooking(@RequestBody BookingDTO bookingDTO) {
        return bookingService.createBooking(bookingDTO);
    }

    @GetMapping("/list")
    List<BookingDTO> getAll() {
        return bookingService.getBookingList();
    }

    @PutMapping("/update")
    BookingDTO updateBooking(@RequestBody BookingDTO bookingDTO) {
        return bookingService.updateBooking(bookingDTO);
    }

    @GetMapping("/list/customer/{customerId}")
    List<BookingDTO> getBookingListByCustomerId(@PathVariable String customerId) {
        return bookingService.getBookingListByCustomerId(customerId);
    }

    @GetMapping("/list/medicalshop/{medicalShopId}")
    List<BookingDTO> getBookingListByMedicalShopId(@PathVariable String medicalShopId) {
        return bookingService.getBookingListByMedicalShopId(medicalShopId);
    }

    @GetMapping("/{bookingId}")
    BookingDTO getBookingById(@PathVariable String bookingId) {
        return bookingService.getById(bookingId);
    }
}

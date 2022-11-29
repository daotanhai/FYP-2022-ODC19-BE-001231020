package com.odc19.booking.customException;

import com.odc19.booking.errorMessageConstant.ErrorMessageConstant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.IM_USED, reason = ErrorMessageConstant.BOOKING_DUPLICATED)
public class DuplicatedBookingException extends RuntimeException{
}

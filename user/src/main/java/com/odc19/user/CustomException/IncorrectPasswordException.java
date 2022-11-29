package com.odc19.user.CustomException;

import com.odc19.user.Constant.ErrorMessageConstant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = ErrorMessageConstant.INCORRECT_PASSWORD)
public class IncorrectPasswordException extends RuntimeException {
}

package com.odc19.user.CustomException;

import com.odc19.user.Constant.ErrorMessageConstant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.IM_USED, reason = ErrorMessageConstant.DUPLICATED_USERNAME)
public class DuplicatedUsernameException extends RuntimeException {
}

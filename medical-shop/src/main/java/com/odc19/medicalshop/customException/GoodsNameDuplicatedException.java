package com.odc19.medicalshop.customException;

import com.odc19.medicalshop.ErrorMessageConstant.ErrorMessageConstant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.IM_USED, reason = ErrorMessageConstant.GOODS_NAME_DUPLICATED)
public class GoodsNameDuplicatedException extends RuntimeException{
}

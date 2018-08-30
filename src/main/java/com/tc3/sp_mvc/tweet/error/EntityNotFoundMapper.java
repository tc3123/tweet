package com.tc3.sp_mvc.tweet.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class EntityNotFoundMapper {


    @ExceptionHandler(EntityNotFoundException.class)
    //异常与HttpStatus关联
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Entity could not be found")
    public void handleNotFound() {

    }
}

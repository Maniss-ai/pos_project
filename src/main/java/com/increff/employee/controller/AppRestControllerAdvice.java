package com.increff.employee.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.increff.employee.model.data.MessageData;
import com.increff.employee.service.ApiException;
import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;


@RestControllerAdvice
public class AppRestControllerAdvice {

    @ExceptionHandler(ApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageData handle(ApiException e) {
        MessageData data = new MessageData();
        data.setMessage(e.getMessage());
        return data;
    }

    @ExceptionHandler(TypeMismatchException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageData handleTypeMismatch(TypeMismatchException e) {
        MessageData data = new MessageData();
        data.setMessage("Invalid input. Data types mismatch");
        return data;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageConversionException.class)
    public MessageData handleException(HttpMessageConversionException e) {
        MessageData data = new MessageData();
        data.setMessage("Invalid input. Data types mismatch");
        return data;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageData handle(Throwable e) {
        MessageData data = new MessageData();
        data.setMessage("An unknown error has occurred this time - " + e.getMessage());
        return data;
    }
}
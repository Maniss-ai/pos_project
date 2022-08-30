package com.increff.employee.controller;

import com.increff.employee.model.data.ErrorData;
import com.increff.employee.service.ApiException;
import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.*;


@RestControllerAdvice
public class AppRestControllerAdvice {

    @ExceptionHandler(ApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorData handle(ApiException e) {
        ErrorData data = new ErrorData();
        data.setMessage(e.getMessage());
        return data;
    }

    @ExceptionHandler(TypeMismatchException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorData handleTypeMismatch(TypeMismatchException e) {
        ErrorData data = new ErrorData();
        data.setMessage("Invalid input. Data types mismatch");
        return data;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageConversionException.class)
    public ErrorData handleException(HttpMessageConversionException e) {
        ErrorData data = new ErrorData();
        data.setMessage("Invalid input. Data types mismatch");
        return data;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorData handle(Throwable e) {
        ErrorData data = new ErrorData();
        data.setMessage("An unknown error has occurred this time - " + e.getMessage());
        return data;
    }

}
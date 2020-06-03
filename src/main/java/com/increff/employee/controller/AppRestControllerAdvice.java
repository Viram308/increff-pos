package com.increff.employee.controller;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.increff.employee.model.MessageData;
import com.increff.employee.service.ApiException;

@RestControllerAdvice
public class AppRestControllerAdvice {

	// Handles ApiException class
	@ExceptionHandler(ApiException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public MessageData handle(ApiException e) {
		MessageData data = new MessageData();
		data.setMessage(e.getMessage());
		return data;
	}

	// Handles exception for foreign key constraints
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public MessageData handle(ConstraintViolationException e) {
		MessageData data = new MessageData();
		data.setMessage("Can not perform delete because given key is present in another table");
		return data;
	}

}
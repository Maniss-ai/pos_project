package com.increff.employee.model.data;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class InfoData {

	final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss z");
	private String message;

	public InfoData() {
		message = "Activity time: " + ZonedDateTime.now().format(formatter);
	}
}

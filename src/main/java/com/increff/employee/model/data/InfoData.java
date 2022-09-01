package com.increff.employee.model.data;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class InfoData {

	private String message;

	public InfoData() {
		message = "Activity time: " + ZonedDateTime.now();
	}
}

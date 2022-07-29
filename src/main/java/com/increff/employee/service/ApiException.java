package com.increff.employee.service;

import java.io.PrintStream;
import java.util.List;

public class ApiException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ApiException(String string) {
		super(string);
	}

	@Override
	public void printStackTrace(PrintStream var1) {
		var1.println(this.getMessage());
	}

}

package com.infosys.bank.bankrestfulwebservices;

public class BankAccountNotFoundException extends RuntimeException {

	public BankAccountNotFoundException(String message) {
		super(message);
	}
}

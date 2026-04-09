package com.practice.test.Infrastructure.Exceptions;

import org.springframework.http.HttpStatus;

public class AddressExistsException extends CodedException {
    public AddressExistsException() {
        super("Mobile number already has an address in this street", HttpStatus.CONFLICT);
    }
}
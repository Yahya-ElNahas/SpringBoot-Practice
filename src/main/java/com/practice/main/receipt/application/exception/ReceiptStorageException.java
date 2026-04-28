package com.practice.main.receipt.application.exception;

import com.practice.main.common.exception.DomainException;
import org.springframework.http.HttpStatus;

public class ReceiptStorageException extends DomainException {
    public ReceiptStorageException() {
        super("exception.receipt_storage", null, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}

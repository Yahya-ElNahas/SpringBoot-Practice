package com.practice.main.receipt.application.exception;

import com.practice.main.common.exception.DomainException;
import org.springframework.http.HttpStatus;

public class ReceiptNotFoundException extends DomainException {
    public ReceiptNotFoundException() {
        super("exception.receipt_not_found", null, "RECEIPT", "001", "RECEIPT_NOT_FOUND");
    }
}
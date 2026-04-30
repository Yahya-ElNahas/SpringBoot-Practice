package com.practice.main.receipt.application.exception;

import com.practice.main.common.exception.DomainException;

public class ReceiptStorageException extends DomainException {
    public ReceiptStorageException() {
        super("exception.receipt_storage", null, "RECEIPT", "002", "ERROR_STORING_RECEIPT");
    }
}
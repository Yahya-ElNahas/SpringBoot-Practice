package com.practice.main.order.application.exception;

import com.practice.main.common.exception.DomainException;
import org.springframework.http.HttpStatus;

public class StockUpdateException extends DomainException {
    public StockUpdateException() {
        super("exception.stock_update", null, HttpStatus.BAD_REQUEST.value());
    }
}

package com.finsure.exception;

public class InsufficientRepaymentAmountException extends RuntimeException {

    public InsufficientRepaymentAmountException(String message) {
        super(message);
    }
}
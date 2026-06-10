package com.finsure.exception;

public class InvalidRepaymentIdException extends RuntimeException {
    public InvalidRepaymentIdException(String message) { super(message); }
}
package com.finsure.exception;

public class InvalidLoanIdException extends RuntimeException {
    public InvalidLoanIdException(String message) { super(message); }
}
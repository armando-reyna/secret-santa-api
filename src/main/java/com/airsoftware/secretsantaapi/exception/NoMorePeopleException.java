package com.airsoftware.secretsantaapi.exception;

public class NoMorePeopleException extends Exception {
    public NoMorePeopleException(String errorMessage) {
        super(errorMessage);
    }
}

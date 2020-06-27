package com.dramancompany.taxiServiceBe.common;

public class DuplicateUsernameException extends RuntimeException {

    public DuplicateUsernameException(String errorMessage) {
        this(errorMessage, null);
    }

    public DuplicateUsernameException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}

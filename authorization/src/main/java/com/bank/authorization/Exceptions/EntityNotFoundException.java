package com.bank.authorization.Exceptions;

public class EntityNotFoundException extends IllegalArgumentException {
    public EntityNotFoundException(String s) {
        super(s);
    }
}

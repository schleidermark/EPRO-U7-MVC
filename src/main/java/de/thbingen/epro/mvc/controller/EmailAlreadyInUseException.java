package de.thbingen.epro.mvc.controller;

public class EmailAlreadyInUseException extends RuntimeException {

    public EmailAlreadyInUseException() {
        super("The selected email-address is already in use.");
    }

    public EmailAlreadyInUseException (String email) {
        super(String.format("The selected email-address '%s' is already in use.", email));
    }
}

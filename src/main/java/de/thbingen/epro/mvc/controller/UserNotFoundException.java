package de.thbingen.epro.mvc.controller;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("The requested user cannot be found.");
    }

    public UserNotFoundException(String name) {
        super(String.format("The user with name '%s' cannot be found.", name));
    }

    public UserNotFoundException(Long userId) {
        super(String.format("The user with Id '%d' cannot be found.", userId));
    }

}

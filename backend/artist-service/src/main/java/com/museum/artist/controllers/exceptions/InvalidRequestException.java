package com.museum.artist.controllers.exceptions;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(error(message));
    }

    private static String error(String message) {
        return message;
    }
}

package com.hutchison.swandraft.exception;

public class NotEnoughPlayersException extends RuntimeException{
    public NotEnoughPlayersException() {
        super("Not enough players to create tournament snapshot.");
    }
}

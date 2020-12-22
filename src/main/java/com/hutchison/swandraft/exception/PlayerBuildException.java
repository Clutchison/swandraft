package com.hutchison.swandraft.exception;

import java.util.List;

public class PlayerBuildException extends RuntimeException {

    public PlayerBuildException(List<String> errors) {
        super("Error creating player: \n" +
                (errors == null ? "" : String.join("\n", errors)));
    }
}

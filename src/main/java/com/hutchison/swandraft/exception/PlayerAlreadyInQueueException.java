package com.hutchison.swandraft.exception;

import com.hutchison.swandraft.model.player.Player;

public class PlayerAlreadyInQueueException extends IllegalArgumentException {
    public PlayerAlreadyInQueueException(Player player) {
        super(player.getIdentifier() + " has already entered.");
    }
}

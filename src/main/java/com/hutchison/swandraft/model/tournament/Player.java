package com.hutchison.swandraft.model.tournament;

import com.hutchison.swandraft.exception.PlayerBuildException;
import com.hutchison.swandraft.model.entity.PlayerEntity;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Value
@Builder(toBuilder = true)
public class Player implements Serializable {

    // Player info
    Long playerId;
    Long discordId;
    String name;
    Integer discriminator;

    // Tournament info
    int points;
    int startingPosition;
    boolean receivedBye;
    boolean reportedThisRound;
    Player currentOpponent;
    Set<Player> previousOpponents;

    public static Player initialize(PlayerEntity pe, int startingPosition) {
        return Player.builder()
                .playerId(pe.getPlayerId())
                .discordId(pe.getDiscordId())
                .name(pe.getName())
                .discriminator(pe.getDiscriminator())
                .points(0)
                .startingPosition(startingPosition)
                .receivedBye(false)
                .reportedThisRound(false)
                .currentOpponent(null)
                .previousOpponents(Set.of())
                .build();
    }

    public static class PlayerBuilder {

        private int pointsToAdd = 0;

        public Player build() {
            List<String> validationErrors = getValidationErrors();
            if (validationErrors.size() > 0) throw new PlayerBuildException(validationErrors);
            return new Player(
                    playerId,
                    discordId,
                    name,
                    discriminator,
                    points + pointsToAdd,
                    startingPosition,
                    receivedBye,
                    reportedThisRound,
                    currentOpponent,
                    previousOpponents
            );
        }

        private List<String> getValidationErrors() {
            List<String> errors = new ArrayList<>();
            if (playerId == null) errors.add("PlayerId must not be null.");
            if (discordId == null) errors.add("DiscordId must not be null.");
            if (points + pointsToAdd < 0) errors.add("Point total cannot be less than zero.");
            if (startingPosition < 0) errors.add("Starting position cannot be less than zero.");
            return errors;
        }

        public PlayerBuilder plusPoints(int pointsToAdd) {
            this.pointsToAdd = pointsToAdd;
            return this;
        }
    }
}

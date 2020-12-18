package com.hutchison.swandraft.model;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Value
@Builder(toBuilder = true)
public class Player {
    Long playerRecordId;
    String discordId;
    int points;
    int startingPosition;
    boolean hasReceivedBye;
    boolean hasReportedThisRound;
    Player currentOpponent;
    Set<Player> previousOpponents;

    public static Player initialize(PlayerRecord playerRecord, int startingPosition, Player currentOpponent) {
        return new Player(
                playerRecord.getPlayerRecordId(),
                playerRecord.getDiscordId(),
                0,
                startingPosition,
                false,
                false,
                currentOpponent,
                Collections.unmodifiableSet(new HashSet<>())
        );
    }

    public static class PlayerBuilder {

        private int pointsToAdd = 0;

        public Player build() {
            return new Player(
                    playerRecordId,
                    discordId,
                    points + pointsToAdd,
                    startingPosition,
                    hasReceivedBye,
                    hasReportedThisRound,
                    currentOpponent,
                    previousOpponents
            );
        }

        public PlayerBuilder plusPoints(int pointsToAdd) {
            this.pointsToAdd = pointsToAdd;
            return this;
        }
    }
}

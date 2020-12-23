package com.hutchison.swandraft.model.tournament;

import com.hutchison.swandraft.model.dto.Result;
import lombok.Builder;
import lombok.Value;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Value
@Builder(toBuilder = true)
public class TournamentSnapshot implements Serializable {
    Map<Long, Player> players;
    int currentRound;
    String message;

    private TournamentSnapshot(Map<Long, Player> players, int currentRound, String message) throws IllegalArgumentException {
        this.players = players;
        this.currentRound = currentRound;
        this.message = message == null ? "" : message;
        validate();
    }

    public TournamentSnapshot report(Set<Result> results) {
        String errorMessage = assertPlayersHaveNotReported(results);
        if (StringUtils.hasText(errorMessage)) return TournamentSnapshot.builder()
                .message(errorMessage)
                .build();

        Map<Long, Player> players = new HashMap<>(Map.copyOf(this.players));
        results.forEach(result ->
                players.put(result.getDiscordId(), addPointsToPlayer(result.getDiscordId(), result.getPoints())));

        String updateMessage = results.stream()
                .map(result -> result.getDiscordId() + " gained " + result.getPoints() + " points")
                .collect(Collectors.joining("; ")) + ".";

        return this.toBuilder()
                .players(players)
                .message(updateMessage)
                .build();
    }

    private void validate() throws IllegalArgumentException {
        List<String> errs = new ArrayList<>();
        if (players == null || players.size() < 6) errs.add("Player count must be at least six.");
        if (currentRound < 1) errs.add("Current Round must be greater than 0");
        if (errs.size() > 0) {
            IllegalStateException ex = new IllegalStateException();
            errs.forEach(e -> ex.addSuppressed(new IllegalStateException(e)));
            throw ex;
        }
    }

    private String assertPlayersHaveNotReported(Set<Result> results) {
        return results.stream()
                .filter(result -> players.get(result.getDiscordId()).isReportedThisRound())
                .map(discordId -> discordId + " has already reported this round.")
                .collect(Collectors.joining("; "));
    }

    private Player addPointsToPlayer(Long discordId, int pointsToAdd) {
        return players.get(discordId).toBuilder()
                .plusPoints(pointsToAdd)
                .reportedThisRound(true)
                .build();
    }

    public TournamentSnapshot advance() {
        Set<String> playersNeedingToReport = players.values().stream()
                .filter(Player::isReportedThisRound)
                .map(Player::getName)
                .collect(Collectors.toSet());
        if (playersNeedingToReport.size() > 0)
            return this.toBuilder()
                    .message("These players still need to report: " + String.join("; " + playersNeedingToReport))
                    .build();

        return this.toBuilder()
                .players(pairPlayers())
                .currentRound(currentRound + 1)
                .message("Round " + currentRound + 1 + "!")
                .build();
    }

    private Map<Long, Player> pairPlayers() {
        return this.players.values().stream()
                .map(op -> op.toBuilder()
                        .reportedThisRound(false)
                        .previousOpponents(Stream.concat(
                                Set.copyOf(op.getPreviousOpponents()).stream(),
                                Stream.of(op.getCurrentOpponent()))
                                .collect(Collectors.toSet()))
                        .currentOpponent(null)
                        .build())
                .collect(Collectors.toMap(
                        Player::getDiscordId,
                        p -> p
                ));
    }
}

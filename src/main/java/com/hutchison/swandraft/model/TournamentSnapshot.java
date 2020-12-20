package com.hutchison.swandraft.model;

import com.hutchison.swandraft.model.dto.Result;
import lombok.Builder;
import lombok.Value;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Value
@Builder(toBuilder = true)
public class TournamentSnapshot implements Serializable {
    Map<String, Player> players;
    int currentRound;
    String message;

    public TournamentSnapshot(Map<String, Player> players, int currentRound, String message) {
        this.players = players == null ? new HashMap<>() : players;
        this.currentRound = currentRound;
        this.message = message;
    }

    public TournamentSnapshot report(Set<Result> results) {
        String errorMessage = assertPlayersHaveNotReported(results);
        if (StringUtils.hasText(errorMessage)) return TournamentSnapshot.builder()
                .message(errorMessage)
                .build();

        Map<String, Player> players = new HashMap<>(Map.copyOf(this.players));
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

    private String assertPlayersHaveNotReported(Set<Result> results) {
        return results.stream()
                .filter(result -> players.get(result.getDiscordId()).isReportedThisRound())
                .map(discordId -> discordId + " has already reported this round.")
                .collect(Collectors.joining("; "));
    }

    private Player addPointsToPlayer(String steamId, int pointsToAdd) {
        return players.get(steamId).toBuilder()
                .plusPoints(pointsToAdd)
                .reportedThisRound(true)
                .build();
    }

    public TournamentSnapshot advance() {
        Set<String> playersNeedingToReport = players.values().stream()
                .filter(Player::isReportedThisRound)
                .map(Player::getDiscordId)
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

    private Map<String, Player> pairPlayers() {
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

package com.hutchison.swandraft.model;

import lombok.Builder;
import lombok.Value;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Value
@Builder(toBuilder = true)
public class TournamentSnapshot {
    Map<String, Player> players;
    int currentRound;
    String updateMessage;

    public TournamentSnapshot(Map<String, Player> players, int currentRound, String updateMessage) {
        this.players = players == null ? new HashMap<>() : players;
        this.currentRound = currentRound;
        this.updateMessage = updateMessage;
    }

    public TournamentSnapshot report(Set<Result>results) {
        String errorMessage = assertPlayersHaveNotReported(results);
        if (StringUtils.hasText(errorMessage)) return TournamentSnapshot.builder()
                .updateMessage(errorMessage)
                .build();

        Map<String, Player> players = new HashMap<>(Map.copyOf(this.players));
        results.forEach(result ->
                players.put(result.getDiscordId(), addPointsToPlayer(result.getDiscordId(), result.getPoints())));

        String updateMessage = results.stream()
                .map(result -> result.getDiscordId() + " gained " + result.getPoints() + " points")
                .collect(Collectors.joining("; ")) + ".";

        return this.toBuilder()
                .players(players)
                .updateMessage(updateMessage)
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
}

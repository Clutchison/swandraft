package com.hutchison.swandraft.model;

import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.hutchison.swandraft.model.SeedingStyle.CROSS;

@Value
@Builder(toBuilder = true)
public class Tournament {
    List<TournamentSnapshot> snapshots;
    SeedingStyle seedingStyle;
    int totalRounds;
    Set<PlayerRecord> playerRecords;

    public Tournament(SeedingStyle seedingStyle, int totalRounds, Set<PlayerRecord> playerRecords) {
        this.snapshots = new ArrayList<>();
        this.seedingStyle = seedingStyle;
        this.totalRounds = totalRounds;
        this.playerRecords = Collections.unmodifiableSet(playerRecords);
    }

    public String report(Map<String, Integer> playerPointMap) {
        TournamentSnapshot latestSnapshot = snapshots.get(snapshots.size() - 1);

        Map<String, Player> players = new HashMap<>(Map.copyOf(latestSnapshot.getPlayers()));
        playerPointMap.forEach((discordId, player) ->
                players.put(discordId, addPointsToPlayer(latestSnapshot, discordId, player)));

        String updateMessage = playerPointMap.entrySet().stream()
                .map(es -> es.getKey() + " gained " + es.getValue() + " points")
                .collect(Collectors.joining("; ")) + ".";

        snapshots.add(latestSnapshot.toBuilder()
                .players(players)
                .updateMessage(updateMessage)
                .build());

        return updateMessage;
    }

    private Player addPointsToPlayer(TournamentSnapshot snapshot, String steamId, int pointsToAdd) {
        return snapshot.getPlayers().get(steamId).toBuilder()
                .plusPoints(pointsToAdd)
                .build();
    }

    public static class TournamentBuilder {

        private static final SeedingStyle DEFAULT_SEEDING_STYLE = CROSS;

        public Tournament build() {
            if (playerRecords == null || playerRecords.size() < 6)
                throw new RuntimeException("Error creating tournament, not enough players given.");
            return new Tournament(
                    seedingStyle == null ? DEFAULT_SEEDING_STYLE : seedingStyle,
                    calculateTotalRounds(playerRecords.size()),
                    playerRecords
            );
        }

        private int calculateTotalRounds(int playerCount) {
            return (int) Math.ceil(Math.log(playerCount) / Math.log(2));
        }
    }
}

package com.hutchison.swandraft.model;

import lombok.Builder;
import lombok.Value;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.hutchison.swandraft.model.SeedingStyle.CROSS;

@Value
@Builder(toBuilder = true)
public class Tournament {
    List<TournamentSnapshot> snapshots;
    SeedingStyle seedingStyle;
    int totalRounds;
    Set<PlayerRecord> playerRecords;

    public Tournament(TournamentSnapshot initialSnapshot, SeedingStyle seedingStyle, int totalRounds, Set<PlayerRecord> playerRecords) {
        this.snapshots = new ArrayList<>();
        snapshots.add(initialSnapshot);
        this.seedingStyle = seedingStyle;
        this.totalRounds = totalRounds;
        this.playerRecords = Collections.unmodifiableSet(playerRecords);
    }

    public String report(Map<String, Integer> playerPointMap) {
        TournamentSnapshot latestSnapshot = snapshots.get(snapshots.size() - 1);

        String errorMessage = assertPlayersHaveNotReported(playerPointMap, latestSnapshot);
        if (StringUtils.hasText(errorMessage)) return errorMessage;

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

    private String assertPlayersHaveNotReported(Map<String, Integer> playerPointMap, TournamentSnapshot latestSnapshot) {
        return playerPointMap.keySet().stream()
                .filter(discordId -> latestSnapshot.getPlayers().get(discordId).isReportedThisRound())
                .map(discordId -> discordId + " has already reported this round.")
                .collect(Collectors.joining("; "));
    }

    private Player addPointsToPlayer(TournamentSnapshot snapshot, String steamId, int pointsToAdd) {
        return snapshot.getPlayers().get(steamId).toBuilder()
                .plusPoints(pointsToAdd)
                .reportedThisRound(true)
                .build();
    }

    public static class TournamentBuilder {

        private static final SeedingStyle DEFAULT_SEEDING_STYLE = CROSS;

        public Tournament build() {
            if (playerRecords == null || playerRecords.size() < 6)
                throw new RuntimeException("Error creating tournament, not enough players given.");
            return new Tournament(
                    buildInitialSnapshot(),
                    seedingStyle == null ? DEFAULT_SEEDING_STYLE : seedingStyle,
                    totalRounds == 0 ? calculateTotalRounds(playerRecords.size()) : totalRounds,
                    playerRecords
            );
        }

        private TournamentSnapshot buildInitialSnapshot() {
            Set<Player> players;
            switch (seedingStyle == null ? DEFAULT_SEEDING_STYLE : seedingStyle) {
                case CROSS:
                    players = buildCrossPairings();
                    break;
                case RANDOM:
                    players = buildRandomPairings();
                    break;
                case ADJACENT:
                case SLAUGHTER:
                default:
                    throw new RuntimeException("Unsupported Seeding Style: " + seedingStyle);
            }
            return TournamentSnapshot.builder()
                    .currentRound(0)
                    .players(players.stream().collect(Collectors.toMap(Player::getDiscordId, p -> p)))
                    .updateMessage("Initialized tournament.")
                    .build();
        }

        private Set<Player> buildCrossPairings() {
            List<PlayerRecord> tempRecords = new ArrayList<>(playerRecords);
            List<PlayerRecord> prs = new ArrayList<>();
            IntStream.range(0, tempRecords.size() / 2)
                    .forEach(i -> {
                        prs.add(tempRecords.get(i));
                        prs.add(tempRecords.get(i + tempRecords.size() / 2));
                    });
            if (prs.size() % 2 == 1) prs.add(tempRecords.get(tempRecords.size() - 1));
            return buildPairings(prs);
        }

        private Set<Player> buildRandomPairings() {
            List<PlayerRecord> prs = new ArrayList<>(playerRecords);
            Collections.shuffle(prs);
            return buildPairings(prs);
        }

        private Set<Player> buildPairings(List<PlayerRecord> prs) {
            Set<Player> players = new HashSet<>();
            IntStream.range(0, prs.size())
                    .filter(i -> i % 2 == 0)
                    .forEach(i -> {
                        Player p1 = Player.initialize(prs.get(i), i);
                        Player p2 = Player.initialize(prs.get(i + 1), i + 1);
                        players.add(p1.toBuilder().currentOpponent(p2).build());
                        players.add(p2.toBuilder().currentOpponent(p1).build());
                    });

            // Give bye to remaining player.
            if (prs.size() % 2 == 1) players.add(
                    Player.initialize(prs.get(prs.size() - 1), prs.size() - 1).toBuilder()
                            .plusPoints(3)
                            .reportedThisRound(true)
                            .receivedBye(true)
                            .build()
            );

            return players;
        }

        private int calculateTotalRounds(int playerCount) {
            return (int) Math.ceil(Math.log(playerCount) / Math.log(2));
        }
    }
}

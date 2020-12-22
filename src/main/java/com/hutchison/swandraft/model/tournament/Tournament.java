package com.hutchison.swandraft.model.tournament;

import com.hutchison.swandraft.model.Player;
import com.hutchison.swandraft.model.SeedingStyle;
import com.hutchison.swandraft.model.dto.Result;
import com.hutchison.swandraft.model.entity.PlayerEntity;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.hutchison.swandraft.model.SeedingStyle.CROSS;
import static com.hutchison.swandraft.model.tournament.Pairing.buildCrossPairings;
import static com.hutchison.swandraft.model.tournament.Pairing.buildRandomPairings;

@Value
@Builder(toBuilder = true)
public class Tournament {
    UUID tournamentUuid;
    SeedingStyle seedingStyle;
    int totalRounds;
    Set<PlayerEntity> playerEntities;
    List<TournamentSnapshot> snapshots;

    static final SeedingStyle DEFAULT_SEEDING_STYLE = CROSS;

    // For deserialization
    Tournament(
            @NonNull UUID tournamentUuid,
            @NonNull SeedingStyle seedingStyle,
            int totalRounds,
            @NonNull Set<PlayerEntity> playerEntities,
            @NonNull List<TournamentSnapshot> snapshots
    ) {
        this.tournamentUuid = tournamentUuid;
        this.seedingStyle = seedingStyle;
        this.totalRounds = totalRounds;
        this.playerEntities = Collections.unmodifiableSet(playerEntities);
        this.snapshots = snapshots;
    }

    // Brand new tournament
    Tournament(
            SeedingStyle seedingStyle,
            int totalRounds,
            TournamentSnapshot initialSnapshot,
            Set<PlayerEntity> playerEntities) {
        this.tournamentUuid = UUID.randomUUID();
        this.snapshots = new ArrayList<>();
        snapshots.add(initialSnapshot);
        this.seedingStyle = seedingStyle;
        this.totalRounds = totalRounds;
        this.playerEntities = Collections.unmodifiableSet(playerEntities);
    }

    public String report(Set<Result> results) {
        TournamentSnapshot snapshot = getLatestSnapshot().report(results);
        snapshots.add(snapshot);
        return snapshot.getMessage();
    }

    public String getMessage() {
        return getLatestSnapshot().getMessage();
    }

    public String advance() {
        TournamentSnapshot snapshot = getLatestSnapshot().advance();
        snapshots.add(snapshot);
        return snapshot.getMessage();
    }

    public static Tournament create(@NonNull Set<PlayerEntity> playerEntities) {
        return create(playerEntities, DEFAULT_SEEDING_STYLE, calculateTotalRounds(playerEntities.size()));
    }

    public static Tournament create(@NonNull Set<PlayerEntity> playerEntities, @NonNull SeedingStyle seedingStyle) {
        return create(playerEntities, seedingStyle, calculateTotalRounds(playerEntities.size()));
    }

    public static Tournament create(@NonNull Set<PlayerEntity> playerEntities, @NonNull Integer totalRounds) {
        return create(playerEntities, DEFAULT_SEEDING_STYLE, totalRounds);
    }

    public static Tournament create(@NonNull Set<PlayerEntity> playerEntities,
                                    @NonNull SeedingStyle seedingStyle,
                                    @NonNull Integer totalRounds) {
        return new Tournament(
                seedingStyle,
                totalRounds,
                buildInitialSnapshot(seedingStyle, playerEntities),
                playerEntities
        );
    }

    private TournamentSnapshot getLatestSnapshot() {
        return snapshots.get(snapshots.size() - 1);
    }

    private static TournamentSnapshot buildInitialSnapshot(SeedingStyle seedingStyle, Set<PlayerEntity> playerEntities) {
        Set<Player> players;
        switch (seedingStyle == null ? DEFAULT_SEEDING_STYLE : seedingStyle) {
            case CROSS:
                players = buildCrossPairings(playerEntities);
                break;
            case RANDOM:
                players = buildRandomPairings(playerEntities);
                break;
            case ADJACENT:
            case SLAUGHTER:
            default:
                throw new RuntimeException("Unsupported Seeding Style: " + seedingStyle);
        }
        return TournamentSnapshot.builder()
                .currentRound(0)
                .players(players.stream().collect(Collectors.toMap(Player::getDiscordId, p -> p)))
                .message("Initialized tournament.")
                .build();
    }

    private static int calculateTotalRounds(int playerCount) {
        return (int) Math.ceil(Math.log(playerCount) / Math.log(2));
    }
}

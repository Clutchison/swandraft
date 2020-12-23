package com.hutchison.swandraft.model.tournament;

import com.hutchison.swandraft.model.dto.Result;
import com.hutchison.swandraft.model.entity.PlayerEntity;
import com.hutchison.swandraft.model.tournament.pairing.Pairing;
import com.hutchison.swandraft.model.tournament.pairing.SeedingStyle;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.hutchison.swandraft.model.tournament.pairing.SeedingStyle.CROSS;

@Value
@Builder(toBuilder = true)
public class Tournament {
    Long tournamentId;
    SeedingStyle seedingStyle;
    int totalRounds;
    Set<PlayerEntity> playerEntities;
    List<TournamentSnapshot> snapshots;

    static final SeedingStyle DEFAULT_SEEDING_STYLE = CROSS;

    // For deserialization
    Tournament(
            Long tournamentId,
            @NonNull SeedingStyle seedingStyle,
            int totalRounds,
            @NonNull Set<PlayerEntity> playerEntities,
            @NonNull List<TournamentSnapshot> snapshots
    ) {
        this.tournamentId = tournamentId;
        this.seedingStyle = seedingStyle;
        this.totalRounds = totalRounds;
        this.playerEntities = Collections.unmodifiableSet(playerEntities);
        this.snapshots = snapshots;
        validate();
    }

    // Brand new tournament
    private Tournament(
            @NonNull SeedingStyle seedingStyle,
            int totalRounds,
            @NonNull TournamentSnapshot initialSnapshot,
            @NonNull Set<PlayerEntity> playerEntities) {
        this.snapshots = new ArrayList<>();
        snapshots.add(initialSnapshot);
        this.seedingStyle = seedingStyle;
        this.totalRounds = totalRounds;
        this.playerEntities = Collections.unmodifiableSet(playerEntities);
        validate();
    }

    private void validate() {

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
//        validate(playerEntities, seedingStyle, totalRounds);
        return new Tournament(
                seedingStyle,
                totalRounds,
                buildInitialSnapshot(seedingStyle, playerEntities),
                playerEntities
        );
    }

    private static void validate(Set<PlayerEntity> playerEntities, SeedingStyle seedingStyle, Integer totalRounds) {
        List<String> errors = new ArrayList<>();
        if (playerEntities == null || playerEntities.size() < 6) errors.add("Not enough players.");
        if (seedingStyle == null) errors.add("No seeding style provided.");
        if (totalRounds == null || totalRounds <= 0) errors.add("Total rounds must be a positive number.");
        if (errors.size() > 0) throw new RuntimeException("Error creating tournament: \n- " +
                String.join("\n- ", errors));
    }

    private TournamentSnapshot getLatestSnapshot() {
        return snapshots.get(snapshots.size() - 1);
    }

    private static TournamentSnapshot buildInitialSnapshot(SeedingStyle seedingStyle,
                                                           Set<PlayerEntity> playerEntities) {
        return TournamentSnapshot.builder()
                .currentRound(0)
                .players(initializePlayers(seedingStyle, playerEntities))
                .message("Initialized tournament.")
                .build();
    }

    private static Map<Long, Player> initializePlayers(SeedingStyle seedingStyle,
                                                       Set<PlayerEntity> playerEntities) {
        return Pairing.initial(seedingStyle, playerEntities).stream()
                .collect(Collectors.toMap(
                        Player::getDiscordId,
                        p -> p
                ));
    }

    private static int calculateTotalRounds(int playerCount) {
        return (int) Math.ceil(Math.log(playerCount) / Math.log(2));
    }
}

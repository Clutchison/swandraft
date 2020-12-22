package com.hutchison.swandraft.model.tournament;

import com.hutchison.swandraft.model.entity.PlayerEntity;
import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Value
@Builder(toBuilder = true)
public class TournamentQueue {
    Map<Long, PlayerEntity> playerRecords;
    String message;

    public TournamentQueue() {
        playerRecords = Collections.emptyMap();
        message = "Initialized. No records provided.";
    }

    public TournamentQueue(Map<Long, PlayerEntity> prs, String message) {
        this.playerRecords = Collections.unmodifiableMap(prs);
        this.message = message;
    }

    public TournamentQueue(PlayerEntity pr) {
        this(Set.of(pr));
    }

    public TournamentQueue(Set<PlayerEntity> playerEntities) {
        this.playerRecords = Collections.unmodifiableMap(playerEntities.stream()
                .collect(Collectors.toMap(
                        PlayerEntity::getDiscordId,
                        pr -> pr
                )));
        message = "Initialized from existing records.";
    }

    public TournamentQueue add(PlayerEntity playerEntity) {
        if (playerRecords.get(playerEntity.getDiscordId()) != null) return this.toBuilder()
                .message(playerEntity.getDiscordId() + " is already entered.")
                .build();

        return this.toBuilder()
                .playerRecords(
                        Collections.unmodifiableMap(Stream.concat(
                                Map.copyOf(this.playerRecords).values().stream(),
                                Stream.of(playerEntity))
                                .collect(Collectors.toMap(
                                        PlayerEntity::getDiscordId,
                                        pr -> pr
                                ))))
                .message(playerEntity.getDiscordId() + " successfully entered.")
                .build();
    }

    public Set<PlayerEntity> getPlayerRecords() {
        return Set.copyOf(this.playerRecords.values());
    }
}

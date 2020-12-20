package com.hutchison.swandraft.model;

import com.hutchison.swandraft.model.entity.PlayerRecord;
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
    Map<String, PlayerRecord> playerRecords;
    String message;

    public TournamentQueue() {
        playerRecords = Collections.emptyMap();
        message = "Initialized. No records provided.";
    }

    public TournamentQueue(Map<String, PlayerRecord> prs, String message) {
        this.playerRecords = Collections.unmodifiableMap(prs);
        this.message = message;
    }

    public TournamentQueue(PlayerRecord pr) {
        this(Set.of(pr));
    }

    public TournamentQueue(Set<PlayerRecord> playerRecords) {
        this.playerRecords = Collections.unmodifiableMap(playerRecords.stream()
                .collect(Collectors.toMap(
                        PlayerRecord::getDiscordId,
                        pr -> pr
                )));
        message = "Initialized from existing records.";
    }

    public TournamentQueue add(PlayerRecord playerRecord) {
        if (playerRecords.get(playerRecord.getDiscordId()) != null) return this.toBuilder()
                .message(playerRecord.getDiscordId() + " is already entered.")
                .build();

        return this.toBuilder()
                .playerRecords(
                        Collections.unmodifiableMap(Stream.concat(
                                Map.copyOf(this.playerRecords).values().stream(),
                                Stream.of(playerRecord))
                                .collect(Collectors.toMap(
                                        PlayerRecord::getDiscordId,
                                        pr -> pr
                                ))))
                .message(playerRecord.getDiscordId() + " successfully entered.")
                .build();
    }

    public Set<PlayerRecord> getPlayerRecords() {
        return Set.copyOf(this.playerRecords.values());
    }
}

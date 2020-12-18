package com.hutchison.swandraft.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "player_record")
@Table(name = "player_record")
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class PlayerRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, name = "player_record_id")
    Long playerRecordId;

    @Column(name = "name", unique = false, nullable = false)
    String name;

    @Column(name = "discord_id", unique = true, nullable = false)
    String discordId;

    @Column(name = "games_played", unique = true, nullable = false)
    int gamesPlayed;

    @Column(name = "total_score", unique = true, nullable = false)
    int totalScore;

    public PlayerRecord(Long playerRecordId,
                        String name,
                        String discordId,
                        int gamesPlayed,
                        int totalScore) {
        this.playerRecordId = playerRecordId;
        this.name = name;
        this.discordId = discordId;
        this.gamesPlayed = gamesPlayed;
        this.totalScore = totalScore;
    }

    public PlayerRecord() {

    }
}

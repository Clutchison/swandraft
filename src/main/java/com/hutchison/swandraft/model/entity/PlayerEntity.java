package com.hutchison.swandraft.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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
@AllArgsConstructor
@NoArgsConstructor
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, name = "player_id")
    Long playerId;

    @Column(name = "name", unique = false, nullable = false)
    String name;

    @Column(name = "discriminator", unique = false, nullable = false)
    Integer discriminator;

    @Column(name = "discord_id", unique = true, nullable = false)
    Long discordId;

    @Column(name = "games_played", unique = true, nullable = false)
    int gamesPlayed;

    @Column(name = "total_score", unique = true, nullable = false)
    int totalScore;

    public static PlayerEntity create(@NonNull Long discordId, @NonNull String name, @NonNull Integer discriminator) {
        return new PlayerEntity(
                null,
                name,
                discriminator,
                discordId,
                0,
                0
        );
    }
}

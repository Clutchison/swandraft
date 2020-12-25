package com.hutchison.swandraft.model.entity;

import com.hutchison.swandraft.model.player.Player;
import com.hutchison.swandraft.model.player.PlayerIdentifier;
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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity(name = "player")
@Table(name = "player",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "discriminator"})
        })
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "player_id")
    Long playerId;

    @Column(name = "name", unique = false, nullable = false)
    String name;

    @Column(name = "discriminator", unique = false, nullable = false)
    @Min(1000) @Max(9999)
    Integer discriminator;

    @Column(name = "discord_id", unique = true, nullable = false)
    Long discordId;

    @Column(name = "games_played", unique = false, nullable = false)
    int gamesPlayed;

    @Column(name = "total_score", unique = false, nullable = false)
    int totalScore;

    @Column(name = "total_trophies", unique = false, nullable = false)
    int totalTrophies;

//    @Column(name = "tournament_queue_id")
//    Long queueId;
//
//    @Transient
//    static Map<Player, PlayerEntity> entityCache = new WeakHashMap<>();
//    @Transient
//    static Map<PlayerEntity, Player> entityCache = new WeakHashMap<>();

    public static PlayerEntity create(PlayerIdentifier playerIdentifier) {
        return create(playerIdentifier.getDiscordId(), playerIdentifier.getName(), playerIdentifier.getDiscriminator());
    }

    public static PlayerEntity create(@NonNull Long discordId, @NonNull String name, @NonNull Integer discriminator) {
        return new PlayerEntity(
                null,
                name,
                discriminator,
                discordId,
                0,
                0,
                0
        );
    }

    public static PlayerEntity fromPlayer(Player p) {
        return new PlayerEntity(
                p.getPlayerId(),
                p.getName(),
                p.getDiscriminator(),
                p.getDiscordId(),
                p.getGamesPlayed(),
                p.getTotalScore(),
                p.getTotalTrophies());
    }

    public Player toPlayer() {
        return Player.builder()
                .playerId(playerId)
                .name(name)
                .discriminator(discriminator)
                .discordId(discordId)
                .gamesPlayed(gamesPlayed)
                .totalScore(totalScore)
                .totalTrophies(totalTrophies)
                .build();
    }
}

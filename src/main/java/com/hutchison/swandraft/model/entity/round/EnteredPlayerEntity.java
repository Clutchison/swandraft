package com.hutchison.swandraft.model.entity.round;


import com.hutchison.swandraft.model.tournament.round.pairing.EnteredPlayer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity(name = "entered_player")
@Table(name = "entered_player")
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnteredPlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, name = "entered_player_id")
    Long enteredPlayerId;

    @Column(unique = false, nullable = false, name = "player_id")
    Long playerId;

    @Column(unique = false, nullable = false, name = "discord_id")
    Long discordId;

    @Column(unique = false, nullable = false, name = "name")
    String name;

    @Column(unique = false, nullable = false, name = "discriminator")
    Integer discriminator;

    @Column(unique = false, nullable = false, name = "points")
    Integer points;

    @Column(unique = false, nullable = false, name = "starting_position")
    Integer startingPosition;

    @Column(unique = false, nullable = false, name = "received_bye")
    Boolean receivedBye;

    @ManyToMany
//    @JoinColumn(name = "entered_player_id", referencedColumnName = "entered_player_id")
    @JoinTable(
            name = "entered_player_opponents",
            joinColumns = @JoinColumn(name = "player_id", referencedColumnName = "entered_player_id"),
            inverseJoinColumns = @JoinColumn(name = "opponent_id", referencedColumnName = "entered_player_id"))
    Set<EnteredPlayerEntity> previousOpponents;
}

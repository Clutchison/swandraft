package com.hutchison.swandraft.model.entity.round;

import com.hutchison.swandraft.model.tournament.round.pairing.Pairing;
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

@Entity(name = "round")
@Table(name = "round")
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoundEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, name = "round_id")
    Long roundId;

    @Column(unique = false, nullable = false, name = "round_number")
    Integer roundNumber;

    @ManyToMany
    @JoinTable(
            name = "round_players",
            joinColumns = @JoinColumn(name = "round_id", referencedColumnName = "round_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id", referencedColumnName = "entered_player_id"))
    Set<EnteredPlayerEntity> enteredPlayers;

    @OneToMany
    @JoinColumn(name="round_id", referencedColumnName="round_id")
    Set<PairingEntity> pairings;

    @OneToMany
    @JoinColumn(name="round_id", referencedColumnName="round_id")
    Set<ResultEntity> results;

    @Column(unique = false, nullable = false, name = "open")
    Boolean open;
}

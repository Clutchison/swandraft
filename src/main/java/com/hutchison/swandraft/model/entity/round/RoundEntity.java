package com.hutchison.swandraft.model.entity.round;

import com.hutchison.swandraft.model.tournament.round.ClosedRound;
import com.hutchison.swandraft.model.tournament.round.OpenRound;
import com.hutchison.swandraft.model.tournament.round.Round;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
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
import java.util.stream.Collectors;

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
    @JoinColumn(name = "round_id", referencedColumnName = "round_id")
    Set<PairingEntity> pairings;

    @OneToMany
    @JoinColumn(name = "round_id", referencedColumnName = "round_id")
    Set<ResultEntity> results;

    @Column(unique = false, nullable = false, name = "open")
    Boolean open;

    private Round.RoundBuilder getRoundBuilder() {
        return Round.builder()
                .roundId(roundId)
                .roundNumber(roundNumber)
                .enteredPlayers(enteredPlayers.stream().map(EnteredPlayerEntity::toEnteredPlayer).collect(Collectors.toSet()))
                .pairings(pairings.stream().map(PairingEntity::toPairing).collect(Collectors.toSet()))
                .results(this.results.stream().map(ResultEntity::toResult).collect(Collectors.toSet()));
    }

    public ClosedRound toClosedRound() {
        if (getOpen()) throw new IllegalStateException("Attempted to cast open round entity to closed round.");
        return getRoundBuilder().buildClosedRound();
    }

    public OpenRound toOpenRound() {
        if (!getOpen()) throw new IllegalStateException("Attempted to cast closed round entity to open round.");
        return getRoundBuilder().buildOpenRound();
    }
}

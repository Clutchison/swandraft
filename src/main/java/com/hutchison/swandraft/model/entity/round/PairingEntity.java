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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(name = "pairing")
@Table(name = "pairing",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"opponent_id", "player_id", "round_number"})
        })
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PairingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, name = "pairing_id")
    Long pairingId;

    @Column(unique = false, nullable = false, name = "round_number")
    Integer roundNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    EnteredPlayerEntity player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opponent_id")
    EnteredPlayerEntity opponent;

    @Column(unique = false, nullable = false, name = "bye")
    Boolean bye;

    static final Map<PairingEntity, Pairing> cache = new HashMap<>();

    public Pairing toPairing() {
        if (cache.get(this) == null) {
            Pairing p = Pairing.builder()
                    .pairingId(pairingId)
                    .roundNumber(roundNumber)
                    .player(player.toEnteredPlayer())
                    .opponent(opponent.toEnteredPlayer())
                    .bye(bye)
                    .build();
            cache.put(this, p);
            return p;
        } else {
            return cache.get(this);
        }
    }

    public static List<Pairing> toPairingList(Set<PairingEntity> pairingEntities) {
        return pairingEntities.stream()
                .map(PairingEntity::toPairing)
                .sorted(Comparator.comparingInt(Pairing::getRoundNumber))
                .collect(Collectors.toList());
    }
}

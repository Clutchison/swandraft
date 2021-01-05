package com.hutchison.swandraft.model.entity.round;


import com.hutchison.swandraft.model.entity.PlayerEntity;
import com.hutchison.swandraft.model.tournament.round.pairing.EnteredPlayer;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

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

    @ManyToOne(optional = false)
    @JoinColumn(name = "player_id")
    PlayerEntity player;

    @Column(unique = false, nullable = false, name = "points")
    Integer points;

    @Column(unique = false, nullable = false, name = "starting_position")
    Integer startingPosition;

    @Column(unique = false, nullable = false, name = "received_bye")
    Boolean receivedBye;

    @OneToMany(mappedBy = "player")
    Set<PairingEntity> pairings;

    static final Map<EnteredPlayerEntity, EnteredPlayer> cache = new HashMap<>();

    public EnteredPlayer toEnteredPlayer() {
        if (cache.get(this) == null) {
            EnteredPlayer ep = EnteredPlayer.builder()
                    .enteredPlayerId(enteredPlayerId)
                    .player(player.toPlayer())
                    .points(points)
                    .startingPosition(startingPosition)
                    .receivedBye(receivedBye)
                    .pairings(PairingEntity.toPairingList(pairings))
                    .build();
            cache.put(this, ep);
            return ep;
        } else {
            return cache.get(this);
        }
    }
}

package com.hutchison.swandraft.model.entity.round;

import com.hutchison.swandraft.model.tournament.round.ClosedRound;
import com.hutchison.swandraft.model.tournament.round.OpenRound;
import com.hutchison.swandraft.model.tournament.round.Round;
import com.hutchison.swandraft.model.tournament.round.Rounds;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(name = "rounds")
@Table(name = "rounds")
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoundsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, name = "rounds_id")
    Long roundsId;

    @OneToMany
    @JoinColumn(name = "rounds_id", referencedColumnName = "rounds_id")
    Set<RoundEntity> rounds;

    public Rounds toRounds() {
        Map<Boolean, List<RoundEntity>> filteredRounds = rounds.stream()
                .collect(Collectors.groupingBy(RoundEntity::getOpen));
        validate(filteredRounds);

        List<ClosedRound> closedRounds = filteredRounds.get(false) == null ? new ArrayList<>() :
                filteredRounds.get(false).stream()
                        .map(RoundEntity::toClosedRound)
                        .sorted(Comparator.comparingInt(ClosedRound::getRoundNumber))
                        .collect(Collectors.toList());

        OpenRound openRound = filteredRounds.get(true).get(0).toOpenRound();

        return Rounds.builder()
                .roundsId(roundsId)
                .closedRounds(closedRounds)
                .openRound(openRound)
                .build();
    }

    private void validate(Map<Boolean, List<RoundEntity>> filteredRounds) {
        if (filteredRounds.get(true) == null || filteredRounds.get(true).size() == 0)
            throw new RuntimeException("Rounds does not contain an open round.");
        if (filteredRounds.get(true) != null && filteredRounds.get(true).size() > 1)
            throw new RuntimeException("Multiple open rounds found for Rounds: " + roundsId);
    }
}

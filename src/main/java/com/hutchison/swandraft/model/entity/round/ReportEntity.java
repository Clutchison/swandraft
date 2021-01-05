package com.hutchison.swandraft.model.entity.round;

import com.hutchison.swandraft.model.tournament.round.pairing.EnteredPlayer;
import com.hutchison.swandraft.model.tournament.round.pairing.Pairing;
import com.hutchison.swandraft.model.tournament.round.result.Report;
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
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Entity(name = "reports")
@Table(name = "reports")
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, name = "report_id")
    Long reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    EnteredPlayerEntity enteredPlayer;

    @Column(unique = false, nullable = false, name = "rounds_won")
    Integer roundsWon;

    @Column(unique = false, nullable = false, name = "rounds_lost")
    Integer roundsLost;

    @Column(unique = false, nullable = false, name = "rounds_drawn")
    Integer roundsDrawn;

    public static Map<EnteredPlayer, Optional<Report>> toReportMap(Pairing pairing,
                                                                   Set<ReportEntity> reports) {
        pairing.getPlayers().stream()
                .map()
    }

    public Report toReport() {
        return null;
    }
}

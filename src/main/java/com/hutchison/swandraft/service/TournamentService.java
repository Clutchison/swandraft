package com.hutchison.swandraft.service;

import com.hutchison.swandraft.exception.PlayerAlreadyInQueueException;
import com.hutchison.swandraft.model.dto.enter.EnterResponse;
import com.hutchison.swandraft.model.dto.report.ReportDto;
import com.hutchison.swandraft.model.dto.report.ReportResponse;
import com.hutchison.swandraft.model.entity.PlayerEntity;
import com.hutchison.swandraft.model.entity.TournamentEntity;
import com.hutchison.swandraft.model.entity.TournamentQueueEntity;
import com.hutchison.swandraft.model.player.Player;
import com.hutchison.swandraft.model.player.PlayerIdentifier;
import com.hutchison.swandraft.model.tournament.Tournament;
import com.hutchison.swandraft.model.tournament.TournamentQueue;
import com.hutchison.swandraft.model.tournament.round.pairing.EnteredPlayer;
import com.hutchison.swandraft.model.tournament.round.pairing.SeedingStyle;
import com.hutchison.swandraft.model.tournament.round.result.Report;
import com.hutchison.swandraft.model.tournament.round.result.ResultState;
import com.hutchison.swandraft.repository.PlayerRepository;
import com.hutchison.swandraft.repository.TournamentEntityRepository;
import com.hutchison.swandraft.repository.TournamentQueueRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.hutchison.swandraft.model.tournament.round.result.ResultState.ERROR;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TournamentService {

    SeedingStyle seedingStyle;
    Integer totalRounds;

    final PlayerRepository playerRepository;
    final TournamentQueueRepository queueRepository;
    final TournamentEntityRepository tournamentEntityRepository;

    @Autowired
    public TournamentService(PlayerRepository playerRepository,
                             TournamentQueueRepository queueRepository,
                             TournamentEntityRepository tournamentEntityRepository) {
        this.playerRepository = playerRepository;
        this.queueRepository = queueRepository;
        this.tournamentEntityRepository = tournamentEntityRepository;
    }

    public EnterResponse enter(PlayerIdentifier playerIdentifier) {
        Player player;
        try {
            player = playerRepository.findByDiscordId(playerIdentifier.getDiscordId())
                    .orElseGet(() -> playerRepository.save(PlayerEntity.create(playerIdentifier)))
                    .toPlayer();
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return EnterResponse.error("Error creating player: " + playerIdentifier.toString());
        }
        TournamentQueueEntity queueEntity = queueRepository.findLatestTournamentQueueEntity().orElseGet(() ->
                queueRepository.save(new TournamentQueueEntity()));
        TournamentQueue queue;
        try {
            queue = queueEntity.toTournamentQueue().add(player);
        } catch (PlayerAlreadyInQueueException e) {
            e.printStackTrace();
            return EnterResponse.error(player, e.getMessage());
        }
        queueRepository.save(TournamentQueueEntity.fromTournamentQueue(queue));
        return EnterResponse.ok(player);
    }

    public String configure(SeedingStyle seedingStyle, Integer totalRounds) {
        throw new UnsupportedOperationException("Not yet implemented.");
//        this.seedingStyle = seedingStyle == null ? this.seedingStyle : seedingStyle;
//        this.totalRounds = totalRounds == null ? this.totalRounds : totalRounds;
//        return "SeedingStyle: " + seedingStyle + ", " + "Total Rounds: " + totalRounds;
    }

    public String start(Boolean forceRestart) {
//        if (this.tournament != null && !forceRestart) return "Can't start, tournament already in progress!";
//        if (queue == null || queue.getPlayers().size() < 6) return "Can't start, player count is less than six";
//        this.tournament = Tournament.builder()
//                .players(queue.getPlayers())
//                .seedingStyle(seedingStyle)
//                .totalRounds(totalRounds)
//                .build();
//        return tournament.getMessage();
        throw new NotYetImplementedException();
    }

    public String recover() {
//        Optional<TournamentEntity> te = tournamentEntityRepository.findLatestTournament();
//        if (te.isEmpty()) return "Error retrieving latest tournament";
//        this.tournament = te.get().toTournament();
//        return "Successfully recovered. Last update: " + this.tournament.getMessage();
        throw new NotYetImplementedException();
    }

    public String advance() {
//        return tournament.advance();
        throw new NotYetImplementedException();
    }

    public ReportResponse report(ReportDto reportDto) {
        Tournament tournament = tournamentEntityRepository.findLatestTournament()
                .orElseThrow(() -> new IllegalStateException("Failed to find running tournament."))
                .toTournament();

        EnteredPlayer enteredPlayer = tournament.getRounds()
                .getOpenRound()
                .getEnteredPlayers()
                .get(reportDto.getDiscordId());

        Report report = new Report(enteredPlayer, reportDto);
        Optional<Pair<Tournament, ResultState>> newTournament = tournament.report(report);
        if (newTournament.isPresent()) {
            if (!newTournament.get().getFirst().equals(tournament)) {
                TournamentEntity te = TournamentEntity.fromTournament(newTournament.get().getFirst());
                tournamentEntityRepository.save(te);
            }
            return ReportResponse.fromState(newTournament.get().getSecond());
        } else {
            return ReportResponse.fromState(ERROR);
        }
    }

    public String end() {
        throw new NotYetImplementedException();
    }
}

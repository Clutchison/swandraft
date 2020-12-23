package com.hutchison.swandraft.service;

import com.hutchison.swandraft.model.tournament.pairing.SeedingStyle;
import com.hutchison.swandraft.model.tournament.Tournament;
import com.hutchison.swandraft.model.tournament.TournamentQueue;
import com.hutchison.swandraft.model.dto.Result;
import com.hutchison.swandraft.model.entity.PlayerEntity;
import com.hutchison.swandraft.model.entity.TournamentEntity;
import com.hutchison.swandraft.repository.PlayerRecordRepository;
import com.hutchison.swandraft.repository.TournamentEntityRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TournamentService {

    TournamentQueue queue;
    Tournament tournament;
    SeedingStyle seedingStyle;
    Integer totalRounds;

    final PlayerRecordRepository playerRecordRepository;
    final TournamentEntityRepository tournamentEntityRepository;

    @Autowired
    public TournamentService(PlayerRecordRepository playerRecordRepository,
                             TournamentEntityRepository tournamentEntityRepository) {
        this.playerRecordRepository = playerRecordRepository;
        this.tournamentEntityRepository = tournamentEntityRepository;
    }

    public String enter(Long discordId, String name, Integer discriminator) {
        PlayerEntity pr = playerRecordRepository.findByDiscordId(discordId)
                .orElseGet(() -> playerRecordRepository.save(PlayerEntity.create(discordId, name, discriminator)));
        queue = queue == null ?
                new TournamentQueue(pr) :
                queue.add(pr);
        return queue.getMessage();
    }

    public String configure(SeedingStyle seedingStyle, Integer totalRounds) {
        this.seedingStyle = seedingStyle == null ? this.seedingStyle : seedingStyle;
        this.totalRounds = totalRounds == null ? this.totalRounds : totalRounds;
        return "SeedingStyle: " + seedingStyle + ", " + "Total Rounds: " + totalRounds;
    }

    public String start(Boolean forceRestart) {
        if (this.tournament != null && !forceRestart) return "Can't start, tournament already in progress!";
        if (queue == null || queue.getPlayerRecords().size() < 6) return "Can't start, player count is less than six";
        this.tournament = Tournament.builder()
                .playerEntities(queue.getPlayerRecords())
                .seedingStyle(seedingStyle)
                .totalRounds(totalRounds)
                .build();
        return tournament.getMessage();
    }

    public String recover() {
        Optional<TournamentEntity> te = tournamentEntityRepository.findLatestTournament();
        if (te.isEmpty()) return "Error retrieving latest tournament";
        this.tournament = te.get().toTournament();
        return "Successfully recovered. Last update: " + this.tournament.getMessage();
    }

    public String advance() {
        return tournament.advance();
    }

    public String report(Set<Result> results) {
        return tournament.report(results);
    }

    public String end() {
        throw new UnsupportedOperationException();
    }
}

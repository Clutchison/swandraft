package com.hutchison.swandraft.service;

import com.hutchison.swandraft.model.entity.PlayerEntity;
import com.hutchison.swandraft.model.dto.Result;
import com.hutchison.swandraft.model.tournament.Tournament;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.hutchison.swandraft.model.SeedingStyle.CROSS;

@Service
public class TestService {

    public Tournament testTournament() {
        return createDummyTournament(8);
    }

    public Tournament testReport() {
        Tournament t = createDummyTournament(8);
        Set<Result> results = Set.of(
                new Result(1111L, 3),
                new Result(2222L, 1)
        );
        t.report(results);
        return t;
    }

    private Tournament createDummyTournament(int count) {
        return Tournament.builder()
                .playerEntities(createDummyPrs(count))
                .seedingStyle(CROSS)
                .totalRounds(3)
                .build();
    }

    private Set<PlayerEntity> createDummyPrs(int count) {
        return IntStream.range(0, count)
                .mapToObj(this::createDummyPr)
                .collect(Collectors.toSet());
    }

    private PlayerEntity createDummyPr(int i) {
        String name = "DummyPR-" + i;
        Long discordId = Long.parseLong(String.valueOf(i) + String.valueOf(i) + String.valueOf(i) + String.valueOf(i));
        return PlayerEntity.builder()
                .discordId(discordId)
                .gamesPlayed(i * 3)
                .name(name)
                .playerId((long) i)
                .totalScore(i * 2)
                .build();
    }
}

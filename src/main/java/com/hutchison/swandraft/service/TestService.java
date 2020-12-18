package com.hutchison.swandraft.service;

import com.hutchison.swandraft.model.Player;
import com.hutchison.swandraft.model.PlayerRecord;
import com.hutchison.swandraft.model.Result;
import com.hutchison.swandraft.model.Tournament;
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
                new Result("DummyPR-0", 3),
                new Result("DummyPR-1", 1)
        );
        t.report(results);
        return t;
    }

    private Tournament createDummyTournament(int count) {
        return Tournament.builder()
                .playerRecords(createDummyPrs(count))
                .seedingStyle(CROSS)
                .totalRounds(3)
                .build();
    }

    private Set<PlayerRecord> createDummyPrs(int count) {
        return IntStream.range(0, count)
                .mapToObj(this::createDummyPr)
                .collect(Collectors.toSet());
    }

    private PlayerRecord createDummyPr(int i) {
        String s = "DummyPR-" + i;
        return PlayerRecord.builder()
                .discordId(s)
                .gamesPlayed(i * 3)
                .name(s)
                .playerRecordId((long) i)
                .totalScore(i * 2)
                .build();
    }
}

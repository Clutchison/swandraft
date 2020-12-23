package com.hutchison.swandraft.model.tournament;

import com.hutchison.swandraft.model.entity.PlayerEntity;
import com.hutchison.swandraft.model.tournament.pairing.SeedingStyle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.hutchison.swandraft.model.tournament.pairing.SeedingStyle.RANDOM;

class TournamentTest {

    /* Static Mock Example
        MockedStatic<Pairing> ms = Mockito.mockStatic(Pairing.class);
        ms.when(() -> Pairing.initial(seedingStyle, playerEntities))
            .thenReturn(new HashSet<Player>());
     */

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void report() {
        // todo
    }

    @Test
    void getMessage() {
        // todo
    }

    @Test
    void advance() {
        // todo
    }

    @Test
    void createAllArgs_happy() {
        Set<PlayerEntity> playerEntities = createDummyPlayerEntities(6);
        Integer totalRounds = 3;
        SeedingStyle seedingStyle = RANDOM;

        Tournament tournament = Tournament.create(playerEntities, seedingStyle, totalRounds);
    }

    @Test
    void createAllArgs_nullSeedingStyle() {
        Set<PlayerEntity> playerEntities = new HashSet<>();
        SeedingStyle seedingStyle = null;
        Integer totalRounds = 3;
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Tournament.create(playerEntities, seedingStyle, totalRounds)
        );
    }

    @Test
    void createAllArgs_negativeTotalRounds() {
        Set<PlayerEntity> playerEntities = new HashSet<>();
        Integer totalRounds = -1;
        Assertions.assertThrows(RuntimeException.class,
                () -> Tournament.create(playerEntities, RANDOM, totalRounds)
        );
    }

    @Test
    void testCreate() {
        // todo
    }

    @Test
    void testCreate1() {
        // todo
    }

    @Test
    void testCreate2() {
        // todo
    }

    private static Set<PlayerEntity> createDummyPlayerEntities(int i) {
        return IntStream.range(0, i)
                .mapToObj(TournamentTest::createDummyPlayerEntity)
                .collect(Collectors.toSet());
    }

    private static PlayerEntity createDummyPlayerEntity(int i) {
        return new PlayerEntity(
                (long) i,
                "Dummy Player: " + i,
                0,
                (long) (i + 1) * 10,
                0,
                0
        );
    }
}
package com.hutchison.swandraft.model.tournament;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TournamentTest {

    /* Static Mock Example
        MockedStatic<Pairings> ms = Mockito.mockStatic(Pairings.class);
        ms.when(() -> Pairings.initial(seedingStyle, playerEntities))
            .thenReturn(new HashSet<EnteredPlayer>());
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

//    @Test
//    void createAllArgs_happy() {
//        Set<PlayerEntity> playerEntities = createDummyPlayerEntities(6);
//        Integer totalRounds = 3;
//        SeedingStyle seedingStyle = RANDOM;
//
//        Tournament tournament = Tournament.create(playerEntities, seedingStyle, totalRounds);
//    }
//
//    @Test
//    void createAllArgs_nullSeedingStyle() {
//        Set<PlayerEntity> playerEntities = new HashSet<>();
//        SeedingStyle seedingStyle = null;
//        Integer totalRounds = 3;
//        Assertions.assertThrows(IllegalArgumentException.class,
//                () -> Tournament.create(playerEntities, seedingStyle, totalRounds)
//        );
//    }
//
//    @Test
//    void createAllArgs_negativeTotalRounds() {
//        Set<PlayerEntity> playerEntities = new HashSet<>();
//        Integer totalRounds = -1;
//        Assertions.assertThrows(RuntimeException.class,
//                () -> Tournament.create(playerEntities, RANDOM, totalRounds)
//        );
//    }

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

//    private static Set<PlayerEntity> createDummyPlayerEntities(int i) {
//        return IntStream.range(0, i)
//                .mapToObj(TournamentTest::createDummyPlayerEntity)
//                .collect(Collectors.toSet());
//    }
//
//    private static PlayerEntity createDummyPlayerEntity(int i) {
//        return new PlayerEntity(
//                (long) i,
//                "Dummy EnteredPlayer: " + i,
//                0,
//                (long) (i + 1) * 10,
//                0,
//                0
//        );
//    }
}
package edu.hitsz.aircraft;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BossEnemyTest {

    private BaseEnemy bossEnemyAircraft;

    @BeforeAll
    static void beforeAll() {
        System.out.println("**--- Executed once before all test methods in this class ---**");
    }

    @BeforeEach
    void setUp() {
        System.out.println("**--- Executed before each test method in this class ---**");
        BaseEnemyFactory baseEnemyFactory;
        baseEnemyFactory = new BossEnemyFactory();
        bossEnemyAircraft = baseEnemyFactory.createEnemy();
    }

    @AfterEach
    void tearDown() {
        System.out.println("**--- Executed after each test method in this class ---**");
        bossEnemyAircraft = null;
    }

    @DisplayName("Test shoot method")
    @Test
    void shoot() {
        System.out.println("**--- Test shoot method executed ---**");
        assertEquals(3, bossEnemyAircraft.executeStrategy(bossEnemyAircraft).size());
    }

    @DisplayName("Test dropProp method")
    @Test
    void dropProp() {
        System.out.println("**--- Test dropProp method executed ---**");
        assertEquals(3, bossEnemyAircraft.dropProp().size());
    }

    @AfterAll
    static void afterAll() {
        System.out.println("**--- Executed once after all test methods in this class ---**");
    }
}
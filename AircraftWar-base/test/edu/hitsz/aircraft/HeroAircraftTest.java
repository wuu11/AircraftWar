package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class HeroAircraftTest {

    private HeroAircraft heroAircraft;

    @BeforeAll
    static void beforeAll() {
        System.out.println("**--- Executed once before all test methods in this class ---**");
    }

    @BeforeEach
    void setUp() {
        System.out.println("**--- Executed before each test method in this class ---**");
        heroAircraft = HeroAircraft.getInstance();
    }

    @AfterEach
    void tearDown() {
        System.out.println("**--- Executed after each test method in this class ---**");
        heroAircraft = null;
    }

    @DisplayName("Test crash method")
    @Test
    void crash() {
        System.out.println("**--- Test crash method executed ---**");
        MobEnemy mobEnemyAircraft = new MobEnemy(Main.WINDOW_WIDTH / 2,
                Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                0, 10, 30);
        assertTrue(heroAircraft.crash(mobEnemyAircraft));
    }

    @DisplayName("Test shoot method")
    @Test
    void shoot() {
        System.out.println("**--- Test shoot method executed ---**");
        assertEquals(1, heroAircraft.executeStrategy(heroAircraft).size());
    }

    @AfterAll
    static void afterAll() {
        System.out.println("**--- Executed once after all test methods in this class ---**");
    }
}
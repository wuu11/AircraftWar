package edu.hitsz.prop;

import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;
import org.junit.jupiter.api.*;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class BloodPropTest {

    private BaseProp bloodProp;

    @BeforeAll
    static void beforeAll() {
        System.out.println("**--- Executed once before all test methods in this class ---**");
    }

    @BeforeEach
    void setUp() {
        System.out.println("**--- Executed before each test method in this class ---**");
        bloodProp = new BloodProp(100, 200, 3, 10, 20);
    }

    @AfterEach
    void tearDown() {
        System.out.println("**--- Executed after each test method in this class ---**");
        bloodProp = null;
    }

    @DisplayName("Test act method")
    @Test
    void act() {
        System.out.println("**--- Test act method executed ---**");
        HeroAircraft heroAircraft;
        heroAircraft = HeroAircraft.getInstance();
        heroAircraft.decreaseHp(30);
        bloodProp.act(heroAircraft, null, null);
        assertEquals(990, heroAircraft.getHp());
        bloodProp.act(heroAircraft, null, null);
        assertEquals(1000, heroAircraft.getHp());
    }

    @DisplayName("Test notValid method")
    @Test
    void notValid() {
        System.out.println("**--- Test notValid method executed ---**");
        bloodProp.vanish();
        assertTrue(bloodProp.notValid());
    }

    @AfterAll
    static void afterAll() {
        System.out.println("**--- Executed once after all test methods in this class ---**");
    }
}
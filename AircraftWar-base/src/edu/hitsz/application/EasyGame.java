package edu.hitsz.application;

import edu.hitsz.aircraft.EliteEnemyFactory;
import edu.hitsz.aircraft.MobEnemyFactory;

import java.io.IOException;

public class EasyGame extends AbstractGame{

    public EasyGame() throws IOException {
        super("EASY");
    }

    @Override
    public void increaseDifficulty(){}

    @Override
    public void createEnemyAircraft() {
        if (enemyAircrafts.size() < enemyMaxNumber) {
            int ran = (int) (Math.random() * 6);
            if (ran == 0) {
                baseEnemyFactory = new EliteEnemyFactory();
                enemyAircrafts.add(baseEnemyFactory.createEnemy());
            }
            else{
                baseEnemyFactory = new MobEnemyFactory();
                enemyAircrafts.add(baseEnemyFactory.createEnemy());
            }
        }
    }

    @Override
    public void createBossEnemy(){}


}

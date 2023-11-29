package edu.hitsz.application;

import edu.hitsz.aircraft.BossEnemyFactory;
import edu.hitsz.aircraft.EliteEnemyFactory;
import edu.hitsz.aircraft.MobEnemyFactory;

import java.io.IOException;

public class CommonGame extends AbstractGame{

    private int limit = 20;
    private int increment = 0;

    public CommonGame() throws IOException {
        super("COMMON");
        enemyMaxNumber = 7;
    }

    @Override
    public void increaseDifficulty() {
        if (time % 8000 == 0) {
            limit++;
            if (limit > 100) {
                limit = 100;
            }
            increment++;
            enemyCycleDuration -= 10;
            if (enemyCycleDuration < 100) {
                enemyCycleDuration = 100;
            }
            System.out.println("提高难度！精英机概率：" + limit/100.0 + "，敌机周期：" + enemyCycleDuration/(double)timeInterval + "，敌机属性提升倍率：" + (30+increment)/30.0);
        }
    }

    @Override
    public void createEnemyAircraft() {
        if (enemyAircrafts.size() < enemyMaxNumber) {
            int ran = (int) (Math.random() * 100);
            if (ran < limit) {
                baseEnemyFactory = new EliteEnemyFactory();
                enemyAircrafts.add(baseEnemyFactory.createEnemy(increment));
            }
            else{
                baseEnemyFactory = new MobEnemyFactory();
                enemyAircrafts.add(baseEnemyFactory.createEnemy(increment));
            }
        }
    }

    @Override
    public void createBossEnemy() {
        if (circleScoreCommon >= 300){
            circleScoreCommon %= 300;
            if (bossEnemyAircrafts.size() < 1){
                baseEnemyFactory = new BossEnemyFactory();
                bossEnemyAircrafts.add(baseEnemyFactory.createEnemy());
                enemyAircrafts.addAll(bossEnemyAircrafts);
                if (musicOn) {
                    bgmBoss = new MusicThread("src/videos/bgm_boss.wav", true);
                    bgmBoss.start();
                }
                System.out.println("产生Boss敌机");
            }
        }
    }
}

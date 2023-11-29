package edu.hitsz.game;

import android.content.Context;
import android.os.Handler;

import java.io.IOException;

import edu.hitsz.ImageManager;
import edu.hitsz.aircraft.EliteEnemyFactory;
import edu.hitsz.aircraft.MobEnemyFactory;

public class EasyGame extends BaseGame{

    public EasyGame(Context context, Handler handler) {
        super(context,handler);
        this.backGround = ImageManager.BACKGROUND1_IMAGE;
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

package edu.hitsz.aircraft;


import edu.hitsz.ImageManager;
import edu.hitsz.activity.MainActivity;

public class BossEnemyFactory implements BaseEnemyFactory {

    @Override
    public BaseEnemy createEnemy(){
        return new BossEnemy(
                (int) (Math.random() * (MainActivity.screenWidth - ImageManager.BOSS_ENEMY_IMAGE.getWidth())) + 42,
                (int) (MainActivity.screenHeight * 0.15),
                3,
                0,
                300,
                new ScatteringShoot(3, 30, 1));
    }

    @Override
    public BaseEnemy createEnemy(int increment){
        return new BossEnemy(
                (int) (Math.random() * (MainActivity.screenWidth - ImageManager.BOSS_ENEMY_IMAGE.getWidth())) + 42,
                (int) (MainActivity.screenHeight * 0.15),
                3,
                0,
                300 + 100*increment,
                new ScatteringShoot(3, 30 + 5*increment, 1));
    }

}

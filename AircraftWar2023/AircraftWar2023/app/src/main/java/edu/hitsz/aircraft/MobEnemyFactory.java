package edu.hitsz.aircraft;

import edu.hitsz.ImageManager;
import edu.hitsz.activity.MainActivity;

public class MobEnemyFactory implements BaseEnemyFactory {

    @Override
    public BaseEnemy createEnemy(){
        return new MobEnemy(
                (int) (Math.random() * (MainActivity.screenWidth - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * MainActivity.screenHeight * 0.05),
                0,
                10,
                30);
    }

    @Override
    public BaseEnemy createEnemy(int increment){
        return new MobEnemy(
                (int) (Math.random() * (MainActivity.screenWidth - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * MainActivity.screenHeight * 0.05),
                0,
                10 + increment/2,
                30 + increment);
    }

}

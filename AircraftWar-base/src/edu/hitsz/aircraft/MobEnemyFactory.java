package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class MobEnemyFactory implements BaseEnemyFactory {

    @Override
    public BaseEnemy createEnemy(){
        return new MobEnemy(
                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                0,
                10,
                30);
    }

    @Override
    public BaseEnemy createEnemy(int increment){
        return new MobEnemy(
                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                0,
                10 + increment/2,
                30 + increment);
    }

}

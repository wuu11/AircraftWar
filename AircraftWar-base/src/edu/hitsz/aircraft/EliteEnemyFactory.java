package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class EliteEnemyFactory implements BaseEnemyFactory {

    @Override
    public BaseEnemy createEnemy(){
        return new EliteEnemy(
                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                (int) (Math.random()*7) - 3,
                6,
                30,
                new DirectShoot(1, 30, 1));
    }

    @Override
    public BaseEnemy createEnemy(int increment) {
        return new EliteEnemy(
                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                (int) (Math.random() * 7) - 3,
                6 + increment/2,
                30 + increment,
                new DirectShoot(1, 30 + increment, 1));
    }
}

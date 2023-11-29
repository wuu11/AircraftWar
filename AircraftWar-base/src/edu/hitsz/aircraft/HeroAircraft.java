package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

/**
 * 英雄飞机，游戏玩家操控
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {


    private volatile static HeroAircraft instance;

    /**
     * @param locationX 英雄机位置x坐标
     * @param locationY 英雄机位置y坐标
     * @param speedX 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp    初始生命值
     * @param strategy 英雄机的初始射击策略
     */
    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp, Strategy strategy) {
        super(locationX, locationY, speedX, speedY, hp, strategy);
    }

    public static HeroAircraft getInstance() {
        if (instance == null) {
            synchronized (HeroAircraft.class) {
                if (instance == null) {
                    instance = new HeroAircraft(
                            Main.WINDOW_WIDTH / 2,
                            Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                            0, 0, 1000, new DirectShoot(1, 30, -1));
                }
            }
        }
        return instance;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    @Override
    public List<BaseBullet> executeStrategy(AbstractAircraft aircraft) {
        return strategy.shoot(aircraft);
    }

    @Override
    public void update() {

    }
}

package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.prop.*;

import java.util.LinkedList;
import java.util.List;

/**
 * 精英敌机
 * 按周期直射子弹
 * 坠毁后随机产生向下飞行的道具
 */

public class EliteEnemy extends BaseEnemy{

    private BasePropFactory propFactory;

    /**
     * @param locationX 敌机位置x坐标
     * @param locationY 敌机位置y坐标
     * @param speedX 敌机射出的子弹的基准速度
     * @param speedY 敌机射出的子弹的基准速度
     * @param hp    初始生命值
     * @param strategy 敌机的初始射击策略
     */
    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp, Strategy strategy) {
        super(locationX, locationY, speedX, speedY, hp, strategy);
    }

    @Override
    public List<BaseProp> dropProp() {
        List<BaseProp> props = new LinkedList<>();
        int x = locationX;
        int y = locationY;
        int speedX = (int) (Math.random()*5) - 2;
        int speedY = this.speedY + 1;

        int ran = (int) (Math.random() * 10);
        if (ran >= 1 && ran <= 3){
            propFactory = new BloodPropFactory();
            props.add(propFactory.createProp(x, y, speedX, speedY, 20));
        }
        else if (ran >= 4 && ran <= 6){
            propFactory = new BulletPropFactory();
            props.add(propFactory.createProp(x, y, speedX, speedY, 20));
        }
        else if (ran >= 7 && ran <= 9){
            propFactory = new BombPropFactory();
            props.add(propFactory.createProp(x, y, speedX, speedY, 20));
        }
        return props;
    }

    @Override
    public void update() {
        vanish();
    }
}

package edu.hitsz.aircraft;

import edu.hitsz.activity.MainActivity;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.prop.BaseProp;

import java.util.List;

/**
 * 敌机类
 */

public abstract class BaseEnemy extends AbstractAircraft{

    public BaseEnemy(int locationX, int locationY, int speedX, int speedY, int hp, Strategy strategy) {
        super(locationX, locationY, speedX, speedY, hp, strategy);
    }

    @Override
    public void forward() {
        super.forward();

        // 判定 y 轴向下飞行出界
        if (locationY >= MainActivity.screenHeight) {
            vanish();
        }
    }

    @Override
    public List<BaseBullet> executeStrategy(AbstractAircraft aircraft) {
        return strategy.shoot(aircraft);
    }

    public abstract List<BaseProp> dropProp();

}

package edu.hitsz.prop;

import edu.hitsz.activity.MainActivity;
import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.BaseEnemy;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

/**
 * 所有道具的抽象父类
 * 三种道具：加血道具（BloodProp）、火力道具（BulletProp）、炸弹道具（BombProp）
 */

public abstract class BaseProp extends AbstractFlyingObject{

    protected int power = 10;

    public BaseProp(int locationX, int locationY, int speedX, int speedY, int power){
        super(locationX, locationY, speedX, speedY);
        this.power = power;
    }

    @Override
    public void forward() {
        super.forward();

        // 判定 y 轴出界
        if (speedY > 0 && locationY >= MainActivity.screenHeight) {
            // 向下飞行出界
            vanish();
        }
    }

    @Override
    public void update() {

    }

    public abstract void act(AbstractAircraft aircraft, List<BaseBullet> enemyBullets, List<BaseEnemy> enemyAircrafts);

    public abstract boolean isBomb();
}

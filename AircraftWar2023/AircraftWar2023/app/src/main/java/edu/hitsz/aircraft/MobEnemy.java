package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.prop.BaseProp;

import java.util.LinkedList;
import java.util.List;

/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
public class MobEnemy extends BaseEnemy {

    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, null);
    }

    @Override
    public List<BaseBullet> executeStrategy(AbstractAircraft aircraft) {
        return new LinkedList<>();
    }

    @Override
    public List<BaseProp> dropProp() { return new LinkedList<>(); }

    @Override
    public void update() {
        vanish();
    }
}

package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;

import java.util.List;

/**
 * 所有种类飞机的抽象父类：
 * 敌机（BOSS, ELITE, MOB），英雄飞机
 *
 * @author hitsz
 */
public abstract class AbstractAircraft extends AbstractFlyingObject {
    /**
     * 生命值
     */
    protected int maxHp;
    protected int hp;

    protected Strategy strategy;

    public AbstractAircraft(int locationX, int locationY, int speedX, int speedY, int hp, Strategy strategy) {
        super(locationX, locationY, speedX, speedY);
        this.hp = hp;
        this.maxHp = hp;
        this.strategy = strategy;
    }

    public  void increaseHp(int increase){
        if (hp + increase >= maxHp) {
            hp = maxHp;
        } else {
            hp += increase;
        }
    }

    public void decreaseHp(int decrease){
        hp -= decrease;
        if(hp <= 0){
            hp=0;
            vanish();
        }
    }

    public int getHp() {
        return hp;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public List<BaseBullet> executeStrategy(AbstractAircraft aircraft) {
        return strategy.shoot(aircraft);
    }

}



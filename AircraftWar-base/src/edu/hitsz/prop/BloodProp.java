package edu.hitsz.prop;

import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

public class BloodProp extends BaseProp{

    public BloodProp(int locationX, int locationY, int speedX, int speedY, int power){
        super(locationX, locationY, speedX, speedY, power);
    }

    @Override
    public void act(AbstractAircraft aircraft, List<BaseBullet> enemyBullets, List<BaseEnemy> enemyAircrafts) {
        aircraft.increaseHp(power);
        System.out.println("BloodSupply active!");
    }
}

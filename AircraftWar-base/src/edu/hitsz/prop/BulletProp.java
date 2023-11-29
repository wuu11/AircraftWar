package edu.hitsz.prop;

import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

public class BulletProp extends BaseProp{

    public BulletProp(int locationX, int locationY, int speedX, int speedY, int power){
        super(locationX, locationY, speedX, speedY, power);
    }

    @Override
    public void act(AbstractAircraft aircraft, List<BaseBullet> enemyBullets, List<BaseEnemy> enemyAircrafts) {
        Runnable r = () -> {
            for (int i = 0; i < 4; i++) {
                aircraft.setStrategy(new ScatteringShoot(3, 30, -1));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            aircraft.setStrategy(new DirectShoot(1, 30, -1));
        };
        new Thread(r).start();
        System.out.println("FireSupply active!");
    }
}

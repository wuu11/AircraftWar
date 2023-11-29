package edu.hitsz.prop;

import edu.hitsz.aircraft.*;
import edu.hitsz.application.AbstractGame;
import edu.hitsz.application.MusicThread;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;

import java.util.ArrayList;
import java.util.List;

public class BombProp extends BaseProp{

    private List<AbstractFlyingObject> objectList = new ArrayList<>();

    public BombProp(int locationX, int locationY, int speedX, int speedY, int power){
        super(locationX, locationY, speedX, speedY, power);
    }

    public void addObject(AbstractFlyingObject object) {
        objectList.add(object);
    }

    public void removeObject(AbstractFlyingObject object) {
        objectList.remove(object);
    }

    public void notifyAll(boolean active) {
        for (AbstractFlyingObject object : objectList){
            object.update();
        }
    }
    @Override
    public void act(AbstractAircraft aircraft, List<BaseBullet> enemyBullets, List<BaseEnemy> enemyAircrafts) {
        for(BaseBullet bullet : enemyBullets) {
            addObject(bullet);
        }
        for (BaseEnemy enemyAircraft : enemyAircrafts) {
            addObject(enemyAircraft);
        }
        notifyAll(true);
        if (AbstractGame.musicOn) {
            MusicThread explosion = new MusicThread("src/videos/bomb_explosion.wav", false);
            explosion.start();
        }
        System.out.println("BombSupply active!");
        for(BaseBullet bullet : enemyBullets) {
            removeObject(bullet);
        }
        for (BaseEnemy enemyAircraft : enemyAircrafts) {
            removeObject(enemyAircraft);
        }
    }
}

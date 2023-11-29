package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class ScatteringShoot implements Strategy {

    /**
     * 子弹一次发射数量
     */
    private int shootNum = 3;

    /**
     * 子弹伤害
     */
    private int power = 30;

    /**
     * 子弹发射方向
     */
    private int direction = -1;

    public ScatteringShoot(int shootNum, int power, int direction) {
        this.shootNum = shootNum;
        this.power = power;
        this.direction = direction;
    }

    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + direction*3;
        int speedX = aircraft.getSpeedX();
        int speedY = aircraft.getSpeedY() + direction*8;
        BaseBullet bullet;
        for(int i=0; i<shootNum; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            if (direction == -1) {
                bullet = new HeroBullet(x + (i*2 - shootNum + 1)*10, y, speedX+i-1, speedY, power);
            }
            else {
                bullet = new EnemyBullet(x + (i * 2 - shootNum + 1) * 10, y, speedX + i - 1, speedY, power);
            }
            res.add(bullet);
        }
        return res;
    }


}

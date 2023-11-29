package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;

import java.util.List;

public interface Strategy {

    /**
     * 飞机射击方法，可射击对象必须实现
     * @return
     *  可射击对象需实现，返回子弹
     *  非可射击对象空实现，返回null
     */
    List<BaseBullet> shoot(AbstractAircraft aircraft);
}

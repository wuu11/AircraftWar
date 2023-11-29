package edu.hitsz.aircraft;

public interface BaseEnemyFactory {

    BaseEnemy createEnemy();

    BaseEnemy createEnemy(int increment);

}

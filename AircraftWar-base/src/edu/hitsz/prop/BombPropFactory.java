package edu.hitsz.prop;

public class BombPropFactory implements BasePropFactory {

    @Override
    public BaseProp createProp(int locationX, int locationY, int speedX, int speedY, int power) {
        return new BombProp(locationX, locationY, speedX, speedY, power);
    }
}

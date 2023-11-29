package edu.hitsz.prop;

public class BulletPropFactory implements BasePropFactory {

    @Override
    public BaseProp createProp(int locationX, int locationY, int speedX, int speedY, int power) {
        return new BulletProp(locationX, locationY, speedX, speedY, power);
    }
}

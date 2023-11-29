package edu.hitsz.prop;

public class BloodPropFactory implements BasePropFactory {

    @Override
    public BaseProp createProp(int locationX, int locationY, int speedX, int speedY, int power) {
        return new BloodProp(locationX, locationY, speedX, speedY, power);
    }
}

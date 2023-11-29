package edu.hitsz.aircraft;

import edu.hitsz.activity.MainActivity;
import edu.hitsz.prop.*;

import java.util.LinkedList;
import java.util.List;

public class BossEnemy extends BaseEnemy {

    private BasePropFactory propFactory;

    /**
     * 敌机坠毁后，掉落道具的数量
     */
    private int propNum = 3;

    /**
     * @param locationX 敌机位置x坐标
     * @param locationY 敌机位置y坐标
     * @param speedX 敌机射出的子弹的基准速度
     * @param speedY 敌机射出的子弹的基准速度
     * @param hp    初始生命值
     * @param strategy 敌机的初始射击策略
     */
    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp, Strategy strategy) {
        super(locationX, locationY, speedX, speedY, hp, strategy);
    }

    @Override
    public void forward() {
        locationX += speedX;
        locationY += speedY;
        if (locationX <= 42 || locationX >= MainActivity.screenWidth - 42) {
            // 横向超出边界后反向
            speedX = -speedX;
        }
    }

    @Override
    public List<BaseProp> dropProp() {
        List<BaseProp> props = new LinkedList<>();
        int x = locationX;
        int y = locationY;
        int speedX = this.speedX;
        int speedY = this.speedY + 10;

        for(int i=0; i<propNum; i++){
            int ran = (int) (Math.random() * 3);
            if (ran == 0){
                propFactory = new BloodPropFactory();
                props.add(propFactory.createProp(x + (i*2 - propNum + 1)*20, y, speedX + (i-1)*2, speedY, 20));
            }
            else if (ran == 1){
                propFactory = new BulletPropFactory();
                props.add(propFactory.createProp(x + (i*2 - propNum + 1)*20, y, speedX + (i-1)*2, speedY, 20));
            }
            else if (ran == 2){
                propFactory = new BombPropFactory();
                props.add(propFactory.createProp(x + (i*2 - propNum + 1)*20, y, speedX + (i-1)*2, speedY, 20));
            }
        }

        return props;
    }

    @Override
    public void update() {
        hp -= 100;
    }
}

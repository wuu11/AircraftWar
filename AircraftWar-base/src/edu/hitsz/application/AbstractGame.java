package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.prop.*;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public abstract class AbstractGame extends JPanel {

    private int backGroundTop = 0;

    public static boolean musicOn = false;

    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    protected int timeInterval = 40;

    protected final HeroAircraft heroAircraft;

    protected final List<BaseEnemy> enemyAircrafts;
    protected final List<BaseBullet> heroBullets;
    protected final List<BaseBullet> enemyBullets;
    protected final List<BaseProp> props;
    protected final List<BaseEnemy> bossEnemyAircrafts;

    protected BaseEnemyFactory baseEnemyFactory;

    private RankingDao rankingDao;

    private MusicThread bgm;
    private MusicThread bulletHit;
    private MusicThread gameOver;
    private MusicThread getSupply;
    protected MusicThread bgmBoss;

    /**
     * 屏幕中出现的敌机最大数量
     */
    protected int enemyMaxNumber = 5;

    /**
     * 当前得分
     */
    protected int score = 0;

    protected int circleScoreCommon = 0;
    protected int circleScoreDifficult = 0;

    /**
     * 当前时刻
     */
    protected int time = 0;

    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    protected int heroCycleDuration = 600;
    protected int enemyCycleDuration = 600;
    private int heroCycleTime = 0;
    private int enemyCycleTime = 0;

    /**
     * 游戏结束标志
     */
    private boolean gameOverFlag = false;

    /**
     * 游戏难度标志
     */
    private String degree;

    public AbstractGame(String degree) throws IOException {
        heroAircraft = HeroAircraft.getInstance();

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();
        bossEnemyAircrafts = new LinkedList<>();

        this.degree = degree;
        rankingDao = new RankingDaoImpl("out/RankingList_" + degree + ".txt");

        if (musicOn) {
            bgm = new MusicThread("src/videos/bgm.wav", true);
            bgm.start();
        }

        /**
         * Scheduled 线程池，用于定时任务调度
         * 关于alibaba code guide：可命名的 ThreadFactory 一般需要第三方包
         * apache 第三方库： org.apache.commons.lang3.concurrent.BasicThreadFactory
         */
        this.executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("game-action-%d").daemon(true).build());

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {

            time += timeInterval;

            //随时间提高游戏难度
            increaseDifficulty();

            // 周期性执行（控制频率）
            if (timeCountAndNewHeroCycleJudge()) {
                System.out.println(time);
                //英雄机射出子弹
                heroShootAction();
            }
            if (timeCountAndNewEnemyCycleJudge()) {
                // 新敌机产生
                createEnemyAircraft();
                // 敌机射出子弹
                enemyShootAction();
            }

            // 当分数达到设定阈值时，Boss敌机出现
            createBossEnemy();

            // 子弹移动
            bulletsMoveAction();

            // 飞机移动
            aircraftsMoveAction();

            // 道具移动
            propsMoveAction();

            // 撞击检测
            crashCheckAction();

            // 后处理
            postProcessAction();

            // 每个时刻重绘界面
            repaint();

            // 游戏结束检查英雄机是否存活
            if (heroAircraft.getHp() <= 0) {
                // 游戏结束
                executorService.shutdown();
                gameOverFlag = true;
                if (musicOn) {
                    bgm.setStopFlag(true);
                    if (bgmBoss != null && bgmBoss.isAlive()) {
                        bgmBoss.setStopFlag(true);
                    }
                    gameOver = new MusicThread("src/videos/game_over.wav", false);
                    gameOver.start();
                }
                System.out.println("Game Over!");
            }

            //游戏结束显示排行榜
            if (gameOverFlag) {
                try {
                    Main.cardPanel.add(new RankingList(degree).getMainPanel());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Main.cardLayout.last(Main.cardPanel);

                String userName = JOptionPane.showInputDialog(getRootPane(),"游戏结束，你的得分为" + score + "。\n请输入名字记录得分：");
                if (userName != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Ranking newRanking = new Ranking(1, userName, score, sdf.format(System.currentTimeMillis()));
                    rankingDao.doAdd(newRanking);
                    rankingDao.doRank();
                    try {
                        rankingDao.storage();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    Main.cardPanel.add(new RankingList(degree).getMainPanel());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Main.cardLayout.last(Main.cardPanel);
            }

        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

    }

    //***********************
    //      Action 各部分
    //***********************

    public abstract void increaseDifficulty();

    private boolean timeCountAndNewHeroCycleJudge() {
        heroCycleTime += timeInterval;
        if (heroCycleTime >= heroCycleDuration && heroCycleTime - timeInterval < heroCycleDuration) {
            // 跨越到新的周期
            heroCycleTime %= heroCycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private boolean timeCountAndNewEnemyCycleJudge() {
        enemyCycleTime += timeInterval;
        if (enemyCycleTime >= enemyCycleDuration) {
            // 跨越到新的周期
            enemyCycleTime %= enemyCycleDuration;
            return true;
        } else {
            return false;
        }
    }

    public abstract void createEnemyAircraft();

    public abstract void createBossEnemy();

    private void heroShootAction() {
        // 英雄机射击
        heroBullets.addAll(heroAircraft.executeStrategy(heroAircraft));
    }

    private void enemyShootAction() {
        // 敌机射击
        for (BaseEnemy enemyAircraft : enemyAircrafts){
            enemyBullets.addAll(enemyAircraft.executeStrategy(enemyAircraft));
        }
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (BaseEnemy enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void propsMoveAction() {
        for (BaseProp prop : props){
            prop.forward();
        }
    }

    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        // TODO 敌机子弹攻击英雄
        for (BaseBullet bullet : enemyBullets){
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.getHp() <= 0){
                //英雄机损毁，不再检测
                break;
            }
            if (heroAircraft.crash(bullet)){
                //英雄机撞击到敌机子弹
                //英雄机损失一定生命值
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (BaseEnemy enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    if (musicOn) {
                        bulletHit = new MusicThread("src/videos/bullet_hit.wav", false);
                        bulletHit.start();
                    }
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        // TODO 获得分数，产生道具补给
                        props.addAll(enemyAircraft.dropProp());
                        score += 10;
                        circleScoreCommon += 10;
                        circleScoreDifficult += 10;
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);

        // Todo: 我方获得道具，道具生效
        for (BaseProp prop : props){
            if (prop.notValid()){
                continue;
            }
            if (heroAircraft.crash(prop) || prop.crash(heroAircraft)){
                if (musicOn) {
                    getSupply = new MusicThread("src/videos/get_supply.wav", false);
                    getSupply.start();
                }
                prop.act(heroAircraft, enemyBullets, enemyAircrafts);
                prop.vanish();
                for (BaseEnemy enemyAircraft : enemyAircrafts) {
                    if (enemyAircraft.notValid()){
                        score += 10;
                        circleScoreCommon += 10;
                        circleScoreDifficult += 10;
                    }
                }
            }
        }
    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 删除无效的道具
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);
        if(bossEnemyAircrafts.removeIf(AbstractFlyingObject::notValid) && musicOn){
            bgmBoss.setStopFlag(true);
        }
    }


    //***********************
    //      Paint 各部分
    //***********************

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
     * @param  g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        try {
            ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg_" + degree + ".jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 绘制背景,图片滚动
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);

        paintImageWithPositionRevised(g, props);

        paintImageWithPositionRevised(g, enemyAircrafts);

        paintImageWithPositionRevised(g, bossEnemyAircrafts);

        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (int i = 0; i < objects.size(); i++) {
            BufferedImage image = objects.get(i).getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, objects.get(i).getLocationX() - image.getWidth() / 2,
                    objects.get(i).getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }


}

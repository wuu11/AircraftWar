package edu.hitsz.game;

import static android.os.Looper.getMainLooper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

import edu.hitsz.ClientThread;
import edu.hitsz.ImageManager;
import edu.hitsz.activity.MainActivity;
import edu.hitsz.MusicPlayer;
import edu.hitsz.activity.OnlineRankingListActivity;
import edu.hitsz.activity.RankingListActivity;
import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.BaseEnemy;
import edu.hitsz.aircraft.BaseEnemyFactory;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.prop.BaseProp;

/**
 * 游戏逻辑抽象基类，遵循模板模式，action() 为模板方法
 * 包括：游戏主面板绘制逻辑，游戏执行逻辑。
 * 子类需实现抽象方法，实现相应逻辑
 * @author hitsz
 */
public abstract class BaseGame extends SurfaceView implements SurfaceHolder.Callback, Runnable{

    public static final String TAG = "BaseGame";
    public static String userName = null;
    boolean mbLoop = false; //控制绘画线程的标志位
    private SurfaceHolder mSurfaceHolder;
    private Canvas canvas;  //绘图的画布
    private Paint mPaint;
    private Handler handler;
    private Context context;

    //点击屏幕位置
    float clickX = 0, clickY=0;

    private int backGroundTop = 0;

    /**
     * 背景图片缓存，可随难度改变
     */
    protected Bitmap backGround;

    public static boolean musicOn = false;

    protected MusicPlayer musicPlayer;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    protected int timeInterval = 12;

    private final HeroAircraft heroAircraft;

    protected final List<BaseEnemy> enemyAircrafts;

    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;

    protected final List<BaseProp> props;
    protected final List<BaseEnemy> bossEnemyAircrafts;

    protected BaseEnemyFactory baseEnemyFactory;

    protected int enemyMaxNumber = 5;

    public boolean gameOverFlag = false;

    public static int score = 0;
    protected int time = 0;

    protected int circleScoreCommon = 0;
    protected int circleScoreDifficult = 0;

    /**
     * 周期（ms)
     * 控制英雄机射击周期，默认值设为简单模式
     */
    protected int heroCycleDuration = 600;
    protected int enemyCycleDuration = 600;
    private int heroCycleTime = 0;
    private int enemyCycleTime = 0;

    private Handler battleHandler;
    private Handler rankHandler;
    private ClientThread battleThread;
    private ClientThread rankThread;
    private String[] battleMessage;
    public static String rankMessage;

    public BaseGame(Context context, Handler handler) {
        super(context);
        this.context = context;
        this.handler = handler;
        mbLoop = true;
        mPaint = new Paint();  //设置画笔
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
        this.setFocusable(true);
        ImageManager.initImage(context);

        musicPlayer = new MusicPlayer(context);

        // 初始化英雄机
        heroAircraft = HeroAircraft.getInstance();

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();
        bossEnemyAircrafts = new LinkedList<>();

        heroController();

        if (musicOn) {
            musicPlayer.playBgm();
        }

        battleHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                //如果消息来自于子线程且不为空
                if (msg.what == 0x123 && msg.obj != null) {
                    battleMessage = ((String) msg.obj).split(" ");
                }
            }
        };
        rankHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                //如果消息来自于子线程且不为空
                if (msg.what == 0x123 && msg.obj != null) {
                    rankMessage = (String) msg.obj;
                }
            }
        };

        if (userName != null) {
            battleThread = new ClientThread(9999, battleHandler);
            new Thread(battleThread).start();
            rankThread = new ClientThread(8888, rankHandler);
            new Thread(rankThread).start();
        }

    }
    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {

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

                //联网对战
                if (userName != null) {
                    Message msg = new Message();
                    msg.obj = userName + " " + score + " " + heroAircraft.getHp() + " " + RankingListActivity.degree;
                    battleThread.toserverHandler.sendMessage(msg);
                    if (battleThread.toclientMessage != null) {
                        battleMessage = battleThread.toclientMessage.split(" ");
                    }
                }

                // 游戏结束关闭背景音乐
                if (gameOverFlag) {
                    // 游戏结束
                    if (musicOn) {
                        musicPlayer.gameOverSP();
                        musicPlayer.stopBgm();
                        musicPlayer.stopBossBgm();
                    }

                    //网络排行榜
                    if (userName != null) {
                        Message msg1 = new Message();
                        msg1.obj = userName + " " + score + " " + RankingListActivity.degree;
                        rankThread.toserverHandler.sendMessage(msg1);
                        while (rankMessage == null) {
                            if (rankThread.toclientMessage != null) {
                                rankMessage = rankThread.toclientMessage;
                                Intent intent = new Intent(context, OnlineRankingListActivity.class);
                                context.startActivity(intent);
                                Message msg2 = new Message();
                                msg2.obj = "bye " + userName;
                                battleThread.toserverHandler.sendMessage(msg2);
                            }
                        }
                    }
                }

                try {
                    Thread.sleep(timeInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

        };
        new Thread(task).start();
    }

    public void heroController(){
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                clickX = motionEvent.getX();
                clickY = motionEvent.getY();
                heroAircraft.setLocation(clickX, clickY);

                if ( clickX<0 || clickX> MainActivity.screenWidth || clickY<0 || clickY>MainActivity.screenHeight){
                    // 防止超出边界
                    return false;
                }
                return true;
            }
        });
    }


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
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
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
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        // 敌机子弹攻击英雄
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
            for (int i = 0; i < enemyAircrafts.size(); i++) {
                if (enemyAircrafts.get(i).notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircrafts.get(i).crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    if (musicOn) {
                        musicPlayer.hitSP();
                    }
                    enemyAircrafts.get(i).decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircrafts.get(i).notValid()) {
                        // 获得分数，产生道具补给
                        props.addAll(enemyAircrafts.get(i).dropProp());
                        score += 10;
                        circleScoreCommon += 10;
                        circleScoreDifficult += 10;
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircrafts.get(i).crash(heroAircraft) || heroAircraft.crash(enemyAircrafts.get(i))) {
                    enemyAircrafts.get(i).vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);

        // 我方获得道具，道具生效
        for (BaseProp prop : props){
            if (prop.notValid()){
                continue;
            }
            if (heroAircraft.crash(prop) || prop.crash(heroAircraft)){
                if (musicOn) {
                    musicPlayer.supplySP();
                    if (prop.isBomb()) {
                        musicPlayer.explosionSP();
                    }
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
     * 4. 检查英雄机生存
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);
        if(bossEnemyAircrafts.removeIf(AbstractFlyingObject::notValid) && musicOn){
            musicPlayer.pauseBossBgm();
        }
        if (heroAircraft.notValid()) {
            gameOverFlag = true;
            mbLoop = false;
            HeroAircraft.instance = null;
            Log.i(TAG, "heroAircraft is not Valid");
        }

    }

    public void draw() {
        canvas = mSurfaceHolder.lockCanvas();
        if(mSurfaceHolder == null || canvas == null){
            return;
        }

        //绘制背景，图片滚动
        canvas.drawBitmap(backGround,0,this.backGroundTop-backGround.getHeight(),mPaint);
        canvas.drawBitmap(backGround,0,this.backGroundTop,mPaint);
        backGroundTop +=1;
        if (backGroundTop == MainActivity.screenHeight)
            this.backGroundTop = 0;

        //先绘制子弹，后绘制飞机
        paintImageWithPositionRevised(enemyBullets); //敌机子弹


        paintImageWithPositionRevised(heroBullets);  //英雄机子弹


        paintImageWithPositionRevised(props);//道具


        paintImageWithPositionRevised(enemyAircrafts);//敌机

        paintImageWithPositionRevised(bossEnemyAircrafts);//Boss敌机


        canvas.drawBitmap(ImageManager.HERO_IMAGE,
                heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY()- ImageManager.HERO_IMAGE.getHeight() / 2,
                mPaint);

        //画生命值
        paintScoreAndLife();

        mSurfaceHolder.unlockCanvasAndPost(canvas);

    }

    private void paintImageWithPositionRevised(List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (int i = 0; i < objects.size(); i++) {
            Bitmap image = objects.get(i).getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            canvas.drawBitmap(image, objects.get(i).getLocationX() - image.getWidth() / 2,
                    objects.get(i).getLocationY() - image.getHeight() / 2, mPaint);
        }
    }

    private void paintScoreAndLife() {
        int x = 10;
        int y = 40;

        mPaint.setColor(Color.RED);
        mPaint.setTextSize(50);

        if (userName == null) {
            canvas.drawText("SCORE:" + this.score, x, y, mPaint);
            y = y + 60;
            canvas.drawText("LIFE:" + this.heroAircraft.getHp(), x, y, mPaint);
        }
        else if (battleMessage != null) {
            for (int i = 0; i < battleMessage.length; i++) {
                if (i % 4 == 0 && !battleMessage[i].equals(RankingListActivity.degree)) {
                    i = i + 3;
                    continue;
                }
                canvas.drawText(battleMessage[i], x, y, mPaint);
                y = y + 60;
            }
        }

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        new Thread(this).start();
        Log.i(TAG, "start surface view thread");
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        MainActivity.screenWidth = i1;
        MainActivity.screenHeight = i2;
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        mbLoop = false;
    }

    @Override
    public void run() {

        while (mbLoop){   //游戏结束停止绘制
            synchronized (mSurfaceHolder){
                action();
                draw();
            }
        }
        Message message = Message.obtain();
        message.what = 1 ;
        handler.sendMessage(message);
    }
}

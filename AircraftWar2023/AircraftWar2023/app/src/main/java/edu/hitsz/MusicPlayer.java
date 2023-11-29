package edu.hitsz;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.util.HashMap;


public class MusicPlayer {

    private MediaPlayer bgmMediaPlayer;
    private MediaPlayer bgmBossMediaPlayer;

    private SoundPool soundPool;
    private AudioAttributes audioAttributes = null;
    private HashMap<Integer,Integer> soundPoolMap;

    public MusicPlayer(Context context) {
        if (bgmMediaPlayer == null) {
            // 根据音乐资源文件创建MediaPlayer对象 设置循环播放属性
            bgmMediaPlayer = MediaPlayer.create(context, R.raw.bgm);
            bgmMediaPlayer.setLooping(true);
        }
        if (bgmBossMediaPlayer == null) {
            bgmBossMediaPlayer = MediaPlayer.create(context, R.raw.bgm_boss);
            bgmBossMediaPlayer.setLooping(true);
        }
        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(audioAttributes)
                .build();
        soundPoolMap = new HashMap<>();
        soundPoolMap.put(1, soundPool.load(context, R.raw.bomb_explosion, 1));
        soundPoolMap.put(2, soundPool.load(context, R.raw.bullet_hit, 1));
        soundPoolMap.put(3, soundPool.load(context, R.raw.get_supply, 1));
        soundPoolMap.put(4, soundPool.load(context, R.raw.game_over, 1));
    }

    public void playBgm() {
        bgmMediaPlayer.start();
    }

    public void playBossBgm() {
        bgmBossMediaPlayer.start();
    }

    public void explosionSP() {
        soundPool.play(soundPoolMap.get(1), 1, 1, 1, 0, 1);
    }

    public void hitSP() {
        soundPool.play(soundPoolMap.get(2), 1, 1, 1, 0, 1);
    }

    public void supplySP() {
        soundPool.play(soundPoolMap.get(3), 1, 1, 1, 0, 1);
    }

    public void gameOverSP() {
        soundPool.play(soundPoolMap.get(4), 1, 1, 1, 0, 1);
    }

    public void stopBgm() {
        bgmMediaPlayer.stop();
        bgmMediaPlayer.release();
    }

    public void stopBossBgm() {
        bgmBossMediaPlayer.stop();
        bgmBossMediaPlayer.release();
    }

    public void pauseBossBgm() {
        bgmBossMediaPlayer.pause();
    }
}

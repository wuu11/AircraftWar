package edu.hitsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import edu.hitsz.game.BaseGame;
import edu.hitsz.game.EasyGame;
import edu.hitsz.game.HardGame;
import edu.hitsz.game.MediumGame;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";

    private int gameType=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(GameActivity.this, RankingListActivity.class);

        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d(TAG,"handleMessage");
                if (msg.what == 1) {
                    Toast.makeText(GameActivity.this,"GameOver",Toast.LENGTH_SHORT).show();
                    if (BaseGame.userName == null) {
                        startActivity(intent);
                    }
                }
            }
        };
        if(getIntent() != null){
            gameType = getIntent().getIntExtra("gameType",1);
        }
        BaseGame basGameView;
        if(gameType == 1){
            basGameView = new MediumGame(this,handler);
            RankingListActivity.degree = "Common";
        }else if(gameType == 3){
            basGameView = new HardGame(this,handler);
            RankingListActivity.degree = "Difficult";
        }else{
            basGameView = new EasyGame(this,handler);
            RankingListActivity.degree = "Easy";
        }
        setContentView(basGameView);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
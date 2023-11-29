package edu.hitsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.hitsz.ClientThread;
import edu.hitsz.R;
import edu.hitsz.game.BaseGame;

public class LoginActivity extends AppCompatActivity implements OnClickListener{

    private static final int PORT = 8899;//端口号

    private EditText et_name;
    private EditText et_password;
    private Button bt_login;
    private Button bt_register;
    private Button bt_offline;

    private String userName;
    private String password;
    private Handler handler;
    private ClientThread clientThread;
    private String loginMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_name = findViewById(R.id.et_name);
        et_password = findViewById(R.id.et_pwd);

        bt_login = findViewById(R.id.bt_login);
        bt_register = findViewById(R.id.bt_register);
        bt_offline = findViewById(R.id.bt_offline);

        bt_login.setOnClickListener(this);
        bt_register.setOnClickListener(this);
        bt_offline.setOnClickListener(this);

        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                //如果消息来自于子线程
                if (msg.what == 0x123) {
                    loginMessage = (String) msg.obj;
                }
            }
        };

        clientThread = new ClientThread(PORT, handler);
        new Thread(clientThread).start();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.bt_offline:
                intent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_login:
                userName = et_name.getText().toString();
                password = et_password.getText().toString();
                if (isLoginSuccess(userName, password)) {
                    Toast.makeText(getApplicationContext(), "登录成功",
                            Toast.LENGTH_SHORT).show();
                    BaseGame.userName = userName;
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    /**Message msg = new Message();
                    msg.obj = "bye";
                    clientThread.toserverHandler.sendMessage(msg);**/
                }
                else {
                    Toast.makeText(getApplicationContext(), "登录失败",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_register:
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    private boolean isLoginSuccess(String userName, String password) {
        Message msg = new Message();
        msg.obj = userName + " " + password;
        clientThread.toserverHandler.sendMessage(msg);
        while(clientThread.toclientMessage == null) {

        }
        loginMessage = clientThread.toclientMessage;
        System.out.println(loginMessage);
        return loginMessage.equals("Y") ? true : false;
    }
}

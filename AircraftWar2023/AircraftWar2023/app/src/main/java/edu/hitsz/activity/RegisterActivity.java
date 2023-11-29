package edu.hitsz.activity;

import android.app.Activity;
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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.hitsz.ClientThread;
import edu.hitsz.R;
import edu.hitsz.game.BaseGame;

public class RegisterActivity extends AppCompatActivity {

    private static final int PORT = 9988;//端口号

    private EditText et_name;
    private EditText et_password;
    private Button bt_register;

    private String userName;
    private String password;
    private Handler handler;
    private ClientThread clientThread;
    private String registerMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_name = findViewById(R.id.et_name);
        et_password = findViewById(R.id.et_pwd);

        bt_register = findViewById(R.id.bt_register);

        bt_register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = et_name.getText().toString();
                password = et_password.getText().toString();
                if (isRegisterSuccess(userName, password)) {
                    Toast.makeText(getApplicationContext(), "注册成功",
                            Toast.LENGTH_SHORT).show();
                    BaseGame.userName = userName;
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "注册失败，该用户已存在",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                //
            }
        };
        clientThread = new ClientThread(PORT, handler);
        new Thread(clientThread).start();
    }

    private boolean isRegisterSuccess(String userName, String password) {
        Message msg = new Message();
        msg.obj = userName + " " + password;
        clientThread.toserverHandler.sendMessage(msg);
        System.out.println(userName + " " + password);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        registerMessage = clientThread.toclientMessage;
        System.out.println(registerMessage);
        return registerMessage.equals("YES") ? true : false;
    }
}

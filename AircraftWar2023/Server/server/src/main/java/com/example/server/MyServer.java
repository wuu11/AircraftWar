package com.example.server;

public class MyServer {

    public static void main(String[] args) {
        new MyServer();
    }

    public MyServer() {
        new Thread(()->{new LoginServer();}).start();
        new Thread(()->{new RegisterServer();}).start();
        new Thread(()->{new BattleServer();}).start();
        new Thread(()->{new RankServer();}).start();
    }
}

package com.example.server;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;

import rank.Ranking;
import rank.RankingDao;
import rank.RankingDaoImpl;

public class RankServer {

    public static final int PORT = 8888;//端口
    private ServerSocket server = null;
    private String userName;
    private int score;
    private String degree;

    public RankServer() {

        //服务器创建流程如下
        try {
            InetAddress addr = InetAddress.getLocalHost();
            System.out.println("local host:" + addr);

            //1.创建ServerSocket
            server = new ServerSocket(PORT);
            //创建线程池
            System.out.println("--RankServer is opening--");
            while (true) {
                //2.等待接收请求   这里接收客户端的请求
                Socket client1 = server.accept();
                System.out.println("Get connection: " + client1);
                new Thread(new RankServer.Service(client1)).start();
                Socket client2 = server.accept();
                System.out.println("Get connection: " + client2);
                new Thread(new RankServer.Service(client2)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Service implements Runnable{

        private Socket client;
        private BufferedReader in = null;
        private String content = "";
        private RankingDao rankingDao;

        public Service(Socket socket) {
            client = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while ((content = in.readLine()) != null) {
                    System.out.println("message from client:"+content);
                    if(content.equals("bye")){
                        System.out.println("disconnect from client,close socket");
                        client.shutdownInput();
                        client.shutdownOutput();
                        client.close();
                    }
                    else {
                        String[] userData = content.split(" ");
                        userName = userData[0];
                        score = Integer.parseInt(userData[1]);
                        degree = userData[2];
                        //改成相应的绝对路径
                        rankingDao = new RankingDaoImpl("D:\\javaprogram\\AircraftWar2023\\Server\\server\\src\\main\\data\\RankingList_" + degree + ".txt");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Ranking newRanking = new Ranking(1, userName, score, sdf.format(System.currentTimeMillis()));
                        rankingDao.doAdd(newRanking);
                        rankingDao.doRank();
                        try {
                            rankingDao.storage();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        this.sendMessage(client);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        //向指定客户端发送消息
        public void sendMessage(Socket client) {
            PrintWriter pout = null;
            try{
                String message = "";
                for (Ranking item : rankingDao.getAll()) {
                    message = message + item.getPosition() + " " + item.getUserName() + " " + item.getScore() + " " + item.getTime() + ",";
                }
                System.out.println("message to client:" + message);
                pout = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(client.getOutputStream())),true);
                pout.println(message);
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
}

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BattleServer {

    public static final int PORT = 9999;//端口
    private Map<String,String> userScore = new HashMap<>();
    private Map<String,String> userHp = new HashMap<>();
    private Map<String,String> userDifficulty = new HashMap<>();
    private ServerSocket server = null;

    public BattleServer() {

        //服务器创建流程如下
        try {
            InetAddress addr = InetAddress.getLocalHost();
            System.out.println("local host:" + addr);

            //1.创建ServerSocket
            server = new ServerSocket(PORT);
            //创建线程池
            System.out.println("--BattleServer is opening--");
            while (true) {
                //2.等待接收请求   这里接收客户端的请求
                Socket client = server.accept();
                System.out.println("Get connection: " + client);

                //执行线程
                new Thread(new BattleServer.Service(client)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Service implements Runnable{

        private Socket client;
        private BufferedReader in = null;
        private String content = "";

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
                    String[] clientMessage = content.split(" ");
                    if(clientMessage[0].equals("bye")){
                        System.out.println("disconnect from client,close socket");
                        userScore.remove(clientMessage[1]);
                        userHp.remove(clientMessage[1]);
                        userDifficulty.remove(clientMessage[1]);
                        client.shutdownInput();
                        client.shutdownOutput();
                        client.close();
                    }
                    else {
                        String[] userData = content.split(" ");
                        userScore.put(userData[0], userData[1]);
                        userHp.put(userData[0], userData[2]);
                        userDifficulty.put(userData[0], userData[3]);
                        System.out.println(userScore);
                        System.out.println(userHp);
                        System.out.println(userDifficulty);
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

                Iterator<Map.Entry<String,String>> it1 = userScore.entrySet().iterator();
                Iterator<Map.Entry<String,String>> it2 = userHp.entrySet().iterator();
                Iterator<Map.Entry<String,String>> it3 = userDifficulty.entrySet().iterator();
                while (it1.hasNext() && it2.hasNext() && it3.hasNext()) {
                    Map.Entry<String,String> entry1 = it1.next();
                    Map.Entry<String,String> entry2 = it2.next();
                    Map.Entry<String,String> entry3 = it3.next();
                    message = message + entry3.getValue() + " Player:" + entry1.getKey() + " SCORE:" + entry1.getValue() + " LIFE:" + entry2.getValue() + " ";
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

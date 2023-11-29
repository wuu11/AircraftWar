package com.example.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LoginServer {

    public static final int PORT = 8899;//端口
    private ServerSocket server = null;

    public LoginServer() {

        //服务器创建流程如下
        try {
            InetAddress addr = InetAddress.getLocalHost();
            System.out.println("local host:" + addr);

            //1.创建ServerSocket
            server = new ServerSocket(PORT);
            //创建线程池
            System.out.println("--LoginServer is opening--");
            while (true) {
                //2.等待接收请求   这里接收客户端的请求
                Socket client = server.accept();
                System.out.println("Get connection: " + client);

                //执行线程
                new Thread(new Service(client)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Service implements Runnable {

        private Socket client;
        private BufferedReader in = null;
        private String content = "";
        private String name;
        private String password;

        public Service(Socket socket) {
            client = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while ((content = in.readLine()) != null) {
                    System.out.println("message from client:" + content);
                    if (content.equals("bye")) {
                        System.out.println("disconnect from client,close socket");
                        client.shutdownInput();
                        client.shutdownOutput();
                        client.close();
                    }
                    else {
                        String[] userData = content.split(" ");
                        name = userData[0];
                        password = userData[1];
                        System.out.println(name + " " + password);
                        String messageToClient = isUserExists() ? "Y" : "N";
                        this.sendMessage(client, messageToClient);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public boolean isUserExists() throws IOException {
            try {
                String path = "D:\\javaprogram\\AircraftWar2023\\Server\\server\\src\\main\\data\\users.txt"; //绝对路径
                //查看文件是否存在，若不存在，则新建
                File file = new File(path);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                List<String> lines = Files.readAllLines(Paths.get(path));
                if (lines.size() == 0) {
                    return false;
                }
                for (String item : lines) {
                    String[] temp = item.split(" ");
                    String tempUserName = temp[0];
                    String tempPasswd = temp[1];
                    if (tempUserName.equals(name) && tempPasswd.equals(password)) {
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        public void sendMessage(Socket socket, String message) {
            PrintWriter pout = null;
            try {
                System.out.println("message to client:" + message);
                pout = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())), true);
                pout.println(message);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
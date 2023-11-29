package edu.hitsz.application;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * 程序入口
 */
public class Main {
    static final java.awt.CardLayout cardLayout = new java.awt.CardLayout(0,0);
    static final JPanel cardPanel = new JPanel(cardLayout);

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;

    public static void main(String[] args) throws IOException {

        System.out.println("Hello Aircraft War");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame("Aircraft War");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setResizable(false);
        //设置窗口的大小和位置,居中放置
        frame.setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(cardPanel);

        StartMenu start = new StartMenu();
        cardPanel.add(start.getMainPanel());
        frame.setVisible(true);
    }
}

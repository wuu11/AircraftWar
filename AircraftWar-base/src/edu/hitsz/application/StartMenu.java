package edu.hitsz.application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class StartMenu {
    private JPanel mainPanel;
    private JButton easyButton;
    private JButton commonButton;
    private JButton difficultButton;
    private JComboBox<String> musicBox;
    private JLabel music;
    private JPanel topPanel;
    private JPanel bottomPanel;

    public StartMenu(){

        easyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractGame game = null;
                try {
                    game = new EasyGame();
                    Main.cardPanel.add(game);
                    game.action();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Main.cardLayout.last(Main.cardPanel);
            }
        });

        commonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractGame game = null;
                try {
                    game = new CommonGame();
                    Main.cardPanel.add(game);
                    game.action();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Main.cardLayout.last(Main.cardPanel);
            }
        });

        difficultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractGame game = null;
                try {
                    game = new DifficultGame();
                    Main.cardPanel.add(game);
                    game.action();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Main.cardLayout.last(Main.cardPanel);
            }
        });

        musicBox.addItem("关");
        musicBox.addItem("开");

        musicBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String item = (String) musicBox.getSelectedItem();
                if ("开".equals(item)) {
                    AbstractGame.musicOn = true;
                }else {
                    AbstractGame.musicOn = false;
                }
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

}

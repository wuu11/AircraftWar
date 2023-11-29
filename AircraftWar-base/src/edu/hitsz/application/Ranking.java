package edu.hitsz.application;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Ranking {

    private int position;

    private String userName;

    private int score;

    private String time;

    public Ranking(int position, String userName, int score, String time) {
        this.position = position;
        this.userName = userName;
        this.score = score;
        this.time = time;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    public String getTime() {
        return time;
    }
}

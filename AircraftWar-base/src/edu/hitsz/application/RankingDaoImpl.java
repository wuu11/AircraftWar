package edu.hitsz.application;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankingDaoImpl implements RankingDao {

    //获取数据
    private List<Ranking> rankings;

    private String path;

    public RankingDaoImpl(String path) throws IOException {
        rankings = new ArrayList<>();
        this.path = path;
        List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        for (String line : lines) {
            String[] split = line.split(" ");
            String time = split[3] + " " + split[4];
            Ranking ranking = new Ranking(Integer.parseInt(split[0]), split[1], Integer.parseInt(split[2]), time);
            rankings.add(ranking);
        }
    }

    @Override
    public List<Ranking> getAll() {
        return rankings;
    }

    @Override
    public void doAdd(Ranking ranking) {
        for (Ranking item : rankings) {
            if (ranking.getScore() <= item.getScore()) {
                ranking.setPosition(ranking.getPosition()+1);
            }
        }
        for (Ranking item : rankings) {
            if (item.getPosition() >= ranking.getPosition()){
                item.setPosition(item.getPosition()+1);
            }
        }
        rankings.add(ranking);
    }

    @Override
    public void doDelete(int position) {
        rankings.remove(rankings.get(position-1));
        for (Ranking item : rankings) {
            if (item.getPosition() > position) {
                item.setPosition(item.getPosition()-1);
            }
        }
    }

    @Override
    public void doRank() {
        Collections.sort(rankings, new Comparator<Ranking>() {
            @Override
            public int compare(Ranking o1, Ranking o2) {
                return Integer.compare(o1.getPosition(), o2.getPosition());
            }
        });
    }

    @Override
    public void storage() throws IOException {
        File f = new File(path);
        FileOutputStream fop = new FileOutputStream(f);
        OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");
        for (Ranking item : rankings) {
            writer.append(item.getPosition() + " " + item.getUserName() + " " + item.getScore() + " " + item.getTime());
            writer.append("\r\n");
        }
        writer.close();
        fop.close();
    }

}

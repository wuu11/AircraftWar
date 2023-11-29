package edu.hitsz.rankinglist;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class RankingDaoImpl implements RankingDao {

    //获取数据
    private List<Ranking> rankings;

    private OutputStreamWriter outputStreamWriter;

    private String path;

    public RankingDaoImpl(InputStreamReader inputStreamReader) {
        rankings = new ArrayList<>();

        BufferedReader reader = new BufferedReader(inputStreamReader);
        List<String> lines = new ArrayList<>();
        String line = null;

        while (true){
            try {
                if (!((line = reader.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            lines.add(line);
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (lines != null) {
            for (int i = 0; i < lines.size(); i++) {
                String[] split = lines.get(i).split(" ");
                String time = split[3] + " " + split[4];
                Ranking ranking = new Ranking(Integer.parseInt(split[0]), split[1], Integer.parseInt(split[2]), time);
                rankings.add(ranking);
            }
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
        OutputStreamWriter writer = outputStreamWriter;
        for (Ranking item : rankings) {
            writer.write(item.getPosition() + " " + item.getUserName() + " " + item.getScore() + " " + item.getTime());
            writer.write("\r\n");
        }
        writer.flush();
        writer.close();
    }

}

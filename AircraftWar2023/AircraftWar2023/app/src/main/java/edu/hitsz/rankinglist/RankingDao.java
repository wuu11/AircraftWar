package edu.hitsz.rankinglist;

import java.io.IOException;
import java.util.List;

public interface RankingDao {

    List<Ranking> getAll();

    void doAdd(Ranking ranking);

    void doDelete(int position);

    void doRank();

    void storage() throws IOException;
}

package edu.hitsz.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import edu.hitsz.R;
import edu.hitsz.game.BaseGame;
import edu.hitsz.rankinglist.Ranking;

public class OnlineRankingListActivity extends AppCompatActivity {

    private List<Ranking> rankings;

    private TableLayout tableLayout;

    private int row = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        TextView difficulty = findViewById(R.id.difficulty);
        Button delete_btn = findViewById(R.id.delete_button);
        tableLayout = findViewById(R.id.RankingList);
        rankings = new ArrayList<>();

        delete_btn.setVisibility(View.INVISIBLE);

        difficulty.setText("难度：" + RankingListActivity.degree);

        String[] lines = BaseGame.rankMessage.split(",");
        for (String line : lines) {
            String[] split = line.split(" ");
            String time = split[3] + " " + split[4];
            Ranking ranking = new Ranking(Integer.parseInt(split[0]), split[1], Integer.parseInt(split[2]), time);
            rankings.add(ranking);
        }

        drawTable(rankings.size());
    }

    private void drawTable(int num) {

        for (int i = 0; i < num; i++) {
            Ranking item = rankings.get(i);

            // 新建一个TableRow并设置样式
            TableRow newRow = new TableRow(getApplicationContext());
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            newRow.setLayoutParams(layoutParams);

            //新建一个LinearLayout
            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            linearLayout.setLayoutParams(lp);

            // 底部边框的宽度
            int bottomLine = dip2px(getApplicationContext(), 1);
            if(i == num - 1) {
                // 如果当前行是最后一行, 则底部边框加粗
                bottomLine = dip2px(getApplicationContext(), 2);
            }

            // 第一列
            TextView tvPosition = new TextView(getApplicationContext());
            // 设置文字居中
            tvPosition.setGravity(Gravity.CENTER);
            // 设置表格中的数据不自动换行
            tvPosition.setSingleLine();
            // 设置边框和weight
            LinearLayout.LayoutParams lpPosition = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            lpPosition.setMargins(dip2px(getApplicationContext(), 2), 0, dip2px(getApplicationContext(), 1), bottomLine);
            tvPosition.setLayoutParams(lpPosition);
            // 设置背景颜色
            tvPosition.setBackgroundColor(Color.parseColor("#FFFFFF"));
            // 填充文字数据
            tvPosition.setText(String.valueOf(item.getPosition()));

            // 第二列
            TextView tvName = new TextView(getApplicationContext());
            // 设置文字居中
            tvName.setGravity(Gravity.CENTER);
            // 设置表格中的数据不自动换行
            tvName.setSingleLine();
            // 设置边框和weight
            LinearLayout.LayoutParams lpName = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 2);
            lpName.setMargins(0, 0, dip2px(getApplicationContext(), 1), bottomLine);
            tvName.setLayoutParams(lpName);
            // 设置背景颜色
            tvName.setBackgroundColor(Color.parseColor("#FFFFFF"));
            // 填充文字数据
            tvName.setText(item.getUserName());

            // 第三列
            TextView tvScore = new TextView(getApplicationContext());
            // 设置文字居中
            tvScore.setGravity(Gravity.CENTER);
            // 设置表格中的数据不自动换行
            tvScore.setSingleLine();
            // 设置边框和weight
            LinearLayout.LayoutParams lpScore = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            lpScore.setMargins(0, 0, dip2px(getApplicationContext(), 1), bottomLine);
            tvScore.setLayoutParams(lpScore);
            // 设置背景颜色
            tvScore.setBackgroundColor(Color.parseColor("#FFFFFF"));
            // 填充文字数据
            tvScore.setText(String.valueOf(item.getScore()));

            // 第四列
            TextView tvTime = new TextView(getApplicationContext());
            // 设置文字居中
            tvTime.setGravity(Gravity.CENTER);
            // 设置表格中的数据不自动换行
            tvTime.setSingleLine();
            // 设置边框和weight
            LinearLayout.LayoutParams lpTime = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 4);
            lpTime.setMargins(0, 0, dip2px(getApplicationContext(), 2), bottomLine);
            tvTime.setLayoutParams(lpTime);
            // 设置背景颜色
            tvTime.setBackgroundColor(Color.parseColor("#FFFFFF"));
            // 填充文字数据
            tvTime.setText(item.getTime());

            int finalI = i;
            newRow.setOnClickListener(view -> {
                row = finalI;
                tvPosition.setBackgroundColor(Color.rgb(240,240,255));
                tvName.setBackgroundColor(Color.rgb(240,240,255));
                tvScore.setBackgroundColor(Color.rgb(240,240,255));
                tvTime.setBackgroundColor(Color.rgb(240,240,255));
            });

            // 将所有新的组件加入到对应的视图中
            linearLayout.addView(tvPosition);
            linearLayout.addView(tvName);
            linearLayout.addView(tvScore);
            linearLayout.addView(tvTime);
            newRow.addView(linearLayout);
            tableLayout.addView(newRow);
        }
    }

    private int dip2px(Context context, int dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}

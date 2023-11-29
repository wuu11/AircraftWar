package edu.hitsz.application;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;

public class RankingList {
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JScrollPane tableScrollPanel;
    private JTable rankingTable;
    private JButton deleteButton;
    private JLabel difficulty;
    private JLabel rankingList;

    public RankingList(String degree) throws IOException {

        String[] columnName = {"名次","玩家名","得分","记录时间"};
        String[][] tableData;
        String path;

        difficulty.setText("难度：" + degree);
        path = "out/RankingList_" + degree + ".txt";

        List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        tableData = new String[lines.size()][4];
        int i = 0;
        for (String line : lines) {
            String[] split = line.split(" ");
            String time = split[3] + " " + split[4];
            System.arraycopy(split, 0, tableData[i], 0, 3);
            tableData[i][3] = time;
            i++;
        }

        RankingDao rankingDao = new RankingDaoImpl(path);

        //表格模型
        DefaultTableModel model = new DefaultTableModel(tableData, columnName){
            @Override
            public boolean isCellEditable(int row, int col){
                return false;
            }
        };

        //设置表格内容居中
        DefaultTableCellRenderer dcr = new DefaultTableCellRenderer();
        dcr.setHorizontalAlignment(SwingConstants.CENTER);
        rankingTable.setDefaultRenderer(Object.class, dcr);

        //JTable并不存储自己的数据，而是从表格模型那里获取它的数据
        rankingTable.setModel(model);
        tableScrollPanel.setViewportView(rankingTable);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = rankingTable.getSelectedRow();
                System.out.println(row);
                int result = JOptionPane.showConfirmDialog(deleteButton,
                        "是否确定中删除？");
                if (JOptionPane.YES_OPTION == result && row != -1) {
                    model.removeRow(row);
                    rankingDao.doDelete(row + 1);
                    try {
                        rankingDao.storage();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    for (int i = row; i < model.getRowCount(); i++) {
                        model.setValueAt(tableData[i][0], i, 0);
                    }
                }
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

}


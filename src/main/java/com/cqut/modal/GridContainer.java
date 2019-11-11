package main.java.com.cqut.modal;


import main.java.com.cqut.config.Config;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GridContainer {
    public  final HashMap<Integer, List<Block>> gridMap;
    public  final HashMap<Integer, Integer>     everyRowSize;
    public GridContainer() {
        gridMap = new HashMap<>();
        everyRowSize = new HashMap<>();
    }

    /**
     * 每当有新的方块组添加进来时，都要判断添加进来后
     * 1.容器是否会满（游戏结束的标志）
     * 2.是否发生消除行为
     * 若上述条件均不符合，则执行添加操作，并更新容器
     * @param addNewBlocks
     */
    public void refresh(Block[] addNewBlocks) {
        int firstMakeRowFullIndex = -1;
        for (Block b : addNewBlocks) {
            int y = b.getY();
            // 若容器已满
            if (y == 0) {
                System.out.println("GAME OVER ！");
                System.exit(0);
            }

            Integer oldSize = everyRowSize.get(y);
            if (oldSize == null) { // 若还没有该行记录
                List<Block> list = new ArrayList<>(Config.EVERY_ROW_FULL_SIZE);
                list.add(b);
                gridMap.put(y,list);
                everyRowSize.put(y, 1);
                continue;
            }
            gridMap.get(y).add(new Block(b));
            int newSize = oldSize + 1;
            if (newSize == Config.EVERY_ROW_FULL_SIZE) {
                firstMakeRowFullIndex = firstMakeRowFullIndex == -1 ? y : firstMakeRowFullIndex;
            }
            everyRowSize.put(y, newSize);
        }

        // 判断当前的插入操作是否会引起行消除操作
        if (firstMakeRowFullIndex == -1) {
            print();
            return;
        }
        int checkCount = 1;
        while (checkCount != 0) {
            if (everyRowSize.get(firstMakeRowFullIndex) == Config.EVERY_ROW_FULL_SIZE) { // 判断该行是否已满
                clearLastRow(firstMakeRowFullIndex);
                continue;
            }
            checkCount--;
        }
        print();
    }

    /**
     * 进行当前行清除以及清除后的一些操作
     * @param row
     */
    private void clearLastRow(int row) {
        everyRowSize.remove(row);
        gridMap.remove(row);

        // 更新gridMap和everyRowSize（让删除行之上的行下移）
        List<Integer> collect = gridMap.keySet().stream()
                                    .filter(key -> key < row).collect(Collectors.toList());

        if (collect == null) {
            return;
        }
        int curRow = row;
        int nextRow = 0;
        for (int i = 0; i < collect.size(); i++) {
            nextRow = curRow - Config.BLOCK_SIZE;
            List<Block> list = gridMap.get(nextRow);
            for (Block b : list) {
                b.autoAddY();
            }
            gridMap.put(curRow,list);
            everyRowSize.put(curRow, everyRowSize.get(nextRow));
            curRow = nextRow;
        }
        gridMap.remove(curRow);
        everyRowSize.remove(curRow);
        System.out.println("=====================");
        System.out.println("执行清除后：");
        print();
    }

    // for test
    private void print() {
        System.out.println("---------------------------");
        System.out.println("当前：");
        for (List<Block> list : gridMap.values()) {
            for (Block b : list) {
                System.out.print(b.toString() + "--->");
            }
            System.out.println();
        }
        System.out.println("---------------------------");
    }


    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        for (List<Block> list : gridMap.values()) {
            if (list != null) {
                for (Block b : list) {
                    g.fillRect(b.getX(), b.getY(), Config.BLOCK_SIZE, Config.BLOCK_SIZE);
                }
            }
        }
    }
}

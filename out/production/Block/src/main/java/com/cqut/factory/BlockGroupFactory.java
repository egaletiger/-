package main.java.com.cqut.factory;

import main.java.com.cqut.config.Config;
import main.java.com.cqut.frame.GameFrame;
import main.java.com.cqut.modal.Block;
import main.java.com.cqut.modal.BlockGroup;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BlockGroupFactory {
    private static List<Block[]> blockGroupList = new ArrayList<>(10);
    static {
        Block[] line1 = {
                new Block(Config.BLOCK_SIZE,0), // 旋转中心在第一位
                new Block(0,0),
                new Block(Config.BLOCK_SIZE * 2,0),
                new Block(Config.BLOCK_SIZE * 3,0)
        };

        blockGroupList.add(line1);

        Block[] line2 = {
                new Block(0,Config.BLOCK_SIZE), // 旋转中心在第一位
                new Block(0,0),
                new Block(0,Config.BLOCK_SIZE * 2),
                new Block(0,Config.BLOCK_SIZE * 3)
        };

        blockGroupList.add(line2);

        Block[] square = {  // 无旋转动作
                new Block(0,0),
                new Block(Config.BLOCK_SIZE,0),
                new Block(0, Config.BLOCK_SIZE),
                new Block(Config.BLOCK_SIZE,Config.BLOCK_SIZE)
        };

        blockGroupList.add(square);

        Block[] Z1 = {
                new Block(Config.BLOCK_SIZE,0), // 旋转中心在第一位
                new Block(0,0),
                new Block(Config.BLOCK_SIZE, Config.BLOCK_SIZE),
                new Block(Config.BLOCK_SIZE * 2, Config.BLOCK_SIZE)
        };

        blockGroupList.add(Z1);

        Block[] Z2 = {
                new Block(0, Config.BLOCK_SIZE), // 旋转中心在第一位
                new Block(Config.BLOCK_SIZE,0),
                new Block(Config.BLOCK_SIZE, Config.BLOCK_SIZE),
                new Block(0, Config.BLOCK_SIZE * 2)
        };

        blockGroupList.add(Z2);

        Block[] T1 = {
                new Block(Config.BLOCK_SIZE, Config.BLOCK_SIZE), // 旋转中心在第一位
                new Block(Config.BLOCK_SIZE,0),
                new Block(0, Config.BLOCK_SIZE),
                new Block(Config.BLOCK_SIZE * 2, Config.BLOCK_SIZE)
        };

        blockGroupList.add(T1);

        Block[] T2 = {
                new Block(Config.BLOCK_SIZE, 0), // 旋转中心在第一位
                new Block(0,0),
                new Block(Config.BLOCK_SIZE * 2, 0),
                new Block(Config.BLOCK_SIZE, Config.BLOCK_SIZE)
        };

        blockGroupList.add(T2);

        Block[] L1 = {
                new Block(Config.BLOCK_SIZE, Config.BLOCK_SIZE),// 旋转中心在第一位
                new Block(0,0),
                new Block(0, Config.BLOCK_SIZE),
                new Block(Config.BLOCK_SIZE * 2, Config.BLOCK_SIZE)
        };

        blockGroupList.add(L1);

        Block[] L2 = {
                new Block(Config.BLOCK_SIZE, Config.BLOCK_SIZE),// 旋转中心在第一位
                new Block(Config.BLOCK_SIZE * 2,0),
                new Block(0, Config.BLOCK_SIZE),
                new Block(Config.BLOCK_SIZE * 2, Config.BLOCK_SIZE)
        };

        blockGroupList.add(L2);

    }

    private final  BlockGroup blockGroup;
    private final  GameFrame  frame;
    private final  Color[]    colors;
    private final  Random     ran;
    public BlockGroupFactory(GameFrame frame) {
        this.frame = frame;
        blockGroup = new BlockGroup(frame);
        colors = new Color[]{Color.RED, Color.orange, Color.BLUE, Color.GREEN};
        ran = new Random();
    }

    public BlockGroup createBlockGroup() {
        // 解除引用联系，模仿享元设计模式
        Block[] blocks = cloneBlockArray(blockGroupList.get(ran.nextInt(blockGroupList.size())));

        Color color = colors[ran.nextInt(colors.length)];
        blockGroup.setColor(color);
        blockGroup.setBlocks(blocks);
        return blockGroup;
    }

    private Block[] cloneBlockArray(Block[] blocks) {
        Block[] clone = new Block[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            clone[i] = new Block(blocks[i]);
        }
        return clone;
    }
}

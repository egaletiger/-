package main.java.com.cqut.modal;

import main.java.com.cqut.config.Config;
import main.java.com.cqut.frame.GameFrame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;

public class BlockGroup {
    private final GameFrame          frame;
    private final GridContainer      container;
    private       Block[]            blocks;
    private       Block[]            blocksMemento;
    private       Color              color;
    public BlockGroup(GameFrame frame){
        this.frame = frame;
        this.container = frame.getBlockContainer();
    }

    public void setBlocks(Block[] blocks) {
        this.blocks = blocks;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void moveLeft(){
        if (canMoveLeft()) {
            for (int i = 0; i < blocks.length; i++) {
                blocks[i].autoSubtractX();
            }
        }
    }

    public void moveRight(){
       if (canMoveRight()) {
           for (int i = 0; i < blocks.length; i++) {
               blocks[i].autoAddX();
           }
       }
    }


    public void moveDown(){
        if (canMoveDown()) {
            for (int i = 0; i < blocks.length; i++) {
                blocks[i].autoAddY();
            }
        }else {   // 产生堆积行为
            container.refresh(blocks);
//            blocks = null;
            frame.updateBlockGroup();
        }
    }


    private boolean canMoveRight() {
        return isOverLRBound(Config.BLOCK_MOVE_DISTANCE);
    }

    private boolean canMoveLeft() {
        return isOverLRBound(-Config.BLOCK_MOVE_DISTANCE);
    }

    private boolean canMoveDown() {
        return isMakeHeapUp();
    }

    public void rotate() {
       if(canRotate()) {
           blocks = blocksMemento;
           blocksMemento = null;
       }
    }

    /**
     * 顺时针旋转公式
     *      newX = 0.y + 0.x - b.y
     *      newY = 0.y -0.x + b.y
     *      x1=y-y0+x0,y1=x0-x+y0
     * 备忘录模式存储每一个旋转成功的方块的属性，
     * 旋转成功之后让blocks指向blocksMemento，
     * 但是若有一个方块没旋转成功，之前的所有备注都将清空。
     * @return
     */
    private boolean canRotate() {
        HashMap<Integer, List<Block>> gridMap = container.gridMap;
        blocksMemento = new Block[blocks.length]; // 模仿备忘录设计模式
        blocksMemento[0] = blocks[0];
        for (int i = 1; i < blocks.length; i++) {
            int newX = blocks[i].getY() - blocks[0].getY() + blocks[0].getX();
            int newY = blocks[0].getX() - blocks[i].getX() + blocks[0].getY();
            // 判断旋转后是否会出界
            if (newX < 0 || newY < 0 || newX > frame.getWidth() - Config.BLOCK_SIZE
                         || newY > frame.getHeight() - Config.BLOCK_SIZE ) {
                blocksMemento = null; // help gc
                return false;
            }

            // 判断旋转后的位置是否已有方块
            List<Block> blockList = gridMap.get(newY);
            if (blockList != null) {
                for (Block block : blockList) {
                    if (block.getX() == newX) {
                        blocksMemento = null; // help gc
                        return false;
                    }
                }
            }
            blocksMemento[i] = new Block(newX,newY);
        }
        return true;
    }


    /**
     *  判断是否能产生堆积行为， 如果产生了堆积行为，就应该进一步考虑消除行为 -- 职责链？
     *  1. 判断每个组内方块下一次移动的坐标，所对应的container里的位置是否已有值
     *  元方块组内只对裸露的方块进行检测即可
     *  2. 向下是否越界， 是则应堆积该方块组
     *
     * @return
     */
    private boolean isMakeHeapUp(){
        HashMap<Integer, List<Block>> gridMap = container.gridMap;
        for (int i = 0; i < blocks.length; i++) {
                // 判断方块是否已到达底部
                int y = blocks[i].getY() + Config.BLOCK_MOVE_DISTANCE;
                if (y > frame.getHeight() - Config.BLOCK_SIZE) {
                    return false;
                }

                // 判断方块下方是否有障碍
                int x = blocks[i].getX();
                List<Block> blockList = gridMap.get(y);
                if (blockList != null) {
                    for (Block block : blockList) {
                        if (block.getX() == x) { // 方块下方有障碍出现堆积行为
                            return false;
                        }
                    }
                }
            }
        return true;
    }

    /**
     * 判断方块左右移动是否有越界行为
     *  1.方块组越过界面的最右，或最右边界， 是则应该限制
     *  2. 向左或向右移动是否有障碍， 是则取消移动
     * @return
     */
    private boolean isOverLRBound(int nextOffsetOfX) {
        HashMap<Integer, List<Block>> gridMap = container.gridMap;
        for (int i = 0; i < blocks.length; i++) {
            // 判断是否越过左右边界
            int x = blocks[i].getX() + nextOffsetOfX;
            if (x < 0 || x > frame.getWidth() - Config.BLOCK_SIZE) {
                return false;
            }

            // 判断左右是否有障碍
            int y = blocks[i].getY();
            List<Block> blockList = gridMap.get(y);

            if (blockList != null) {
                for (Block block : blockList) {
                    if (block.getX() == x) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        for (Block b : blocks) {
            g.fillRect(b.getX(), b.getY(), Config.BLOCK_SIZE, Config.BLOCK_SIZE);
        }
    }

    public void keyAction(KeyEvent e) {
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_RIGHT:
                moveRight();
                break;
            case KeyEvent.VK_LEFT:
                moveLeft();
                break;
            case KeyEvent.VK_UP:
                rotate();
                break;
            case KeyEvent.VK_DOWN:
                moveDown();
                break;
        }
    }

    // for test
    public Block[] getBlocks() {
        return this.blocks;
    }
}

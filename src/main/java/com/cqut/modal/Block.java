package main.java.com.cqut.modal;

import main.java.com.cqut.config.Config;


public class Block {
    private int x;
    private int y;

    public Block(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Block(Block block) {
        this.x = block.getX();
        this.y = block.getY();
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void autoAddX() {
        x += Config.BLOCK_MOVE_DISTANCE;
    }

    public void autoSubtractX() {
        x -=  Config.BLOCK_MOVE_DISTANCE;
    }

    public void autoAddY() {
        y +=  Config.BLOCK_MOVE_DISTANCE;
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }
}

package main.java.com.cqut.frame;

import main.java.com.cqut.config.Config;
import main.java.com.cqut.factory.BlockGroupFactory;
import main.java.com.cqut.modal.Block;
import main.java.com.cqut.modal.BlockGroup;
import main.java.com.cqut.modal.GridContainer;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameFrame extends Frame {
    private final GridContainer     gridContainer;
    private final BlockGroupFactory factory;
    private       BlockGroup        blockGroup;
    private       Image             offBackgroundImage = null;
    public GameFrame() {
        gridContainer = new GridContainer();
        factory = new BlockGroupFactory(this);
        this.setSize(Config.FRAME_WIDTH, Config.FRAME_HEIGHT);
        this.setVisible(true);
        this.setResizable(false);
        this.setBackground(Color.BLACK);
        this.setTitle("俄罗斯方块");
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.addKeyListener(new GameKeyListener());
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (blockGroup == null) {
            blockGroup = factory.createBlockGroup();
        }
        Color color = g.getColor();
        blockGroup.draw(g);
        gridContainer.draw(g);
        g.setColor(color);
        blockGroup.moveDown();
    }

    // 双缓冲消除闪烁问题
    @Override
    public void update(Graphics g) {
        if(offBackgroundImage == null)
        {
            offBackgroundImage = this.createImage(800,600);
        }
        Graphics gOffScreen = offBackgroundImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.BLACK);
        gOffScreen.fillRect(0, 0, 800, 600);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offBackgroundImage, 0, 0, null);
    }

    public GridContainer getBlockContainer() {
        return  this.gridContainer;
    }

    public void updateBlockGroup() {
        blockGroup = factory.createBlockGroup();
        System.out.println("**********************");
        System.out.println("下一个");
        for (Block b : blockGroup.getBlocks()){
            System.out.print(b.toString() + "--->");
        }

        System.out.println();
        System.out.println("**********************");
    }

    private class GameKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            blockGroup.keyAction(e);
        }
    }
}

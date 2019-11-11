package main.java.com.cqut.controller;

import main.java.com.cqut.frame.GameFrame;

import java.util.concurrent.TimeUnit;

public class GameController {
    public static void main(String[] args) {
        GameFrame frame = new GameFrame();
        while(true)
        {
            frame.repaint();
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
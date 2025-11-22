package com.game_adventure.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class ScoreRenderer {

    private final Font scoreFont;

    public ScoreRenderer() {
        this.scoreFont = new Font("Arial", Font.BOLD, 20);
    }

    public void draw(Graphics g, int score) {
        g.setFont(scoreFont);
        
        String text = "SCORE: " + score;
        int x = 20; 
        int y = 40; 

        // 그림자
        g.setColor(Color.BLACK);
        g.drawString(text, x + 2, y + 2);

        // 본문
        g.setColor(Color.WHITE); 
        g.drawString(text, x, y);
    }
}
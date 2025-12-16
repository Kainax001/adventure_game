package com.game_adventure.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class ScoreRenderer {

    private final Font scoreFont; // 점수 표시용 폰트

    public ScoreRenderer() { // 기본 폰트 설정
        this.scoreFont = new Font("Arial", Font.BOLD, 20);
    }

    public void draw(Graphics g, int score) {
        g.setFont(scoreFont);
        
        String text = "SCORE: " + score; // 표시할 점수 문자열
        int x = 20; // 왼쪽 상단 여백
        int y = 40; // 위쪽 여백

        // 그림자
        g.setColor(Color.BLACK);
        g.drawString(text, x + 2, y + 2);

        // 본문
        g.setColor(Color.WHITE); 
        g.drawString(text, x, y);
    }
}
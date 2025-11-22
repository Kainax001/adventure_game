package com.game_adventure.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class GameStatusRenderer {

    private final Font titleFont;
    private final Font subFont;

    public GameStatusRenderer() {
        this.titleFont = new Font("Arial", Font.BOLD, 50); // 큰 폰트
        this.subFont = new Font("Arial", Font.PLAIN, 20);  // 작은 폰트
    }

    // 1. 게임 오버 화면
    public void drawGameOver(Graphics g, int panelWidth, int panelHeight) {
        drawDimmedBackground(g, panelWidth, panelHeight);
        
        g.setColor(Color.RED);
        drawCenteredString(g, "GAME OVER", panelWidth, panelHeight, -30, titleFont);
        
        g.setColor(Color.WHITE);
        drawCenteredString(g, "Press 'Q' to Quit", panelWidth, panelHeight, 30, subFont);
    }

    // 2. 레벨 클리어 화면
    public void drawLevelClear(Graphics g, int panelWidth, int panelHeight) {
        drawDimmedBackground(g, panelWidth, panelHeight);

        g.setColor(Color.GREEN); // 혹은 노란색
        drawCenteredString(g, "LEVEL CLEAR!", panelWidth, panelHeight, -30, titleFont);

        g.setColor(Color.WHITE);
        drawCenteredString(g, "Press 'Y' for Next Level", panelWidth, panelHeight, 30, subFont);
    }

    // 3. 종료 확인 화면
    public void drawQuitConfirmation(Graphics g, int panelWidth, int panelHeight) {
        drawDimmedBackground(g, panelWidth, panelHeight);

        g.setColor(Color.WHITE);
        drawCenteredString(g, "Quit Game?", panelWidth, panelHeight, -30, titleFont);
        drawCenteredString(g, "Yes (Y) / No (N)", panelWidth, panelHeight, 30, subFont);
    }

    // [Helper] 배경을 반투명 검은색으로 덮기
    private void drawDimmedBackground(Graphics g, int w, int h) {
        g.setColor(new Color(0, 0, 0, 150)); // 마지막 150이 투명도 (0~255)
        g.fillRect(0, 0, w, h);
    }

    // [Helper] 화면 중앙 정렬 텍스트 그리기
    private void drawCenteredString(Graphics g, String text, int w, int h, int yOffset, Font font) {
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        
        int x = (w - metrics.stringWidth(text)) / 2;
        int y = (h - metrics.getHeight()) / 2 + metrics.getAscent() + yOffset;
        
        g.drawString(text, x, y);
    }
}
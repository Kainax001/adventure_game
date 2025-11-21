package com.game_adventure.ui; // ui 패키지를 새로 파서 넣는 것을 추천합니다.

import com.game_adventure.entity.Stats;
import java.awt.Color;
import java.awt.Graphics;

public class HealthBarRenderer {
    private final Color hpColor;
    private final Color bgColor;
    
    // 생성자에서 색상을 설정받음
    public HealthBarRenderer(Color hpColor) {
        this.hpColor = hpColor;
        this.bgColor = new Color(50, 50, 50); // 기본 배경색 짙은 회색
    }

    // 그리기 메서드
    public void draw(Graphics g, int x, int y, int tileSize, Stats stats) {
        if (stats == null || stats.isDead()) return;

        int currentHp = stats.getCurrentHp();
        int maxHp = stats.getMaxHp();

        // 1. 픽셀 좌표 및 크기 계산
        int pixelX = x * tileSize;
        int pixelY = y * tileSize;

        int barWidth = (int)(tileSize * 1.2); 
        int barHeight = 3;                    
        
        int barXOffset = (tileSize - barWidth) / 2; 
        int barX = pixelX + barXOffset;
        int barY = pixelY - barHeight - 1; // 타일 위에 약간 띄워서 그리기

        double hpRatio = (double)currentHp / (double)maxHp;
        int filledWidth = (int)(barWidth * hpRatio);

        // 2. 그리기
        // 배경
        g.setColor(bgColor);
        g.fillRect(barX, barY, barWidth, barHeight);

        // 체력
        g.setColor(hpColor);
        g.fillRect(barX, barY, filledWidth, barHeight);
    }
}
package com.game_adventure.core;

import com.game_adventure.map.Dungeon;
import com.game_adventure.map.Tile;
import com.game_adventure.entity.Player;

import javax.swing.JPanel; // Swing의 '도화지'
import java.awt.Graphics; // 그리기 도구
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font; // [추가] 폰트 import

// GamePanel은 JPanel(도화지)을 상속받는다
public class GamePanel extends JPanel {

    private Dungeon dungeon;
    private static final int TILE_SIZE = 16; // 타일 1개의 픽셀 크기

    private boolean showQuitMessage = false;

    public GamePanel(Dungeon dungeon) {
        this.dungeon = dungeon;

        // 이 패널(도화지)의 권장 크기 설정
        int width = dungeon.getWidth() * TILE_SIZE;
        int height = dungeon.getHeight() * TILE_SIZE;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK); // 맵 밖은 검은색
    }

    /**
     * [추가 2] Game 클래스가 호출할 메서드 (캡슐화)
     */
    public void setShowQuitMessage(boolean show) {
        this.showQuitMessage = show;
    }

    /**
     * (중요) Swing이 화면을 다시 그려야 할 때마다 호출하는 메서드
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // 부모(JPanel)의 그리기 작업 우선 수행

        if (dungeon == null) return;

        // 1. 모든 타일 그리기 (다형성)
        for (int y = 0; y < dungeon.getHeight(); y++) {
            for (int x = 0; x < dungeon.getWidth(); x++) {
                Tile tile = dungeon.getTile(x, y);
                if (tile != null) {
                    // GamePanel은 타일이 Wall인지 Floor인지 모름
                    // 그저 "너 자신을 그려!" 라고 명령 (다형성)
                    tile.draw(g, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE);
                }
            }
        }

        // 2. 플레이어 그리기 (다형성)
        Player player = dungeon.getPlayer();
        if (player != null) {
            // 플레이어에게 "너 자신을 그려!" 라고 명령
            player.draw(g, TILE_SIZE);
        }

        // [추가 4] "종료 확인 대기 중"일 때 화면 위에 텍스트 그리기
        if (showQuitMessage) {
            // 반투명한 검은색 배경
            g.setColor(new Color(0, 0, 0, 150)); // R,G,B,Alpha(투명도)
            g.fillRect(0, 0, getWidth(), getHeight());

            // 흰색 텍스트
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            String msg = "You really want to Quit? (Y/N)";

            // 텍스트를 화면 중앙에 정렬
            int strWidth = g.getFontMetrics().stringWidth(msg);
            g.drawString(msg, (getWidth() - strWidth) / 2, getHeight() / 2);
        }
    }
}
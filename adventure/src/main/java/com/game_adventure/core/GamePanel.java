package com.game_adventure.core;

import com.game_adventure.map.Dungeon;
import com.game_adventure.map.Tile;
import com.game_adventure.entity.Enemy;
import com.game_adventure.entity.Player;
import com.game_adventure.map.ExitTile;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class GamePanel extends JPanel {

    private Dungeon dungeon;
    private static final int TILE_SIZE = 16;
    
    private boolean showQuitMessage = false;
    private boolean showWinMessage = false; // 승리 메시지 표시 플래그

    public GamePanel(Dungeon dungeon) {
        this.dungeon = dungeon;
        int width = dungeon.getWidth() * TILE_SIZE;
        int height = dungeon.getHeight() * TILE_SIZE;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK); 
    }

    // --- [Setter 및 Getter 메서드] ---

    public void setDungeon(Dungeon newDungeon) {
        this.dungeon = newDungeon;
        int width = newDungeon.getWidth() * TILE_SIZE;
        int height = newDungeon.getHeight() * TILE_SIZE;
        setPreferredSize(new Dimension(width, height));
    }
    
    public void setShowQuitMessage(boolean show) {
        this.showQuitMessage = show;
    }
    
    public void setShowWinMessage(boolean show) {
        this.showWinMessage = show;
    }

    public boolean isPlayerAtExit() {
        if (dungeon == null) return false;
        
        Player player = dungeon.getPlayer();
        ExitTile exitTile = dungeon.getExitTile();
        
        if (player != null && exitTile != null) {
             return player.getX() == exitTile.getX() && player.getY() == exitTile.getY();
        }
        return false;
    }

    // --- [그리기 (렌더링) 메서드] ---

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 

        if (dungeon == null) return;

        // 1. 모든 타일 그리기 
        drawMapTiles(g);

        // 2. 플레이어 그리기
        drawPlayer(g);

        // 3. 모든 적 그리기
        drawEnemies(g);

        // 4. 오버레이 메시지 그리기 (항상 마지막에 그려져야 함)
        drawOverlayMessages(g);
    }
    
    // --- [헬퍼 그리기 메서드] ---
    
    private void drawMapTiles(Graphics g) {
        for (int y = 0; y < dungeon.getHeight(); y++) {
            for (int x = 0; x < dungeon.getWidth(); x++) {
                Tile tile = dungeon.getTile(x, y);
                if (tile != null) {
                    tile.draw(g, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE);
                }
            }
        }
        // ExitTile은 Tile 배열에 있지만, 확실한 오버레이를 위해 다시 그릴 수 있습니다.
        ExitTile exitTile = dungeon.getExitTile();
        if (exitTile != null) {
            exitTile.draw(g, exitTile.getX() * TILE_SIZE, exitTile.getY() * TILE_SIZE, TILE_SIZE);
        }
    }
    
    private void drawPlayer(Graphics g) {
        Player player = dungeon.getPlayer();
        if (player != null) {
            player.draw(g, TILE_SIZE);
        }
    }

    private void drawEnemies(Graphics g) { // 메서드 이름을 복수로 변경하는 것이 좋습니다.
        // Dungeon 클래스의 getEnemies()가 List<Enemy>를 반환한다고 가정
        // [수정] getEnemy() 대신 getEnemies() 호출
        if (dungeon.getEnemies() != null) { 
            for (Enemy enemy : dungeon.getEnemies()) { // 리스트를 순회
                if (enemy != null) {
                    enemy.draw(g, TILE_SIZE);
                }
            }
        }
    }

    private void drawOverlayMessages(Graphics g) {
        // [수정] 승리 메시지: showWinMessage 플래그로만 제어됨 (isPlayerAtExit 로직 제거됨)
        if (showWinMessage) { 
            drawCenteredMessage(g, "Level Cleared! Next level? (Y/N)", 30, 
                                new Color(0, 0, 0, 150), 
                                new Color(255, 255, 255, 250));
        }
        
        // 종료 확인 메시지 표시
        if (showQuitMessage) {
            drawCenteredMessage(g, "You really want to Quit? (Y/N)", 20, 
                                new Color(0, 0, 0, 150), 
                                Color.WHITE);
        }
    }
    
    // 텍스트를 중앙에 정렬하여 그리는 범용 헬퍼 메서드
    private void drawCenteredMessage(Graphics g, String msg, int fontSize, Color bgColor, Color textColor) {
        // 배경 사각형
        g.setColor(bgColor); 
        g.fillRect(0, 0, getWidth(), getHeight()); 

        // 텍스트
        g.setColor(textColor);
        g.setFont(new Font("Arial", Font.BOLD, fontSize));

        int strWidth = g.getFontMetrics().stringWidth(msg);
        g.drawString(msg, (getWidth() - strWidth) / 2, getHeight() / 2);
    }
}
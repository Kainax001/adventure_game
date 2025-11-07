package com.game_adventure.core;

import com.game_adventure.map.Dungeon;
import com.game_adventure.map.Tile;
import com.game_adventure.entity.Player;
import com.game_adventure.map.ExitTile;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

// GamePanel은 JPanel(도화지)을 상속받는다
public class GamePanel extends JPanel {

    private Dungeon dungeon;
    private static final int TILE_SIZE = 16;
    
    // Game 클래스의 상태를 반영하는 플래그
    private boolean showQuitMessage = false;
    // 승리 메시지를 표시할지 여부를 결정하는 플래그 (Game 클래스에서 타이머와 연동)
    private boolean showWinMessage = false; 

    public GamePanel(Dungeon dungeon) {
        this.dungeon = dungeon;

        // 이 패널(도화지)의 권장 크기 설정
        int width = dungeon.getWidth() * TILE_SIZE;
        int height = dungeon.getHeight() * TILE_SIZE;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK); 
    }

    // --- [Setter 및 Getter 메서드] ---

    /**
     * Game 클래스가 Dungeon 객체를 업데이트할 수 있는 Setter
     */
    public void setDungeon(Dungeon newDungeon) {
        this.dungeon = newDungeon;
        // 새 맵 크기에 맞춰 패널 크기 업데이트
        int width = newDungeon.getWidth() * TILE_SIZE;
        int height = newDungeon.getHeight() * TILE_SIZE;
        setPreferredSize(new Dimension(width, height));
    }
    
    /**
     * Game 클래스가 종료 메시지 표시 여부를 설정하는 Setter
     */
    public void setShowQuitMessage(boolean show) {
        this.showQuitMessage = true;
    }
    
    /**
     * [추가] Game 클래스가 승리 메시지 표시 여부를 설정하는 Setter
     */
    public void setShowWinMessage(boolean show) {
        this.showWinMessage = true;
    }

    /**
     * 현재 플레이어가 출구 타일 위에 있는지 확인하여 반환합니다. (로직만 담당)
     */
    public boolean isPlayerAtExit() {
        if (dungeon == null) return false;
        
        Player player = dungeon.getPlayer();
        ExitTile exitTile = dungeon.getExitTile();
        
        if (player != null && exitTile != null) {
             // (getX(), getY()는 Tile 추상 클래스에 정의되어 있다고 가정)
             return player.getX() == exitTile.getX() && player.getY() == exitTile.getY();
        }
        return false;
    }

    // --- [그리기 (렌더링) 메서드] ---

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 

        if (dungeon == null) return;

        // 1. 모든 타일 그리기 (배경)
        drawMapTiles(g);

        // 2. 플레이어 그리기
        drawPlayer(g);

        // 3. 오버레이(Overlay) 메시지 그리기 (승리 또는 종료 확인)
        drawOverlayMessages(g);
    }
    
    // --- [헬퍼 그리기 메서드] ---
    
    private void drawMapTiles(Graphics g) {
        for (int y = 0; y < dungeon.getHeight(); y++) {
            for (int x = 0; x < dungeon.getWidth(); x++) {
                Tile tile = dungeon.getTile(x, y);
                if (tile != null) {
                    // 다형성을 이용하여 타일 스스로를 그리게 함
                    tile.draw(g, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE);
                }
            }
        }

        // ExitTile은 Tile 배열에 있으므로, 이 로직 안에서 이미 draw 됩니다.
        // 하지만 Wall/Floor 위에 ExitTile이 그려지는 것을 보장하기 위해
        // 별도의 draw 로직을 남겨둡니다. (옵션)
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

    private void drawOverlayMessages(Graphics g) {
        // 맵 재생성 직후 짧은 시간동안 승리 메시지 표시
        if (showWinMessage) {
            drawCenteredMessage(g, "Level Cleared! Next level? (Y/N)", 30, new Color(0, 0, 0, 150), new Color(255, 255, 255, 250));
        }
        
        // 종료 확인 메시지 표시
        if (showQuitMessage) {
            drawCenteredMessage(g, "You really want to Quit? (Y/N)", 20, new Color(0, 0, 0, 150), Color.WHITE);
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
package com.game_adventure.core;

import com.game_adventure.map.Dungeon;
import com.game_adventure.map.Tile;
import com.game_adventure.entity.Enemy;
import com.game_adventure.entity.Player;
import com.game_adventure.map.ExitTile;
import com.game_adventure.ui.ScoreRenderer;
import com.game_adventure.ui.GameStatusRenderer;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;

public class GamePanel extends JPanel {

    private Dungeon dungeon;
    private static final int TILE_SIZE = 32;
    
    private boolean showQuitMessage = false;
    private boolean showWinMessage = false; // 승리 메시지 표시 플래그

    private ScoreRenderer scoreRenderer;
    private GameStatusRenderer gameStatusRenderer;

    public GamePanel(Dungeon dungeon) {
        this.dungeon = dungeon;

        this.scoreRenderer = new ScoreRenderer();
        this.gameStatusRenderer = new GameStatusRenderer();

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

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        Player player = dungeon.getPlayer();

        // 점수 표시 (항상 표시)
        if (player != null) {
            scoreRenderer.draw(g, player.getScore());
        }

        // 상태 메시지 오버레이 (우선순위에 따라 하나만 표시)
        if (showQuitMessage) {
            // 종료 확인창
            gameStatusRenderer.drawQuitConfirmation(g, panelWidth, panelHeight);
        } 
        else if (showWinMessage) {
            // 레벨 클리어
            gameStatusRenderer.drawLevelClear(g, panelWidth, panelHeight);
        }
        else if (player != null && player.isDead()) {
            // 플레이어 사망 시 게임 오버 화면
            gameStatusRenderer.drawGameOver(g, panelWidth, panelHeight);
        }
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

}
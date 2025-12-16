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

    private Dungeon dungeon; // 현재 던전 맵
    private static final int TILE_SIZE = 32; // 타일 크기 픽셀 단위
    
    private boolean showQuitMessage = false; // 종료 메시지 표시 플래그
    private boolean showWinMessage = false; // 승리 메시지 표시 플래그

    private ScoreRenderer scoreRenderer; // 점수 렌더러
    private GameStatusRenderer gameStatusRenderer; // 게임 상태 렌더러

    public GamePanel(Dungeon dungeon) {
        this.dungeon = dungeon;

        this.scoreRenderer = new ScoreRenderer(); // 점수 렌더러 초기화
        this.gameStatusRenderer = new GameStatusRenderer(); // 게임 상태 렌더러 초기화

        int width = dungeon.getWidth() * TILE_SIZE; // 패널 너비 계산
        int height = dungeon.getHeight() * TILE_SIZE; // 패널 높이 계산
        setPreferredSize(new Dimension(width, height)); // 패널 크기 설정
        setBackground(Color.BLACK); // 배경색 검정으로 설정
    }

    // --- [Setter 및 Getter 메서드] ---

    // 던전 맵 Setter
    public void setDungeon(Dungeon newDungeon) {
        this.dungeon = newDungeon;

        int width = newDungeon.getWidth() * TILE_SIZE;
        int height = newDungeon.getHeight() * TILE_SIZE;
        setPreferredSize(new Dimension(width, height));
    }

    public void setShowQuitMessage(boolean show) { this.showQuitMessage = show; } // 종료 메시지 플래그 Setter
    public void setShowWinMessage(boolean show) { this.showWinMessage = show; } // 승리 메시지 플래그 Setter

    // 플레이어가 출구 타일에 도달했는지 확인하는 메서드
    public boolean isPlayerAtExit() {
        if (dungeon == null) return false; 
        
        Player player = dungeon.getPlayer(); // 플레이어 객체 가져오기
        ExitTile exitTile = dungeon.getExitTile(); // 출구 타일 가져오기
        
        if (player != null && exitTile != null) { // 플레이어와 출구 타일이 모두 존재하는지 확인
             return player.getX() == exitTile.getX() && player.getY() == exitTile.getY(); // 위치 비교
        }
        return false; // 플레이어 또는 출구 타일이 없으면 false 반환
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

        int panelWidth = getWidth(); // 패널 너비
        int panelHeight = getHeight(); // 패널 높이
        Player player = dungeon.getPlayer(); // 플레이어 객체 가져오기

        // 점수 표시 (항상 표시)
        if (player != null) { // 플레이어가 존재할 때만
            scoreRenderer.draw(g, player.getScore()); // 점수 그리기
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
    
    private void drawMapTiles(Graphics g) { // 모든 타일 그리기
        for (int y = 0; y < dungeon.getHeight(); y++) { // 세로 순회
            for (int x = 0; x < dungeon.getWidth(); x++) { // 가로 순회
                Tile tile = dungeon.getTile(x, y); // (x, y) 타일 가져오기
                if (tile != null) { // null 체크
                    tile.draw(g, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE); // 타일 그리기
                }
            }
        }
        // ExitTile은 Tile 배열에 있지만, 확실한 오버레이를 위해 다시 그리기
        ExitTile exitTile = dungeon.getExitTile(); // 출구 타일 가져오기
        if (exitTile != null) { // null 체크
            exitTile.draw(g, exitTile.getX() * TILE_SIZE, exitTile.getY() * TILE_SIZE, TILE_SIZE); // 출구 타일 그리기
        }
    }
    
    private void drawPlayer(Graphics g) { // 플레이어 그리기
        Player player = dungeon.getPlayer(); // 플레이어 객체 가져오기
        if (player != null) { // null 체크
            player.draw(g, TILE_SIZE); // 플레이어 그리기
        }
    }

    private void drawEnemies(Graphics g) {
        // Dungeon 클래스의 getEnemies()가 List<Enemy>를 반환한다고 가정
        if (dungeon.getEnemies() != null) { // null 체크
            for (Enemy enemy : dungeon.getEnemies()) { // 리스트를 순회
                if (enemy != null) { // null 체크
                    enemy.draw(g, TILE_SIZE); // 적 그리기
                }
            }
        }
    }

}
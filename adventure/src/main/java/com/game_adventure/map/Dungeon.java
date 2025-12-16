package com.game_adventure.map;

import java.util.ArrayList;
import java.util.List;

import com.game_adventure.entity.Enemy;
import com.game_adventure.entity.Player;
import com.game_adventure.entity.Entity;

public class Dungeon {

    private Tile[][] tiles; // 던전의 타일 배열
    private Player player;  // 던전에 속한 플레이어 객체
    private List<Enemy> enemies; // 적 리스트

    // 시작 위치를 저장할 필드
    private int startX;
    private int startY;

    public Dungeon(Tile[][] tiles, Player player, int startX, int startY) {
        this.tiles = tiles;
        this.player = player;
        this.startX = startX;
        this.startY = startY;
        this.enemies = new ArrayList<>();
    }

    public Player getPlayer() { return player; } // 플레이어 Getter
    public List<Enemy> getEnemies() { return enemies; } // 적 리스트 Getter
    public void addEnemy(Enemy enemy) { this.enemies.add(enemy); } // 적 추가 메서드

    public Enemy getEnemyAt(int x, int y) { // (x, y) 위치의 적 반환 메서드
        for (Enemy enemy : enemies) {
            if (enemy.getX() == x && enemy.getY() == y) {
                return enemy; // 해당 위치에 적이 있을 경우 반환
            }
        }
        return null; // 해당 위치에 적이 없을 경우
    }

    // 시작 위치 Getter
    public int getStartX() { return startX; }
    public int getStartY() { return startY; }
    
    // Game.java에서 기존 Player 객체를 Dungeon에 연결하기 위한 Setter
    public void setPlayer(Player player) { this.player = player; }
        
    // 출구 타일을 반환하는 메서드
    public ExitTile getExitTile() { // 출구 타일 검색
        for (int y = 0; y < tiles.length; y++) { // 세로 탐색
            for (int x = 0; x < tiles[0].length; x++) { // 가로 탐색
                if (tiles[y][x] instanceof ExitTile) { // 출구 타일 발견 시 반환
                    return (ExitTile) tiles[y][x]; // 형변환 후 반환
                }
            }
        }
        return null; // 출구 타일이 없을 경우
    }

    public boolean isWalkable(int x, int y) { // (x, y) 위치가 이동 가능한지 확인
        if (y < 0 || y >= tiles.length || x < 0 || x >= tiles[0].length) { // 맵 밖일 경우
            return false; // 이동 불가
        }
        return tiles[y][x].isWalkable(); // 해당 타일의 이동 가능 여부 반환
    }

    public int getWidth() { return tiles[0].length; } // 맵 너비 반환
    public int getHeight() { return tiles.length; } // 맵 높이 반환

    // 죽은 적을 리스트에서 삭제하는 메서드
    public void removeEnemy(Entity enemy) {
        if (enemies != null) {
            enemies.remove(enemy);
        }
    }

    // (x, y) 위치의 타일 객체를 반환
    public Tile getTile(int x, int y) {
        if (y < 0 || y >= tiles.length || x < 0 || x >= tiles[0].length) { // 맵 밖일 경우
            return null; // 맵 밖은 null
        }
        return tiles[y][x]; 
    }
}
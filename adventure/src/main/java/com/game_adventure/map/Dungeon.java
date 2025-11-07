package com.game_adventure.map;

import com.game_adventure.entity.Player;

public class Dungeon {

    private Tile[][] tiles;
    private Player player; 

    // 시작 위치를 저장할 필드
    private int startX;
    private int startY;

    public Dungeon(Tile[][] tiles, Player player, int startX, int startY) {
        this.tiles = tiles;
        this.player = player;
        this.startX = startX;
        this.startY = startY;
    }

    public Player getPlayer() { return player; }
    // 시작 위치 Getter
    public int getStartX() { return startX; }
    public int getStartY() { return startY; }
    // Game.java에서 기존 Player 객체를 Dungeon에 연결하기 위한 Setter
    public void setPlayer(Player player) { this.player = player; }
        
    // 출구 타일을 반환하는 메서드
    public ExitTile getExitTile() {
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[0].length; x++) {
                if (tiles[y][x] instanceof ExitTile) {
                    return (ExitTile) tiles[y][x];
                }
            }
        }
        return null; // 출구 타일이 없을 경우
    }

    

    public boolean isWalkable(int x, int y) {
        if (y < 0 || y >= tiles.length || x < 0 || x >= tiles[0].length) {
            return false;
        }
        return tiles[y][x].isWalkable();
    }

    // [추가] 맵의 너비를 반환
    public int getWidth() {
        return tiles[0].length;
    }

    // [추가] 맵의 높이를 반환
    public int getHeight() {
        return tiles.length;
    }

    // [추가] (x, y) 위치의 타일 객체를 반환
    public Tile getTile(int x, int y) {
        if (y < 0 || y >= tiles.length || x < 0 || x >= tiles[0].length) {
            return null; // 맵 밖은 null
        }
        return tiles[y][x];
    }
}
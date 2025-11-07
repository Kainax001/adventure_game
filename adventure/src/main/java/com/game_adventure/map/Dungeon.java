package com.game_adventure.map;

import com.game_adventure.entity.Player;

public class Dungeon {

    private Tile[][] tiles;
    private Player player; 

    public Dungeon(Tile[][] tiles, Player player) {
        this.tiles = tiles;
        this.player = player;
    }

    public Player getPlayer() { return player; }
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
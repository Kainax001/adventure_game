package com.game_adventure.map;

import java.awt.Graphics; // Swing 그래픽 import

public abstract class Tile {
    protected boolean isWalkable;
    // protected char consoleChar; (삭제)

    public Tile(boolean isWalkable) { // (consoleChar 삭제)
        this.isWalkable = isWalkable;
    }

    public boolean isWalkable() { return isWalkable; }

    // "너 자신을 그려" 메서드 (다형성의 핵심)
    // (x, y) 픽셀 좌표에 TILE_SIZE 크기로 그림
    public abstract void draw(Graphics g, int x, int y, int tileSize);
}
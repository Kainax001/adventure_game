package com.game_adventure.map;

import java.awt.Color;
import java.awt.Graphics;

public class FloorTile extends Tile {
    public FloorTile(int x, int y) {
        super(true, x, y); // (consoleChar 삭제)
    }

    @Override
    public void draw(Graphics g, int x, int y, int tileSize) {
        g.setColor(Color.WHITE); // 바닥 색깔
        g.fillRect(x, y, tileSize, tileSize);
    }
}
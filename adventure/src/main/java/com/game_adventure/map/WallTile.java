package com.game_adventure.map;

import java.awt.Color;
import java.awt.Graphics;

public class WallTile extends Tile {
    public WallTile(int x, int y) {
        super(false, x, y);
    }

    @Override
    public void draw(Graphics g, int x, int y, int tileSize) {
        g.setColor(Color.DARK_GRAY); // 벽 색깔
        g.fillRect(x, y, tileSize, tileSize);
    }
}
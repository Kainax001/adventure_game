package com.game_adventure.map;

import java.awt.Color;
import java.awt.Graphics;

public class ExitTile extends Tile {
    public ExitTile(int x, int y) {
        super(true, x, y);
    }
    
    @Override
    public void draw(Graphics g, int x, int y, int tileSize) {
        g.setColor(Color.BLUE); // 출구 타일 색깔
        g.fillRect(x, y, tileSize + 2, tileSize + 2);
    }

}

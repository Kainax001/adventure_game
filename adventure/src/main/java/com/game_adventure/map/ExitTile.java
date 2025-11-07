package com.game_adventure.map;

import java.awt.Color;
import java.awt.Graphics;

public class ExitTile extends Tile {
    protected int x; // 타일의 맵 그리드 X 좌표
    protected int y; // 타일의 맵 그리드 Y 좌표

    public ExitTile(int x, int y) {
        super(true, x, y);
        this.x = x; // 초기화 추가
        this.y = y; // 초기화 추가
    }

    public int getX() { 
        return x; 
    }

    public int getY() { 
        return y; 
    }

    @Override
    public void draw(Graphics g, int x, int y, int tileSize) {
        g.setColor(Color.BLUE); // 출구 타일 색깔
        g.fillRect(x, y, tileSize + 2, tileSize + 2);
    }

}

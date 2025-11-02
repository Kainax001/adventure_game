package com.game_adventure.entity;

import java.awt.Graphics; // Swing 그래픽 import

public abstract class Entity {
    protected int x; // 타일 좌표 (x)
    protected int y; // 타일 좌표 (y)
    // protected char consoleChar; (삭제)

    public Entity(int x, int y) { // (consoleChar 삭제)
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    // public char getConsoleChar() { return consoleChar; } (삭제)

    // 엔티티 스스로를 그리는 메서드
    public abstract void draw(Graphics g, int tileSize);
}
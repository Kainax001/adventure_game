package com.game_adventure.entity;

import java.awt.Graphics; // Swing 그래픽 import

import com.game_adventure.map.Dungeon;

public abstract class Entity {
    protected int x; // 타일 좌표 (x)
    protected int y; // 타일 좌표 (y)
    public Entity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    //플레이어 위치를 새로 설정하는 메서드
    public void setPosition(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }
    
    public void move(int dx, int dy, Dungeon dungeon) {
        int newX = this.x + dx;
        int newY = this.y + dy;
        
        if (dungeon.isWalkable(newX, newY)) {
            this.x = newX;
            this.y = newY;
        }
        else {
            // 이동 불가 시 처리 로직 (필요시 구현)
        }
    }

    // 엔티티 스스로를 그리는 메서드
    public abstract void draw(Graphics g, int tileSize);
}
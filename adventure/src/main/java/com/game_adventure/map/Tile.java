package com.game_adventure.map;

import java.awt.Graphics; // Swing 그래픽 import
public abstract class Tile {
    protected boolean isWalkable; // 이동 가능 여부 필드
    protected boolean isEnemyhere; // 적 존재 여부 필드
    protected boolean isPlayerhere; // 플레이어 존재 여부 필드

    // 모든 타일이 가질 그리드 좌표 필드
    protected int x; 
    protected int y; 

    public Tile(boolean isWalkable, int x, int y) { 
        this.isWalkable = isWalkable;
        this.x = x; // 초기화
        this.y = y; // 초기화
        this.isEnemyhere = false;
        this.isPlayerhere = false;
    }

    public void setIsEnemyhere(boolean value) {
        this.isEnemyhere = value;
    }
    public void setIsPlayerhere(boolean isPlayerhere) { 
        this.isPlayerhere = isPlayerhere; 
    }

    public boolean isWalkable() { return isWalkable; } // 이동 가능 여부 반환 메서드
    public boolean isEnemyhere() { return isEnemyhere; } // 적 존재 여부 반환 메서드
    public boolean isPlayerhere() { return isPlayerhere; } // 플레이어 존재 여부 반환 메서드

    // 그리드 좌표 반환 메서드
    public int getX() { return x; } 
    public int getY() { return y; }
    
    // (x, y) 픽셀 좌표에 TILE_SIZE 크기로 그림
    public abstract void draw(Graphics g, int x, int y, int tileSize);
}
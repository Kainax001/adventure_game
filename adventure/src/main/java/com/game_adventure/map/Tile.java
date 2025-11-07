package com.game_adventure.map;

import java.awt.Graphics; // Swing 그래픽 import

public abstract class Tile {
    protected boolean isWalkable;

    // **[추가]** 모든 타일이 가질 그리드 좌표 필드
    protected int x; 
    protected int y; 

    // **[수정]** x, y 좌표를 매개변수로 받도록 생성자 수정
    public Tile(boolean isWalkable, int x, int y) { 
        this.isWalkable = isWalkable;
        this.x = x; // 초기화
        this.y = y; // 초기화
    }

    public boolean isWalkable() { return isWalkable; }

    // **[추가]** getX() 및 getY() 메서드 추가 (GamePanel에서 호출할 메서드)
    public int getX() { return x; } 
    public int getY() { return y; }
    
    // "너 자신을 그려" 메서드 (다형성의 핵심)
    // (x, y) 픽셀 좌표에 TILE_SIZE 크기로 그림
    public abstract void draw(Graphics g, int x, int y, int tileSize);
}
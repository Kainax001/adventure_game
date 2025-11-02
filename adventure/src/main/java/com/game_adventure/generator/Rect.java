package com.game_adventure.generator;

// BSP에서 구역(Region)과 방(Room)을 정의하는 데 사용할 클래스
public class Rect {
    public int x, y, w, h;

    public Rect(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
}
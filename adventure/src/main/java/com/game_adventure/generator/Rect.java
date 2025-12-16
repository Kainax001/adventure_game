package com.game_adventure.generator;

// BSP에서 구역(Region)과 방(Room)을 정의하는 데 사용할 클래스
public class Rect {
    public int x, y, w, h;

    public Rect(int x, int y, int w, int h) {
        this.x = x; // 좌상단 x 좌표
        this.y = y; // 좌상단 y 좌표
        this.w = w; // 너비
        this.h = h; // 높이
    }
}
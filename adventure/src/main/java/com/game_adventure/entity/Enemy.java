package com.game_adventure.entity;

import com.game_adventure.map.Dungeon;
import java.awt.Color; // import
import java.awt.Graphics; // import
import java.lang.Math; // import

public class Enemy extends Entity{
    protected int detectionRangeSquared;
    private final int MOVE_SPEED_FACTOR = 20; // 적이 몇 프레임마다 한 번 움직일지 설정
    private int frameCounter = 0; // 현재 프레임 카운터를 저장

    public Enemy (int x, int y){
        super(x, y);
        this.detectionRangeSquared = 9; // 기본 탐지 범위 설정 피타고라스라 제곱
    }

    public void move(int dx, int dy, Dungeon dungeon) {
        int newX = this.x + dx;
        int newY = this.y + dy;

        if (dungeon.isWalkable(newX, newY)) {
            this.x = newX;
            this.y = newY;
        }
    }

    public void update(Dungeon dungeon, Player player) {
        this.frameCounter++; // 1. 카운터를 증가시키고, 이동 주기가 아닐 경우 즉시 종료
        if (this.frameCounter < MOVE_SPEED_FACTOR) {  // MOVE_SPEED_FACTOR 주기가 채워지지 않았으면 이동하지 않음
            return;
        }
        this.frameCounter = 0; // 카운터 초기화

        int xDiff = player.getX() - this.x;
        int yDiff = player.getY() - this.y;
        double distanceSquared = xDiff * xDiff + yDiff * yDiff;

        // 플레이어가 탐지 범위 밖에 있으면 아무 행동도 하지 않음
        if(distanceSquared > this.detectionRangeSquared) return;

        // 간단한 추적 AI: 플레이어 방향으로 한 칸 이동
        int dx = Integer.compare(player.getX(), this.x);
        int dy = Integer.compare(player.getY(), this.y);

        if (dx != 0 && dy != 0) {
            // X축 거리가 Y축 거리보다 멀면 X축 이동을 우선
            if (Math.abs(xDiff) > Math.abs(yDiff)) {
                dy = 0; // Y축 이동을 포기
            } 
            // Y축 거리가 X축 거리보다 멀거나 같으면 Y축 이동을 우선
            else { 
                dx = 0; // X축 이동을 포기
            }
        }
        
        move(dx, dy, dungeon);
    }

    @Override
    public void draw(Graphics g, int tileSize) {
        g.setColor(Color.RED); 
        g.fillOval(x * tileSize, y * tileSize, tileSize, tileSize);
    }
}

package com.game_adventure.entity;

import com.game_adventure.map.Dungeon;
import java.awt.Color; // import
import java.awt.Graphics; // import

public class Enemy extends Entity{
    protected int detectionRangeSquared;

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
        int xDiff = player.getX() - this.x;
        int yDiff = player.getY() - this.y;
        double distanceSquared = xDiff * xDiff + yDiff * yDiff;

        // 플레이어가 탐지 범위 밖에 있으면 아무 행동도 하지 않음
        if(distanceSquared > this.detectionRangeSquared) return;

        // 간단한 추적 AI: 플레이어 방향으로 한 칸 이동
        int dx = Integer.compare(player.getX(), this.x);
        int dy = Integer.compare(player.getY(), this.y);
        move(dx, dy, dungeon);
    }

    @Override
    public void draw(Graphics g, int tileSize) {
        g.setColor(Color.RED); 
        g.fillOval(x * tileSize, y * tileSize, tileSize, tileSize);
    }
}

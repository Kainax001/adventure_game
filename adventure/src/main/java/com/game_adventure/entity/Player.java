package com.game_adventure.entity;

import com.game_adventure.map.Dungeon;
import java.awt.Color; // import
import java.awt.Graphics; // import

public class Player extends Entity {

    public Player(int x, int y) {
        super(x, y); // (consoleChar 삭제)
    }

    public void move(int dx, int dy, Dungeon dungeon) {
        int newX = this.x + dx;
        int newY = this.y + dy;

        if (dungeon.isWalkable(newX, newY)) {
            this.x = newX;
            this.y = newY;
        }
    }

    @Override
    public void draw(Graphics g, int tileSize) {
        g.setColor(Color.GREEN); // 플레이어 색깔
        // 타일 좌표(x,y)를 픽셀 좌표로 변환하여 원을 그림
        g.fillOval(x * tileSize, y * tileSize, tileSize, tileSize);
    }
}
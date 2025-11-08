package com.game_adventure.entity;

import com.game_adventure.map.Dungeon;
import com.game_adventure.map.Tile;

import java.awt.Color; // import
import java.awt.Graphics; // import

public class Player extends Entity {
    private int dx = 0;
    private int dy = 0;

    public Player(int x, int y) {
        super(x, y);
    }

    public void move(int dx, int dy, Dungeon dungeon) {
        int newX = this.x + dx;
        int newY = this.y + dy;

        Tile currentTile = dungeon.getTile(this.x, this.y);
        Tile newtile = dungeon.getTile(newX, newY);

        if (dungeon.isWalkable(newX, newY)) {
            currentTile.setIsPlayerhere(false); // 이동 전 현재 타일 플레이어 상태 설정
            super.move(dx, dy, dungeon);
            newtile.setIsPlayerhere(true);  // 이동 후 원래 타일 플레이어 상태 설정
        }
    }

    public void dash(int dx, int dy, Dungeon dungeon) {
        move(this.dx, this.dy, dungeon); // 대시 이동
    }

    @Override
    public void draw(Graphics g, int tileSize) {
        g.setColor(Color.GREEN); // 플레이어 색깔
        // 타일 좌표(x,y)를 픽셀 좌표로 변환하여 원을 그림
        g.fillOval(x * tileSize, y * tileSize, tileSize, tileSize);
    }
}
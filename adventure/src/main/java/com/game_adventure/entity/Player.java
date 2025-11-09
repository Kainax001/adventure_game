package com.game_adventure.entity;

import com.game_adventure.map.Dungeon;
import com.game_adventure.map.Tile;

import java.awt.Color; // import
import java.awt.Graphics; // import

public class Player extends Entity {
    private int dashCooldown = 0; // 대시 쿨다운 타이머
    private final int DASH_COOLDOWN_TIME = 100; // 대시 쿨다운 시간 (프레임 단위)
    private final int DASH_DISTANCE = 2; // 대시 거리

    public Player(int x, int y) {
        super(x, y);
    }

    public void update() { //쿨다운을 1프레임마다 감소
        if (dashCooldown > 0) {
            dashCooldown--;
        }
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
        if (dashCooldown > 0) {
            move(dx, dy, dungeon);
            return;
        }
        this.dashCooldown = DASH_COOLDOWN_TIME; 
        
        for (int i = 0; i < DASH_DISTANCE; i++) {
            move(dx, dy, dungeon); 
        }
    }

    @Override
    public void draw(Graphics g, int tileSize) {
        g.setColor(Color.GREEN); // 플레이어 색깔
        // 타일 좌표(x,y)를 픽셀 좌표로 변환하여 원을 그림
        g.fillOval(x * tileSize, y * tileSize, tileSize, tileSize);
    }
}
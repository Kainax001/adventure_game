package com.game_adventure.entity;

import com.game_adventure.combat.Battle;
import com.game_adventure.map.Dungeon;
import com.game_adventure.map.Tile;

import java.awt.Color;
import java.awt.Graphics;

public class Player extends Entity {
    private int dashCooldown = 0; // 대시 쿨다운 타이머
    private final int DASH_COOLDOWN_TIME = 90; // 대시 쿨다운 시간 프레임 단위
    private final int DASH_DISTANCE = 3; // 대시 거리

    private final int MOVE_SPEED_FACTOR = 5; // 플레이어가 몇 프레임마다 한 번 움직일지 설정
    private int moveTimer = 0; // 현재 프레임 카운터를 저장

    public Player(int x, int y) {
        super(x, y);
    }

    public void update() { //쿨다운을 1프레임마다 감소
        if (dashCooldown > 0) {
            dashCooldown--;
        }
        if (moveTimer > 0) {
            moveTimer--;
        }
    }

    public void move(int dx, int dy, Dungeon dungeon) {
        if (moveTimer > 0) {
            return; 
        }

        int newX = this.x + dx;
        int newY = this.y + dy;

        Tile currentTile = dungeon.getTile(this.x, this.y);
        Tile newtile = dungeon.getTile(newX, newY);

        if(newtile.isEnemyhere()) {
            int enemyX = newtile.getX();
            int enemyY = newtile.getY();
            Entity enemy = dungeon.getEnemyAt(enemyX, enemyY);

            if (enemy != null) {
                newtile.setIsEnemyhere(false); 

                int blockedCount = Battle.pushEntity(enemy, dx, dy, dungeon, 1); 

                Tile enemyNextTile = dungeon.getTile(enemy.getX(), enemy.getY());
                enemyNextTile.setIsEnemyhere(true);

                // 적이 성공적으로 밀려났고 내 앞이 비었을 때만 이동
                if (blockedCount == 0 && !newtile.isEnemyhere()) {
                    currentTile.setIsPlayerhere(false);
                    super.move(dx, dy, dungeon); // 플레이어 전진
                    newtile.setIsPlayerhere(true);
                } 
                // blockedCount > 0 이면(적이 벽에 막힘) 플레이어는 제자리에 멈춤.
            }

            moveTimer = MOVE_SPEED_FACTOR; // 이동 후 타이머 초기화
        }
        else if (dungeon.isWalkable(newX, newY)) {
            currentTile.setIsPlayerhere(false); // 이동 전 현재 타일 플레이어 상태 설정
            super.move(dx, dy, dungeon);
            newtile.setIsPlayerhere(true);  // 이동 후 원래 타일 플레이어 상태 설정

            moveTimer = MOVE_SPEED_FACTOR; // 이동 후 타이머 초기화
        }
    }

    public void dash(int dx, int dy, Dungeon dungeon) {
        if (dashCooldown > 0) {
            move(dx, dy, dungeon);
            return;
        }
        this.dashCooldown = DASH_COOLDOWN_TIME; 
        
        for (int i = 0; i < DASH_DISTANCE; i++) {
            this.moveTimer = 0; // 대시 중에는 이동 타이머 무시
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
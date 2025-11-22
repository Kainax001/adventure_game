package com.game_adventure.entity;

import com.game_adventure.map.Dungeon;
import com.game_adventure.ui.HealthBarRenderer;

import java.awt.Color;
import java.awt.Graphics;

public class Player extends Entity {
    private int dashCooldown = 0; // 대시 쿨다운 타이머
    private final int DASH_COOLDOWN_TIME = 90; // 대시 쿨다운 시간 프레임 단위
    private final int DASH_DISTANCE = 3; // 대시 거리

    private final int MOVE_SPEED_FACTOR = 5; // 플레이어가 몇 프레임마다 한 번 움직일지 설정
    private int moveTimer = 0; // 현재 프레임 카운터를 저장

    private HealthBarRenderer healthBarRenderer;

    private int score = 0;

    public Player(int x, int y) {
        super(x, y, createStats(200, 20));
        this.healthBarRenderer = new HealthBarRenderer(Color.GREEN);
        this.score = 0;
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
        PlayerMovementLogic.processMovement(this, dx, dy, dungeon, false);
    }

    // dash 메서드
    public void dash(int dx, int dy, Dungeon dungeon) {
        if (dashCooldown > 0) {
            move(dx, dy, dungeon); 
            return;
        }
        this.dashCooldown = DASH_COOLDOWN_TIME; 
        
        for (int i = 0; i < DASH_DISTANCE; i++) {
            this.moveTimer = 0; // 대시 중에는 타이머 무시
            // 대시 이동 (true 전달)
            PlayerMovementLogic.processMovement(this, dx, dy, dungeon, true);
        }
    }

    // --- [Getter & Setter] ---

    // 점수
    public int getScore() { return this.score; }
    public void setScore(int score) { this.score = score; }

    // 이동 타이머
    public int getMoveTimer() { return this.moveTimer; }
    public void setMoveTimer(int time) { this.moveTimer = time; }
    
    // 이동 속도 상수
    public int getMoveSpeedFactor() { return MOVE_SPEED_FACTOR; }

    @Override
    public void draw(Graphics g, int tileSize) {
        g.setColor(Color.GREEN); // 플레이어 색깔
        // 타일 좌표(x,y)를 픽셀 좌표로 변환하여 원을 그림
        g.fillOval(x * tileSize, y * tileSize, tileSize, tileSize);

        healthBarRenderer.draw(g, this.x, this.y, tileSize, this.getStats());
    }
}
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

    private HealthBarRenderer healthBarRenderer; // 체력바 렌더러

    private int score = 0; // 플레이어 점수

    public Player(int x, int y) { 
        super(x, y, createStats(200, 20)); // 체력 200, 공격력 20으로 스탯 생성
        this.healthBarRenderer = new HealthBarRenderer(Color.GREEN); // 체력바 녹색으로 초기화
        this.score = 0; // 점수 초기화
    }

    public void update() { //쿨다운을 1프레임마다 감소
        if (dashCooldown > 0) { // 대시 쿨다운 감소
            dashCooldown--;
        }
        if (moveTimer > 0) { // 이동 타이머 감소
            moveTimer--;
        }
    }

    public void move(int dx, int dy, Dungeon dungeon) { // 일반 이동 메서드
        PlayerMovementLogic.processMovement(this, dx, dy, dungeon, false); // 대시 아님(false 전달)
    }

    // dash 메서드
    public void dash(int dx, int dy, Dungeon dungeon) {
        if (dashCooldown > 0) {
            move(dx, dy, dungeon); // 대시 쿨다운 중이면 일반 이동으로 대체
            return;
        }
        this.dashCooldown = DASH_COOLDOWN_TIME; // 대시 쿨다운 초기화
        
        for (int i = 0; i < DASH_DISTANCE; i++) {
            this.moveTimer = 0; // 대시 중에는 타이머 무시, 이동 기능을 이용한 대시 구현
            PlayerMovementLogic.processMovement(this, dx, dy, dungeon, true); // 대시 이동 (true 전달)
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
        g.fillOval(x * tileSize, y * tileSize, tileSize, tileSize); // 타일 좌표(x,y)를 픽셀 좌표로 변환하여 원을 그림
        healthBarRenderer.draw(g, this.x, this.y, tileSize, this.getStats()); // 체력바 그리기
    }
}
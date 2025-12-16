package com.game_adventure.entity;

import com.game_adventure.map.Dungeon;
import com.game_adventure.ui.HealthBarRenderer;

import java.awt.Color;
import java.awt.Graphics;

public class Enemy extends Entity{
    protected int detectionRangeSquared; // 탐지 범위 상수
    protected int addtionalRangeSquared; // 추가 탐지 범위 상수

    private final int MOVE_SPEED_FACTOR = 20; // 적이 몇 프레임마다 한 번 움직일지 설정

    private int frameCounter = 0; // 현재 프레임 카운터를 저장
    private boolean playerDetected = false; // 플레이어 탐지 여부
    private int curruntdetectionRangeSquared;   // 현재 탐지 범위
    private HealthBarRenderer healthBarRenderer; // 체력바 렌더러
    private Color enemyColor; // 적 색상

    private int dx = 0; // 이동 방향 x
    private int dy = 0; // 이동 방향 y
    private final int initialX; // 초기 위치 x
    private final int initialY; // 초기 위치 y

    public Enemy (int x, int y){ 
        super(x, y, EnemyStat.createRandomStats()); // 랜덤 스탯 생성

        this.initialX = x; // 초기 위치 저장
        this.initialY = y; // 초기 위치 저장
        this.detectionRangeSquared = 9; // 기본 탐지 범위 설정
        this.addtionalRangeSquared = 72; // 추가 탐지 범위 설정
        this.curruntdetectionRangeSquared = detectionRangeSquared; // 현재 탐지 범위 기본 초기화

        this.enemyColor = EnemyStat.calculateColorByHp(this.getStats().getMaxHp()); // 최대 체력에 따른 색상 계산
        this.healthBarRenderer = new HealthBarRenderer(Color.RED); // 체력바 빨간색으로 초기화
    }

    public void update(Dungeon dungeon, Player player) { // 적의 행동 업데이트 메서드
        EnemyBehavior.processUpdate(this, dungeon, player); // 적 행동 처리
    }

    // --- [Getter & Setter] ---
    // 프레임 카운터
    public int getFrameCounter() { return frameCounter; }
    public void setFrameCounter(int frameCounter) { this.frameCounter = frameCounter; }

    // 플레이어 탐지 여부
    public boolean isPlayerDetected() { return playerDetected; }
    public void setPlayerDetected(boolean playerDetected) { this.playerDetected = playerDetected; }

    // 현재 탐지 범위
    public int getCurrentDetectionRangeSquared() { return curruntdetectionRangeSquared; }
    public void setCurrentDetectionRangeSquared(int range) { this.curruntdetectionRangeSquared = range; }

    // 탐지 범위 상수
    public int getDetectionRangeSquared() { return detectionRangeSquared; }
    public int getAdditionalRangeSquared() { return addtionalRangeSquared; }

    // 초기 위치 Getter
    public int getInitialX() { return initialX; }
    public int getInitialY() { return initialY; }

    // 이동 방향 Getter & Setter
    public int getDx() { return dx; }
    public void setDx(int dx) { this.dx = dx; }
    public int getDy() { return dy; }
    public void setDy(int dy) { this.dy = dy; }
    
    // 이동 속도 상수
    public int getMoveSpeedFactor() { return MOVE_SPEED_FACTOR; }

    @Override
    public void draw(Graphics g, int tileSize) {
        g.setColor(this.enemyColor); // 적 색깔
        g.fillOval(x * tileSize, y * tileSize, tileSize, tileSize); // 타일 좌표(x,y)를 픽셀 좌표로 변환하여 원을 그림
        healthBarRenderer.draw(g, this.x, this.y, tileSize, this.getStats()); // 체력바 그리기
    }
}

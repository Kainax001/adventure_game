package com.game_adventure.entity;

import com.game_adventure.map.Dungeon;
import com.game_adventure.ui.HealthBarRenderer;

import java.awt.Color;
import java.awt.Graphics;

public class Enemy extends Entity{
    protected int detectionRangeSquared;
    protected int addtionalRangeSquared;

    private final int MOVE_SPEED_FACTOR = 20; // 적이 몇 프레임마다 한 번 움직일지 설정

    private int frameCounter = 0; // 현재 프레임 카운터를 저장
    private boolean playerDetected = false;
    private int curruntdetectionRangeSquared;
    private HealthBarRenderer healthBarRenderer;
    private Color enemyColor;

    private int dx = 0;
    private int dy = 0;
    private final int initialX;
    private final int initialY;

    public Enemy (int x, int y){
        super(x, y, EnemyStat.createRandomStats());

        this.initialX = x;
        this.initialY = y;
        this.detectionRangeSquared = 9; // 기본 탐지 범위 설정 3 반지름 원
        this.addtionalRangeSquared = 72; // 추가 탐지 범위 설정
        this.curruntdetectionRangeSquared = detectionRangeSquared; // 현재 탐지 범위 기본 초기화

        this.enemyColor = EnemyStat.calculateColorByHp(this.getStats().getMaxHp());
        this.healthBarRenderer = new HealthBarRenderer(Color.RED);
    }

    public void update(Dungeon dungeon, Player player) {
        EnemyBehavior.processUpdate(this, dungeon, player);
    }

    public int getFrameCounter() { return frameCounter; }
    public void setFrameCounter(int frameCounter) { this.frameCounter = frameCounter; }

    public boolean isPlayerDetected() { return playerDetected; }
    public void setPlayerDetected(boolean playerDetected) { this.playerDetected = playerDetected; }

    public int getCurrentDetectionRangeSquared() { return curruntdetectionRangeSquared; }
    public void setCurrentDetectionRangeSquared(int range) { this.curruntdetectionRangeSquared = range; }

    public int getDetectionRangeSquared() { return detectionRangeSquared; }
    public int getAdditionalRangeSquared() { return addtionalRangeSquared; }

    public int getInitialX() { return initialX; }
    public int getInitialY() { return initialY; }

    public int getDx() { return dx; }
    public void setDx(int dx) { this.dx = dx; }
    public int getDy() { return dy; }
    public void setDy(int dy) { this.dy = dy; }
    
    public int getMoveSpeedFactor() { return MOVE_SPEED_FACTOR; }

    @Override
    public void draw(Graphics g, int tileSize) {
        g.setColor(this.enemyColor); 
        g.fillOval(x * tileSize, y * tileSize, tileSize, tileSize);

        healthBarRenderer.draw(g, this.x, this.y, tileSize, this.getStats());
    }
}

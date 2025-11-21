package com.game_adventure.entity;

import com.game_adventure.map.Dungeon;
import com.game_adventure.map.Tile;
import com.game_adventure.ui.HealthBarRenderer;
import com.game_adventure.combat.Battle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Enemy extends Entity{
    protected int detectionRangeSquared;
    protected int addtionalRangeSquared;

    private final int MOVE_SPEED_FACTOR = 20; // 적이 몇 프레임마다 한 번 움직일지 설정

    private int frameCounter = 0; // 현재 프레임 카운터를 저장
    private boolean playerDetected = false;
    private int curruntdetectionRangeSquared;
    private HealthBarRenderer healthBarRenderer;

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

        this.healthBarRenderer = new HealthBarRenderer(Color.RED);
    }

    public void move(Dungeon dungeon) {
        int newX = this.x + this.dx;
        int newY = this.y + this.dy;

        Tile newTile = dungeon.getTile(newX, newY); 
        Entity player = dungeon.getPlayer();

        // 1. 플레이어 충돌 확인 및 밀어내기 시도
        if (newTile.isPlayerhere()) {
            int oldPlayerX = player.getX();
            int oldPlayerY = player.getY();

            Tile playerCurrentTile = dungeon.getTile(oldPlayerX, oldPlayerY);
            playerCurrentTile.setIsPlayerhere(false);

            Battle.pushEntity(player, this.dx, this.dy, dungeon, 2); // Battle 클래스로 밀어내기 로직 위임 (2회 시도 가정)

            Tile playerNewTile = dungeon.getTile(player.getX(), player.getY());
            playerNewTile.setIsPlayerhere(true);

            // 플레이어가 이동에 성공했는지 확인
            if (player.getX() != oldPlayerX || player.getY() != oldPlayerY) {
                super.move(this.dx, this.dy, dungeon); // 밀어내기 성공: 플레이어가 떠난 자리에 진입
                this.dx = 0; // 이동 후 x방향 초기화
                this.dy = 0; // 이동 후 y방향 초기화
                return;
            }
        } 
        else { // 2. 일반 이동
            super.move(this.dx, this.dy, dungeon); 
            this.dx = 0;
            this.dy = 0;
        }
    }

    public void update(Dungeon dungeon, Player player) {
        this.frameCounter++; // 1. 카운터를 증가시키고, 이동 주기가 아닐 경우 즉시 종료

        if (this.frameCounter < MOVE_SPEED_FACTOR) {  // MOVE_SPEED_FACTOR 주기가 채워지지 않았으면 이동하지 않음
            return;
        }
        this.frameCounter = 0; // 카운터 초기화

        int playerXDiff = player.getX() - this.x; // 플레이어와의 X차이
        int playerYDiff = player.getY() - this.y; // 플레이어와의 Y차이
        int distanceSquared = playerXDiff * playerXDiff + playerYDiff * playerYDiff;

        if (playerDetected) {
            if (distanceSquared > curruntdetectionRangeSquared) { // 이미 탐지된 상태에서 탐지 유지 범위(detection + additional)를 벗어났다면
                this.playerDetected = false; // 탐지 해제
                curruntdetectionRangeSquared = detectionRangeSquared; // 탐지 범위 기본으로 초기화
            }
        }
        else { 
            this.curruntdetectionRangeSquared = this.detectionRangeSquared; // 탐지 조건 검사 전에 현재 탐지 범위를 기본으로 재설정

            if (distanceSquared <= this.curruntdetectionRangeSquared) { // 아직 탐지되지 않은 상태에서 detectionRange안에 들어왔다면
                this.playerDetected = true; // 탐지 시작
                this.curruntdetectionRangeSquared = this.detectionRangeSquared + this.addtionalRangeSquared; // 탐지 시작 시에만 추적 유지 범위로 넓힘
            } 
        }

        Point target;
        
        if (playerDetected) {
            target = new Point(player.getX(), player.getY());
        } 
        else { // 탐지되지 않았다면 무조건 초기 위치를 목표로 설정하여 복귀를 시도
            target = new Point(initialX, initialY);

            if (this.x == initialX && this.y == initialY) { // 초기 위치에 도착했고 미탐지 상태라면
                return; // BFS 및 이동 로직 건너뛰고 멈춤
            }
        }
        
        Point nextStep = Pathfinder.findNextStep(dungeon, this.x, this.y, target.x, target.y); // Pathfinder를 사용하여 다음 이동할 칸의 좌표를 얻음

        if (nextStep != null) {
            this.dx = nextStep.x - this.x;
            this.dy = nextStep.y - this.y;
            
            // 1. 타일 상태 업데이트 (이동 전)
            Tile currentTile = dungeon.getTile(this.x, this.y);
            if (currentTile != null) currentTile.setIsEnemyhere(false); 
            
            // 2. 이동 실행
            move(dungeon);
            
            // 3. 타일 상태 업데이트 (이동 후)
            Tile newTile = dungeon.getTile(this.x, this.y);
            if (newTile != null) newTile.setIsEnemyhere(true);
        }
    }

    @Override
    public void draw(Graphics g, int tileSize) {
        g.setColor(Color.RED); 
        g.fillOval(x * tileSize, y * tileSize, tileSize, tileSize);

        healthBarRenderer.draw(g, this.x, this.y, tileSize, this.getStats());
    }
}

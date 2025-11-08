package com.game_adventure.entity;

import com.game_adventure.map.Dungeon;
import java.awt.Color; // import
import java.awt.Graphics; // import
import java.lang.Math; // import

public class Enemy extends Entity{
    protected int detectionRangeSquared;
    protected int addtionalRangeSquared;
    private final int MOVE_SPEED_FACTOR = 20; // 적이 몇 프레임마다 한 번 움직일지 설정
    private int frameCounter = 0; // 현재 프레임 카운터를 저장
    private boolean playerDetected = false;
    private int curruntdetectionRangeSquared;

    public Enemy (int x, int y){
        super(x, y);
        this.detectionRangeSquared = 9; // 기본 탐지 범위 설정 피타고라스라 제곱
        this.addtionalRangeSquared = 9; // 추가 탐지 범위 설정
        this.curruntdetectionRangeSquared = detectionRangeSquared; // 현재 탐지 범위 기본 초기화
    }

    private final int initialX = this.x;
    private final int initialY = this.y;

    public void move(int dx, int dy, Dungeon dungeon) {
        int newX = this.x + dx;
        int newY = this.y + dy;

        if (dungeon.isWalkable(newX, newY)) {
            this.x = newX;
            this.y = newY;
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
            // 이미 탐지된 상태에서 탐지 유지 범위(detection + additional)를 벗어났다면
            if (distanceSquared > curruntdetectionRangeSquared) {
                this.playerDetected = false; // 탐지 해제
                curruntdetectionRangeSquared = detectionRangeSquared; // 탐지 범위 기본으로 초기화
            }
        } 
        else { 
            // 탐지 조건 검사 전에 현재 탐지 범위를 기본으로 재설정합니다.
            // 미탐지 상태에서는 넓은 범위가 적용되지 않도록 합니다.
            this.curruntdetectionRangeSquared = this.detectionRangeSquared; 

            // 아직 탐지되지 않은 상태에서 detectionRange안에 들어왔다면
            if (distanceSquared <= this.curruntdetectionRangeSquared) { 
                this.playerDetected = true; // 탐지 시작
                // 탐지 시작 시에만 추적 유지 범위로 넓힙니다.
                this.curruntdetectionRangeSquared = this.detectionRangeSquared + this.addtionalRangeSquared;
            } else {
                // 탐지 범위 밖에 있으므로, 추적 및 이동 로직을 수행하지 않고 다음으로 넘어갑니다.
            }
        }

        int dx;
        int dy;
        int targetXDiff; // 이동 목표와의 최종 X 거리 차이
        int targetYDiff; // 이동 목표와의 최종 Y 거리 차이

        if (playerDetected) { 
            // 플레이어 탐지 상태: 플레이어 쪽으로 이동
            dx = Integer.compare(player.getX(), this.x);
            dy = Integer.compare(player.getY(), this.y);
            targetXDiff = playerXDiff;
            targetYDiff = playerYDiff;
        }
        else { 
            // 플레이어 미탐지 상태: 초기 위치로 이동
            dx = Integer.compare(initialX, this.x);
            dy = Integer.compare(initialY, this.y);
            targetXDiff = initialX - this.x; 
            targetYDiff = initialY - this.y;
            
            // 초기 위치에 도착했다면 즉시 멈춤
            if (this.x == initialX && this.y == initialY) {
                dx = 0;
                dy = 0;
            }
        }

        int priorityDx = dx;
        int priorityDy = dy;
        int secondaryDx = 0;
        int secondaryDy = 0;
        
        // 2. 직선 우선 결정
        if (dx != 0 && dy != 0) {
            boolean preferX = Math.abs(targetXDiff) > Math.abs(targetYDiff);
            
            if (preferX) {
                // X축 우선: 주(X), 부(Y) 설정
                priorityDy = 0; 
                secondaryDx = 0;
                secondaryDy = dy; // Y축으로 대체 가능성 저장
            } else {
                // Y축 우선: 주(Y), 부(X) 설정
                priorityDx = 0;
                secondaryDx = dx; // X축으로 대체 가능성 저장
                secondaryDy = 0;
            }
        }
        
        // 3. 이동 시도 (move() 함수가 isWalkable을 체크합니다.)
        if (dungeon.isWalkable(this.x + priorityDx, this.y + priorityDy)) {
            // 1순위 이동 가능하면 이동
            move(priorityDx, priorityDy, dungeon);
        } 
        else if ((secondaryDx != 0 || secondaryDy != 0) && 
                dungeon.isWalkable(this.x + secondaryDx, this.y + secondaryDy)) {
            // 2순위 이동 가능하면 이동
            move(secondaryDx, secondaryDy, dungeon);
        }
    }

    @Override
    public void draw(Graphics g, int tileSize) {
        g.setColor(Color.RED); 
        g.fillOval(x * tileSize, y * tileSize, tileSize, tileSize);
    }
}

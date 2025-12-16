package com.game_adventure.entity;

import java.awt.Graphics; // Swing 그래픽 import

import com.game_adventure.map.Dungeon;

public abstract class Entity {
    protected int x; // 타일 좌표 (x)
    protected int y; // 타일 좌표 (y)

    protected Stats stats; // 엔티티의 스테이터스

    public Entity(int x, int y, Stats stats) {
        this.x = x;
        this.y = y;
        this.stats = stats;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    //플레이어 위치를 새로 설정하는 메서드
    public void setPosition(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }
    
    // Stats 생성 메서드
    protected static Stats createStats(int maxHp, int attackPower) { 
        return new Stats(maxHp, attackPower); // 스테이터스 생성, 최대 체력과 공격력 설정
    }

    public void move(int dx, int dy, Dungeon dungeon) {
        int newX = this.x + dx; // 새로운 좌표 계산
        int newY = this.y + dy; // 새로운 좌표 계산

        if (dungeon.isWalkable(newX, newY)) { // 던전 맵에서 이동 가능 여부 확인
            this.x = newX;
            this.y = newY;
        }
        else {
            // 이동 불가 시 처리 로직 (필요시 구현)
        }
    }

    // 공격받았을 때 Stats의 체력을 깎는 메서드
    public void onAttacked(int damage) { 
        if (this.stats != null) { // stats가 있을 때만 데미지 적용
            this.stats.takeDamage(damage); // 데미지 적용
            System.out.println(this.getClass().getSimpleName() + " took " + damage + " damage!"); // 콘솔 출력, 디버그용
        }
    }

    // stats가 있을 때 사망 여부 반환해주는 메서드
    public boolean isDead() { 
        return this.stats != null && this.stats.isDead(); 
    }
    public Stats getStats() { return stats; } // Stats Getter

    // 엔티티 스스로를 그리는 메서드
    public abstract void draw(Graphics g, int tileSize);
}
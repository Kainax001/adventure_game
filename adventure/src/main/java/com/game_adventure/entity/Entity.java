package com.game_adventure.entity;

import java.awt.Graphics; // Swing 그래픽 import

import com.game_adventure.map.Dungeon;

public abstract class Entity {
    protected int x; // 타일 좌표 (x)
    protected int y; // 타일 좌표 (y)

    protected Stats stats;

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
        return new Stats(maxHp, attackPower);
    }

    public void move(int dx, int dy, Dungeon dungeon) {
        int newX = this.x + dx;
        int newY = this.y + dy;
        
        if (dungeon.isWalkable(newX, newY)) {
            this.x = newX;
            this.y = newY;
        }
        else {
            // 이동 불가 시 처리 로직 (필요시 구현)
        }
    }

    // 공격받았을 때 Stats의 체력을 깎는 메서드
    public void onAttacked(int damage) {
        if (this.stats != null) {
            this.stats.takeDamage(damage);
            System.out.println(this.getClass().getSimpleName() + " took " + damage + " damage!");
        }
    }

    // 사망 여부를 확인하는 메서드
    public boolean isDead() {
        // stats가 없으면 죽은 것으로 간주하거나 stats의 상태를 리턴
        return this.stats != null && this.stats.isDead();
    }

    public Stats getStats() {
        return stats;
    }

    // 엔티티 스스로를 그리는 메서드
    public abstract void draw(Graphics g, int tileSize);
}
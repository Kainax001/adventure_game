package com.game_adventure.entity;

public class Stats {
    private int maxHp;
    private int currentHp;
    private int attackPower;

    public Stats(int maxHp, int attackPower) {
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.attackPower = attackPower;
    }

    // 피해를 입는 로직
    public void takeDamage(int damage) { // 피해량만큼 체력 감소
        this.currentHp -= damage;
        if (this.currentHp < 0) { // 체력이 0 이하로 떨어지면
            this.currentHp = 0; // 체력을 0으로 고정
        }
    }

    // 회복 로직
    public void heal(int amount) { // 회복량만큼 체력 증가
        this.currentHp += amount; // 회복
        if (this.currentHp > maxHp) { // 최대 체력 초과 방지
            this.currentHp = maxHp; // 최대 체력으로 고정
        }
    }

    // Getter
    public boolean isDead() { return this.currentHp <= 0; } // 사망 여부 확인 메서드
    public int getCurrentHp() { return currentHp; } // 현재 체력 반환 메서드
    public int getMaxHp() { return maxHp; } // 최대 체력 반환 메서드
    public int getAttackPower() { return attackPower; } // 공격력 반환 메서드
}
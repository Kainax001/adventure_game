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
    public void takeDamage(int damage) {
        this.currentHp -= damage;
        if (this.currentHp < 0) {
            this.currentHp = 0;
        }
    }

    // 회복 로직
    public void heal(int amount) {
        this.currentHp += amount;
        if (this.currentHp > maxHp) {
            this.currentHp = maxHp;
        }
    }

    public boolean isDead() {
        return this.currentHp <= 0;
    }

    // Getter
    public int getCurrentHp() { return currentHp; }
    public int getMaxHp() { return maxHp; }
    public int getAttackPower() { return attackPower; }
}
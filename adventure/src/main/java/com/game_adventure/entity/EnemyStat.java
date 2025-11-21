package com.game_adventure.entity;

public enum EnemyStat {
    // 상수 정의 (최소값, 최대값)
    HP(20, 100),
    ATK(5, 15);

    private final int min;
    private final int max;

    EnemyStat(int min, int max) {
        this.min = min;
        this.max = max;
    }

    // 랜덤 로직
    public int getRandomValue() {
        return (int)(Math.random() * (max - min + 1)) + min;
    }

    public int getMin() { return min; }
    public int getMax() { return max; }

    // Stats 객체를 생성해서 반환하는 정적 메서드
    public static Stats createRandomStats() {
        int randomHp = HP.getRandomValue();
        int randomAtk = ATK.getRandomValue();
        
        // Stats 생성자 호출
        return new Stats(randomHp, randomAtk);
    }
}
package com.game_adventure.entity;

import java.awt.Color;

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

    public static Color calculateColorByHp(int currentMaxHp) {
        int minHp = HP.min;
        int maxHpLimit = HP.max;

        // 비율 계산 (0.0 ~ 1.0)
        float ratio = (float)(currentMaxHp - minHp) / (maxHpLimit - minHp);
        
        // 안전장치 (비율이 0보다 작거나 1보다 크면 조정)
        if (ratio < 0) ratio = 0;
        if (ratio > 1) ratio = 1;

        // 비율에 따라 명도 조절 (셀수록 어둡게)
        int redValue = 255 - (int)(ratio * 155); 
        
        // 색상 범위 안전장치
        if (redValue < 50) redValue = 50;
        if (redValue > 255) redValue = 255;

        return new Color(redValue, 0, 0);
    }
}
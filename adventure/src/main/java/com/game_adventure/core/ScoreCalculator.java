package com.game_adventure.core; // 또는 logic 패키지

import com.game_adventure.entity.Enemy;
import com.game_adventure.entity.Stats;

public class ScoreCalculator {

    // 점수 밸런스 조절용 상수
    private static final int HP_SCORE_MULTIPLIER = 1;   // 체력 1당 1점
    private static final int ATK_SCORE_MULTIPLIER = 5;  // 공격력 1당 5점

    // 적의 능력치를 기반으로 처치 점수를 계산하는 메서드
    public static int calculateKillReward(Enemy enemy) {
        if (enemy == null || enemy.getStats() == null) {
            return 0;
        }

        Stats stats = enemy.getStats();
        int maxHp = stats.getMaxHp();
        int attackPower = stats.getAttackPower();
        
        int score = (maxHp * HP_SCORE_MULTIPLIER) + (attackPower * ATK_SCORE_MULTIPLIER);

        return score;
    }
}
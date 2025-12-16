package com.game_adventure.core; // 또는 logic 패키지

import com.game_adventure.entity.Enemy;
import com.game_adventure.entity.Stats;

public class ScoreCalculator {

    // 점수 밸런스 조절용 상수
    private static final int HP_SCORE_MULTIPLIER = 1;   // 체력 1당 1점
    private static final int ATK_SCORE_MULTIPLIER = 5;  // 공격력 1당 5점

    // 적의 능력치를 기반으로 처치 점수를 계산하는 메서드
    public static int calculateKillReward(Enemy enemy) {
        if (enemy == null || enemy.getStats() == null) { // 안전장치, null 체크
            return 0;
        }

        Stats stats = enemy.getStats(); // 적의 능력치 가져오기
        int maxHp = stats.getMaxHp(); // 최대 체력
        int attackPower = stats.getAttackPower(); // 공격력
        
        int score = (maxHp * HP_SCORE_MULTIPLIER) + (attackPower * ATK_SCORE_MULTIPLIER); // 점수 계산

        return score; // 계산된 점수 반환
    }
}
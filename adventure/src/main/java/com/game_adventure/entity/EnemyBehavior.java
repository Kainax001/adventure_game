package com.game_adventure.entity;

import com.game_adventure.combat.Battle;
import com.game_adventure.combat.CombatCalculator;
import com.game_adventure.map.Dungeon;
import com.game_adventure.map.Tile;
import java.awt.Point;

public class EnemyBehavior {

    // 1. 적의 행동 결정 (Update)
    public static void processUpdate(Enemy enemy, Dungeon dungeon, Player player) {
        // (1) 속도 조절
        enemy.setFrameCounter(enemy.getFrameCounter() + 1);
        if (enemy.getFrameCounter() < enemy.getMoveSpeedFactor()) {
            return;
        }
        enemy.setFrameCounter(0);

        // (2) 플레이어 탐지 로직
        int playerXDiff = player.getX() - enemy.getX();
        int playerYDiff = player.getY() - enemy.getY();
        int distanceSquared = playerXDiff * playerXDiff + playerYDiff * playerYDiff;

        if (enemy.isPlayerDetected()) {
            if (distanceSquared > enemy.getCurrentDetectionRangeSquared()) {
                enemy.setPlayerDetected(false);
                enemy.setCurrentDetectionRangeSquared(enemy.getDetectionRangeSquared());
            }
        } else {
            enemy.setCurrentDetectionRangeSquared(enemy.getDetectionRangeSquared());
            if (distanceSquared <= enemy.getCurrentDetectionRangeSquared()) {
                enemy.setPlayerDetected(true);
                enemy.setCurrentDetectionRangeSquared(enemy.getDetectionRangeSquared() + enemy.getAdditionalRangeSquared());
            }
        }

        // 목표 설정 및 길찾기
        Point target;
        if (enemy.isPlayerDetected()) {
            target = new Point(player.getX(), player.getY());
        } else {
            target = new Point(enemy.getInitialX(), enemy.getInitialY());
            if (enemy.getX() == enemy.getInitialX() && enemy.getY() == enemy.getInitialY()) {
                return; // 제자리 복귀 완료 시 중단
            }
        }

        // Pathfinder 호출
        Point nextStep = Pathfinder.findNextStep(dungeon, enemy.getX(), enemy.getY(), target.x, target.y);

        if (nextStep != null) {
            // 다음 이동 방향 설정
            enemy.setDx(nextStep.x - enemy.getX());
            enemy.setDy(nextStep.y - enemy.getY());

            // 실제 이동 처리
            processMovement(enemy, dungeon, player);
        }
    }

    // 2. 적의 물리적 이동 및 충돌
    private static void processMovement(Enemy enemy, Dungeon dungeon, Player player) {
        int newX = enemy.getX() + enemy.getDx();
        int newY = enemy.getY() + enemy.getDy();

        Tile currentTile = dungeon.getTile(enemy.getX(), enemy.getY());
        Tile newTile = dungeon.getTile(newX, newY);

        // 플레이어와 충돌 시 (전투)
        if (newTile.isPlayerhere()) {
            // CombatCalculator에게 전투 위임 (적은 대시 없음 -> false)
            CombatCalculator.calculateDamage(enemy, player, false);

            // 플레이어 사망 체크 (게임 오버 등)
            if (player.isDead()) {
                System.out.println("플레이어 사망!");
            } 
            else {
                // 밀쳐내기 로직
                int oldPlayerX = player.getX();
                int oldPlayerY = player.getY();

                // 플레이어 타일 비우기
                Tile playerCurrentTile = dungeon.getTile(oldPlayerX, oldPlayerY);
                playerCurrentTile.setIsPlayerhere(false);

                // 플레이어 밀기
                Battle.pushEntity(player, enemy.getDx(), enemy.getDy(), dungeon, 2);

                // 플레이어 새 위치 갱신
                Tile playerNewTile = dungeon.getTile(player.getX(), player.getY());
                playerNewTile.setIsPlayerhere(true);

                // 플레이어가 밀려나서 자리가 비었으면 적이 그 자리로 이동
                if (player.getX() != oldPlayerX || player.getY() != oldPlayerY) {
                    currentTile.setIsEnemyhere(false);
                    enemy.setPosition(newX, newY); // 좌표 이동
                    newTile.setIsEnemyhere(true);
                }
            }
        }
        // 빈 땅 이동
        else {
            currentTile.setIsEnemyhere(false);
            enemy.setPosition(newX, newY); // 좌표 이동
            newTile.setIsEnemyhere(true);
        }

        // 이동 후 방향 초기화
        enemy.setDx(0);
        enemy.setDy(0);
    }
}
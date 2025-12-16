package com.game_adventure.entity;

import com.game_adventure.combat.Battle;
import com.game_adventure.combat.CombatCalculator;
import com.game_adventure.map.Dungeon;
import com.game_adventure.map.Tile;
import java.awt.Point;

public class EnemyBehavior {

    // 1. 적의 행동 결정 및 경로 탐색
    public static void processUpdate(Enemy enemy, Dungeon dungeon, Player player) {
        // (1) 속도 조절
        enemy.setFrameCounter(enemy.getFrameCounter() + 1); // 프레임 카운터 증가
        if (enemy.getFrameCounter() < enemy.getMoveSpeedFactor()) { // 이동 속도에 도달하지 않았으면
            return; // 이동하지 않음
        }
        enemy.setFrameCounter(0); // 카운터 초기화

        // (2) 플레이어 탐지 로직
        int playerXDiff = player.getX() - enemy.getX(); // 플레이어와 적의 X 좌표 차이
        int playerYDiff = player.getY() - enemy.getY(); // 플레이어와 적의 Y 좌표 차이
        int distanceSquared = playerXDiff * playerXDiff + playerYDiff * playerYDiff; // 거리 제곱 계산

        // 탐지 상태 업데이트
        if (enemy.isPlayerDetected()) { // 이미 탐지된 상태라면
            if (distanceSquared > enemy.getCurrentDetectionRangeSquared()) { // 탐지 범위를 벗어났다면
                enemy.setPlayerDetected(false); // 탐지 해제
                enemy.setCurrentDetectionRangeSquared(enemy.getDetectionRangeSquared()); // 탐지 범위 초기화
            }
        } 
        else { // 아직 탐지되지 않은 상태라면
            enemy.setCurrentDetectionRangeSquared(enemy.getDetectionRangeSquared()); // 탐지 범위 초기화
            if (distanceSquared <= enemy.getCurrentDetectionRangeSquared()) { // 탐지 범위 내에 있다면
                enemy.setPlayerDetected(true); // 탐지 상태로 전환
                enemy.setCurrentDetectionRangeSquared(enemy.getDetectionRangeSquared() + enemy.getAdditionalRangeSquared()); // 탐지 범위 확장
            }
        }

        // 목표 설정 및 길찾기
        Point target; // 목표 좌표
        if (enemy.isPlayerDetected()) { // 플레이어 탐지 상태라면
            target = new Point(player.getX(), player.getY()); // 플레이어 위치를 목표로 설정
        } 
        else { // 탐지되지 않은 상태라면
            target = new Point(enemy.getInitialX(), enemy.getInitialY()); // 초기 위치를 목표로 설정
            if (enemy.getX() == enemy.getInitialX() && enemy.getY() == enemy.getInitialY()) { // 제자리 복귀 완료 시
                return; // 이동하지 않음
            }
        }

        // Pathfinder 호출
        Point nextStep = Pathfinder.findNextStep(dungeon, enemy.getX(), enemy.getY(), target.x, target.y); // 다음 이동 칸 찾기

        if (nextStep != null) {
            // 다음 이동 방향 설정
            enemy.setDx(nextStep.x - enemy.getX()); // 이동할 x 방향
            enemy.setDy(nextStep.y - enemy.getY()); // 이동할 y 방향

            processMovement(enemy, dungeon, player); // 실제 이동 처리
        }
    }

    // 2. 적의 물리적 이동 및 충돌
    private static void processMovement(Enemy enemy, Dungeon dungeon, Player player) {
        int newX = enemy.getX() + enemy.getDx(); // 새로운 X 좌표
        int newY = enemy.getY() + enemy.getDy(); // 새로운 Y 좌표

        Tile currentTile = dungeon.getTile(enemy.getX(), enemy.getY()); // 현재 타일
        Tile newTile = dungeon.getTile(newX, newY); // 이동할 타일

        // 플레이어와 충돌 시 (전투)
        if (newTile.isPlayerhere()) { // 플레이어가 있는 타일이라면
            // CombatCalculator에게 전투 위임 (적은 대시 없음 -> false)
            CombatCalculator.calculateDamage(enemy, player, false); // 적이 플레이어 공격

            if (player.isDead()) { // 플레이어가 사망했을 경우
                System.out.println("플레이어 사망!"); // 콘솔 출력
            } 
            else { // 플레이어가 아직 살아있다면

                // 밀쳐내기 로직
                int oldPlayerX = player.getX(); // 이전 플레이어 X 좌표
                int oldPlayerY = player.getY(); // 이전 플레이어 Y 좌표

                // 플레이어 타일 비우기
                Tile playerCurrentTile = dungeon.getTile(oldPlayerX, oldPlayerY); // 플레이어의 현재 타일
                playerCurrentTile.setIsPlayerhere(false); // 플레이어가 밀려나기 전 타일 상태 갱신

                // 플레이어 밀기
                Battle.pushEntity(player, enemy.getDx(), enemy.getDy(), dungeon, 2); // 2칸 밀쳐내기

                // 플레이어 새 위치 갱신
                Tile playerNewTile = dungeon.getTile(player.getX(), player.getY()); // 플레이어의 새로운 타일
                playerNewTile.setIsPlayerhere(true); // 플레이어가 밀려난 후 타일 상태 갱신

                // 플레이어가 밀려나서 자리가 비었으면 적이 그 자리로 이동
                if (player.getX() != oldPlayerX || player.getY() != oldPlayerY) { // 플레이어가 실제로 밀려났다면
                    currentTile.setIsEnemyhere(false); // 적이 있던 타일 상태 갱신
                    enemy.setPosition(newX, newY); // 좌표 이동
                    newTile.setIsEnemyhere(true); // 적이 이동한 타일 상태 갱신
                }
            }
        }
        else { // 빈 땅으로 이동
            currentTile.setIsEnemyhere(false); // 적이 있던 타일 상태 갱신
            enemy.setPosition(newX, newY); // 좌표 이동
            newTile.setIsEnemyhere(true); // 적이 이동한 타일 상태 갱신
        }

        // 이동 후 방향 초기화
        enemy.setDx(0);
        enemy.setDy(0);
    }
}
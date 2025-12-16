package com.game_adventure.entity; // 또는 com.game_adventure.core

import com.game_adventure.combat.Battle;
import com.game_adventure.combat.CombatCalculator;
import com.game_adventure.map.Dungeon;
import com.game_adventure.map.Tile;
import com.game_adventure.core.ScoreCalculator;

public class PlayerMovementLogic {

    /**
     * 플레이어의 이동 및 충돌 처리를 담당하는 정적 메서드
     */
    public static void processMovement(Player player, int dx, int dy, Dungeon dungeon, boolean isDashing) {
        
        // 1. 쿨다운 체크
        // 대시가 아닐 때, 타이머가 남아있다면 이동 불가
        if (!isDashing && player.getMoveTimer() > 0) {
            return; 
        }

        int currentX = player.getX();
        int currentY = player.getY();
        int newX = currentX + dx;
        int newY = currentY + dy;

        Tile currentTile = dungeon.getTile(currentX, currentY);
        Tile newtile = dungeon.getTile(newX, newY);

        // 2. 적과 충돌 시 (전투 발생)
        if (newtile.isEnemyhere()) {
            int enemyX = newtile.getX();
            int enemyY = newtile.getY();
            Entity enemy = dungeon.getEnemyAt(enemyX, enemyY);

            if (enemy != null) {
                // [전투 로직] CombatCalculator에게 위임
                CombatCalculator.calculateDamage(player, enemy, isDashing);

                if (enemy.isDead()) { // 적 사망 처리
                    if (enemy instanceof Enemy) {
                        int reward = ScoreCalculator.calculateKillReward((Enemy) enemy); // 점수 보상 계산
                        
                        int currentScore = player.getScore(); // 현재 점수 가져오기
                        player.setScore(currentScore + reward); // 점수 갱신
                        
                        System.out.println("점수 획득! +" + reward + " (현재 점수: " + player.getScore() + ")"); // 콘솔 출력
                    }
                    
                    newtile.setIsEnemyhere(false); // 적이 죽었으므로 타일에서 적 제거
                    dungeon.removeEnemy(enemy); // 던전의 적 리스트에서도 제거
                }
                else { // 적이 아직 살아있다면 밀쳐내기 시도
                    newtile.setIsEnemyhere(false); // 밀쳐내기 전 적이 있던 타일 상태 갱신

                    Battle.pushEntity(enemy, dx, dy, dungeon, 1); // 1칸 밀쳐내기
                    Tile enemyNextTile = dungeon.getTile(enemy.getX(), enemy.getY()); // 적의 새로운 타일

                    enemyNextTile.setIsEnemyhere(true); // 적의 새로운 타일 상태 갱신
                }
            }

            player.setMoveTimer(player.getMoveSpeedFactor()); // 행동을 했으므로 쿨다운 설정
        }
        
        // 3. 빈 땅 이동 (전투가 아닐 경우)
        else if (dungeon.isWalkable(newX, newY)) { // 이동 가능한 타일인지 확인
            
            currentTile.setIsPlayerhere(false); // 타일 점유 상태 갱신
            player.setPosition(newX, newY); // Player의 좌표를 강제로 변경 (Entity.setPosition 활용)
            newtile.setIsPlayerhere(true); // 이동할 타일의 점유 상태 갱신 

            player.setMoveTimer(player.getMoveSpeedFactor()); // 이동했으므로 쿨다운 설정
        }
    }
}
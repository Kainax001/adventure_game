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

                // 적 사망 처리
                if (enemy.isDead()) {
                    if (enemy instanceof Enemy) {
                        int reward = ScoreCalculator.calculateKillReward((Enemy) enemy);
                        
                        int currentScore = player.getScore();
                        player.setScore(currentScore + reward);
                        
                        System.out.println("점수 획득! +" + reward + " (현재 점수: " + player.getScore() + ")");
                    }
                    
                    newtile.setIsEnemyhere(false);
                    dungeon.removeEnemy(enemy);
                }
                else {
                    // 적 생존 시 밀쳐내기 (Battle 클래스 위임)
                    newtile.setIsEnemyhere(false); 
                    Battle.pushEntity(enemy, dx, dy, dungeon, 1); 
                    
                    Tile enemyNextTile = dungeon.getTile(enemy.getX(), enemy.getY());
                    enemyNextTile.setIsEnemyhere(true);
                }
            }
            // 행동을 했으므로 쿨다운 설정
            player.setMoveTimer(player.getMoveSpeedFactor()); 
        }
        
        // 3. 빈 땅 이동 (전투가 아닐 경우)
        else if (dungeon.isWalkable(newX, newY)) {
            // 타일 점유 상태 갱신
            currentTile.setIsPlayerhere(false); 
            
            // Player의 좌표를 강제로 변경 (Entity.setPosition 활용)
            player.setPosition(newX, newY);
            
            newtile.setIsPlayerhere(true); 

            // 이동했으므로 쿨다운 설정
            player.setMoveTimer(player.getMoveSpeedFactor()); 
        }
    }
}
package com.game_adventure.combat;

import com.game_adventure.map.Dungeon;
import com.game_adventure.map.Tile;
import com.game_adventure.entity.Entity;

public class Battle {
    /**
     * 특정 엔티티를 목표 방향으로 밀어내기를 시도하고, 막힌 횟수를 반환합니다.
     * @param targetEntity 밀려날 대상 엔티티 (Player 또는 Enemy)
     * @param pushDx 밀어낼 방향 X
     * @param pushDy 밀어낼 방향 Y
     * @param attempts 밀어내기 시도 횟수
     * @return 엔티티의 이동이 막힌 총 횟수
     */
    public static int pushEntity(Entity targetEntity, int pushDx, int pushDy, Dungeon dungeon, int attempts) {
        int blockedCount = 0;

        for (int attempt = 0; attempt < attempts; attempt++) {
            int currentX = targetEntity.getX();
            int currentY = targetEntity.getY();

            int nextX = currentX + pushDx;
            int nextY = currentY + pushDy;

            // 1. 벽인지 확인 (벽이면 못 밀림)
            if (!dungeon.isWalkable(nextX, nextY)) {
                blockedCount++; 
                break; // 즉시 중단
            }

            // 2. 뒤에 다른 적이 있는지 확인 (겹침 방지)
            Tile nextTile = dungeon.getTile(nextX, nextY);
            if (nextTile.isEnemyhere()) { 
                blockedCount++;
                break; 
            }

            // 3. [핵심 수정] move() 대신 setPosition() 사용
            // 이동 로직(속도, 애니메이션 등)을 무시하고 좌표만 즉시 변경 -> 딜레이 없음
            targetEntity.setPosition(nextX, nextY); 
        }
        
        return blockedCount; // 막힌 횟수 반환 (0이면 성공, >0이면 실패)
    }
}
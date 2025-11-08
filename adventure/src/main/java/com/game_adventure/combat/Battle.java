package com.game_adventure.combat;

import com.game_adventure.map.Dungeon;
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
            int oldX = targetEntity.getX();
            int oldY = targetEntity.getY();
            
            // [주의] targetEntity가 Player일 경우, Player.move(dx, dy, dungeon)을 호출합니다.
            // Entity 클래스에 move 메서드가 구현되어 있으면 (Entity.move(int dx, int dy, Dungeon dungeon) 형태)
            // 모든 엔티티가 이 move 메서드를 사용하도록 코드를 통일해야 합니다.

            // 1. 엔티티에게 이동 요청 (해당 엔티티의 move 로직이 호출됨)
            // Entity 추상 클래스가 move(int dx, int dy, Dungeon dungeon)을 가지도록 가정합니다.
            targetEntity.move(pushDx, pushDy, dungeon); 
            
            // 2. 위치가 변하지 않았다면 (이동이 막혔다면)
            if (targetEntity.getX() == oldX && targetEntity.getY() == oldY) {
                blockedCount++; 
            }
        }
        return blockedCount;
    }
}
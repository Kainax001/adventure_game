package com.game_adventure.entity; // 적절한 패키지 선택

import com.game_adventure.map.Dungeon;
import com.game_adventure.map.Tile;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Map;
import java.util.HashMap;
import java.awt.Point;

public class Pathfinder {

    /**
     * BFS를 사용하여 start에서 end까지의 최단 경로의 '다음 칸' 좌표를 반환합니다.
     * @return 다음 이동할 Point 객체, 경로가 없거나 실패 시 null 반환.
     */
    public static Point findNextStep(Dungeon dungeon, int startX, int startY, int endX, int endY) {
        // 이미 목표에 도달했거나, 시작 위치와 목표 위치가 같으면 이동할 필요 없음
        if (startX == endX && startY == endY) {
            return null;
        }

        Map<Point, Point> cameFrom = new HashMap<>(); 
        Queue<Point> queue = new LinkedList<>();
        
        Point start = new Point(startX, startY);
        Point end = new Point(endX, endY);
        
        queue.add(start);
        cameFrom.put(start, null);

        int[] dx = {0, 0, 1, -1}; // 상, 하, 우, 좌
        int[] dy = {1, -1, 0, 0};

        Point foundEnd = null;

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            if (current.equals(end)) {
                foundEnd = current;
                break; // 경로 찾기 성공
            }

            for (int i = 0; i < 4; i++) {
                int nextX = current.x + dx[i];
                int nextY = current.y + dy[i];
                Point next = new Point(nextX, nextY);
                
                // 맵 경계/벽 체크 및 이미 방문했는지 체크
                if (dungeon.isWalkable(nextX, nextY) && !cameFrom.containsKey(next)) {
                    // 추가 충돌 방지 로직 (다른 적이 있는 타일은 막음)
                    Tile nextTile = dungeon.getTile(nextX, nextY);
                    if (nextTile != null && nextTile.isEnemyhere()) {
                         // 목표 지점(end)은 예외적으로 허용
                        if (nextX != end.x || nextY != end.y) {
                            continue; 
                        }
                    }

                    cameFrom.put(next, current);
                    queue.add(next);
                }
            }
        }
        
        if (foundEnd == null) {
            return null; // 경로 찾기 실패
        }

        // 경로 재구성: 시작점에서 두 번째 칸을 찾습니다.
        Point nextStep = foundEnd;
        Point previous = cameFrom.get(nextStep);
        
        // 경로가 시작점 바로 옆일 경우
        while (previous != null && !previous.equals(start)) {
            nextStep = previous;
            previous = cameFrom.get(previous);
        }

        // nextStep이 시작점과 같으면 이동할 칸이 없는 것입니다.
        if (nextStep.equals(start)) {
            return null;
        }
        
        return nextStep; // 시작점에서 다음 칸을 반환
    }
}
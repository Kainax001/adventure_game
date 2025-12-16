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
    public static Point findNextStep(Dungeon dungeon, int startX, int startY, int endX, int endY) { // BFS 기반 경로 탐색 메서드
        if (startX == endX && startY == endY) { // 이미 목표에 도달했거나, 시작 위치와 목표 위치가 같으면 
            return null; // 이동할 필요 없음
        }

        Map<Point, Point> cameFrom = new HashMap<>(); // 경로 추적용 맵 
        Queue<Point> queue = new LinkedList<>(); // BFS 큐
        
        Point start = new Point(startX, startY); // 시작점
        Point end = new Point(endX, endY); // 목표점
        
        queue.add(start); // 시작점 큐에 추가
        cameFrom.put(start, null); // 시작점의 이전 칸은 없음

        int[] dx = {0, 0, 1, -1}; // x 좌표 상, 하, 우, 좌
        int[] dy = {1, -1, 0, 0}; // y 좌표 상, 하, 우, 좌

        Point foundEnd = null; // 목표 지점 발견 시 저장할 변수

        while (!queue.isEmpty()) { // BFS 탐색 시작
            Point current = queue.poll();

            if (current.equals(end)) { // 목표 지점 도달 시
                foundEnd = current; // 발견 표시
                break; // 경로 찾기 성공 
            }

            for (int i = 0; i < 4; i++) { // 상, 하, 좌, 우 인접 칸 탐색
                int nextX = current.x + dx[i]; // 다음 x 좌표
                int nextY = current.y + dy[i]; // 다음 y 좌표
                Point next = new Point(nextX, nextY); // 다음 칸 좌표

                if (dungeon.isWalkable(nextX, nextY) && !cameFrom.containsKey(next)) { // 맵 경계/벽 체크 및 이미 방문했는지 체크

                    // 추가 충돌 방지 로직 (다른 적이 있는 타일은 막음)
                    Tile nextTile = dungeon.getTile(nextX, nextY); // 다음 타일 가져오기
                    if (nextTile != null && nextTile.isEnemyhere()) { // 적이 있는 타일인 경우
                        if (nextX != end.x || nextY != end.y) { // 목표 지점이 아닌 경우
                            continue; // 이 칸은 건너뜀
                        }
                    }

                    cameFrom.put(next, current); // 이전 칸 기록
                    queue.add(next); // 큐에 다음 칸 추가
                }
            }
        }
        
        if (foundEnd == null) {
            return null; // 경로 찾기 실패
        }

        // 경로 재구성: 시작점에서 두 번째 칸을 찾기
        Point nextStep = foundEnd; // 목표 지점부터 시작
        Point previous = cameFrom.get(nextStep); //이전 칸

        while (previous != null && !previous.equals(start)) { // 경로가 시작점 바로 옆일 경우
            nextStep = previous; // 다음 칸 업데이트
            previous = cameFrom.get(previous); // 이전 칸으로 이동
        }

        // nextStep이 시작점과 같으면 이동할 칸이 없는 것이므로 null 반환
        if (nextStep.equals(start)) {
            return null;
        }
        
        return nextStep; // 시작점에서 다음 칸을 반환
    }
}
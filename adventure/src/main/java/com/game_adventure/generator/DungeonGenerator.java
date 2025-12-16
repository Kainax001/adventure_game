package com.game_adventure.generator;

import com.game_adventure.map.Dungeon;
import com.game_adventure.map.Tile;
import com.game_adventure.map.WallTile;
import com.game_adventure.map.FloorTile;
import com.game_adventure.entity.Enemy;
import com.game_adventure.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DungeonGenerator {

    private Tile[][] tiles;
    private List<Rect> roomList = new ArrayList<>(); // 생성된 방 목록

    public Dungeon generate(int width, int height) {
        this.tiles = new Tile[height][width];
        roomList.clear(); // 맵 재생성 시 목록 초기화

        // 1. 맵을 모두 벽으로 초기화
        for (int y = 0; y < height; y++) { // 세로
            for (int x = 0; x < width; x++) { // 가로
                tiles[y][x] = new WallTile(x, y); // 벽 타일로 초기화
            }
        }

        // 2. BSP 로직 실행
        BSPNode root = new BSPNode(new Rect(1, 1, width - 2, height - 2)); // 가장 바깥쪽은 벽으로 남기기 위해 1칸 여유
        root.split(); // 분할 수행
        root.createRoom(this); // 방 생성
        root.createCorridors(this); // 복도 생성

        // 3. 첫 번째 방 좌표 계산 (Player 생성하지 않음)
        if (roomList.isEmpty()) {
            createRoom(new Rect(width/2 - 2, height/2 - 2, 5, 5));
        }

        Rect firstRoom = roomList.get(0); // 첫 번째 방
        int startX = firstRoom.x + firstRoom.w / 2; // 방 중앙 x 좌표
        int startY = firstRoom.y + firstRoom.h / 2; // 방 중앙 y 좌표

        // 4. 출구 타일 배치
        if (!roomList.isEmpty()) {
            Rect lastRoom = roomList.get(roomList.size() - 1); // 마지막 방
            int exitX = lastRoom.x + lastRoom.w / 2; // 방 중앙 x 좌표
            int exitY = lastRoom.y + lastRoom.h / 2; // 방 중앙 y 좌표
            
            tiles[exitY][exitX] = new com.game_adventure.map.ExitTile(exitX, exitY); // 출구 타일 배치
        }

        // 5. Player 생성 및 던전에 연결
        // 임시 Player 객체를 생성하여 Dungeon에 전달하고, 시작 좌표를 함께 저장
        Player tempPlayer = new Player(startX, startY); // 임시 플레이어 생성
        tiles[startY][startX].setIsPlayerhere(true);// 플레이어 시작 위치는 바닥 타일로 설정
        Dungeon dungeon = new Dungeon(tiles, tempPlayer, startX, startY); // Dungeon 객체 생성

        // 6. 적 생성 및 배치 (50% 확률)
        Random rand = new Random();
        if (!roomList.isEmpty()) {
            for (Rect room : roomList) { // 각 방에 대해 리스트 순회
                if (room == firstRoom) continue; // 첫 방은 플레이어 시작 지점이므로 적을 배치하지 않음
                
                // 50% 확률로 적 생성
                if (rand.nextBoolean()) { // true 또는 false (50% 확률)
                    int enemyX = room.x + room.w / 2; // 방 중앙 x 좌표
                    int enemyY = room.y + room.h / 2; // 방 중앙 y 좌표

                    if (tiles[enemyY][enemyX] instanceof FloorTile) { // 바닥 타일인지 확인
                        Enemy enemy = new Enemy(enemyX, enemyY); // 적 생성
                        tiles[enemyY][enemyX].setIsEnemyhere(true); // 적이 있는 타일로 설정
                        dungeon.addEnemy(enemy); // 생성된 적을 던전에 추가
                    }
                }
            }
        }

        return dungeon; // 생성된 던전 객체 반환
    }

    /**
     * BSPNode가 호출할 공용 메서드 방을 타일맵에 "파냄"
     */
    void createRoom(Rect room) {
        for (int y = room.y; y < room.y + room.h; y++) {
            for (int x = room.x; x < room.x + room.w; x++) {
                // 맵 경계 체크
                if (y > 0 && y < tiles.length - 1 && x > 0 && x < tiles[0].length - 1) {
                    tiles[y][x] = new FloorTile(x, y);
                }
            }
        }
        roomList.add(room); // 생성된 방 목록에 추가
    }

    /**
     * BSPNode가 호출할 공용 메서드, L자 복도로 두 방을 연결
     */
    void connectRooms(Rect room1, Rect room2) {
        // 각 방의 중심 좌표
        int x1 = room1.x + room1.w / 2; // 방1 중심 x 좌표
        int y1 = room1.y + room1.h / 2; // 방1 중심 y 좌표
        int x2 = room2.x + room2.w / 2; // 방2 중심 x 좌표
        int y2 = room2.y + room2.h / 2; // 방2 중심 y 좌표

        // 50% 확률로 가로 먼저 뚫거나, 세로 먼저 뚫거나
        if (new Random().nextBoolean()) {
            // 가로 -> 세로
            createHCorridor(x1, x2, y1);
            createVCorridor(x2, y1, y2);
        } else {
            // 세로 -> 가로
            createVCorridor(x1, y1, y2);
            createHCorridor(x1, x2, y2);
        }
    }

    // 가로 복도를 파는 헬퍼 메서드
    private void createHCorridor(int x1, int x2, int y) {
        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            if (y > 0 && y < tiles.length - 1 && x > 0 && x < tiles[0].length - 1) {
                tiles[y][x] = new FloorTile(x, y);
            }
        }
    }

    // 세로 복도를 파는 헬퍼 메서드
    private void createVCorridor(int x, int y1, int y2) {
        for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
            if (y > 0 && y < tiles.length - 1 && x > 0 && x < tiles[0].length - 1) {
                tiles[y][x] = new FloorTile(x, y);
            }
        }
    }
}
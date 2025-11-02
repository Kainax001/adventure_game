package com.game_adventure.generator;

import com.game_adventure.map.Dungeon;
import com.game_adventure.map.Tile;
import com.game_adventure.map.WallTile;
import com.game_adventure.map.FloorTile;
import com.game_adventure.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DungeonGenerator {

    private Tile[][] tiles;
    private List<Rect> roomList = new ArrayList<>(); // 생성된 방 목록

    public Dungeon generate(int width, int height) {
        this.tiles = new Tile[height][width];

        // 1. 맵을 모두 벽으로 초기화
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tiles[y][x] = new WallTile();
            }
        }

        // 2. BSP 로직 실행
        BSPNode root = new BSPNode(new Rect(1, 1, width - 2, height - 2)); // (벽 여백 1칸)

        // 2-1. 맵 분할
        root.split(); 

        // 2-2. 리프 노드에 방 생성
        root.createRoom(this); 

        // 2-3. 방 생성 후 복도 연결 (이 코드가 있어야 방이 연결됩니다)
        root.createCorridors(this);

        // 3. 첫 번째 방에 플레이어 배치
        if (roomList.isEmpty()) {
            // BSP가 방을 못 만들었을 경우를 대비해 임시 방이라도 만듦
            createRoom(new Rect(width/2 - 2, height/2 - 2, 5, 5));
        }

        Rect firstRoom = roomList.get(0);
        int startX = firstRoom.x + firstRoom.w / 2;
        int startY = firstRoom.y + firstRoom.h / 2;

        Player player = new Player(startX, startY);

        // 4. Dungeon 객체 반환
        return new Dungeon(tiles, player);
    }

    /**
     * BSPNode가 호출할 공용 메서드 (방을 타일맵에 "파냄")
     * (private이 아니어야 BSPNode가 접근 가능)
     */
    void createRoom(Rect room) {
        for (int y = room.y; y < room.y + room.h; y++) {
            for (int x = room.x; x < room.x + room.w; x++) {
                // 맵 경계 체크
                if (y > 0 && y < tiles.length - 1 && x > 0 && x < tiles[0].length - 1) {
                    tiles[y][x] = new FloorTile();
                }
            }
        }
        roomList.add(room); // 생성된 방 목록에 추가
    }

    /**
     * BSPNode가 호출할 공용 메서드 (L자 복도로 두 방을 연결)
     * (private이 아니어야 BSPNode가 접근 가능)
     */
    void connectRooms(Rect room1, Rect room2) {
        // 각 방의 중심 좌표
        int x1 = room1.x + room1.w / 2;
        int y1 = room1.y + room1.h / 2;
        int x2 = room2.x + room2.w / 2;
        int y2 = room2.y + room2.h / 2;

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
                tiles[y][x] = new FloorTile();
            }
        }
    }

    // 세로 복도를 파는 헬퍼 메서드
    private void createVCorridor(int x, int y1, int y2) {
        for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
            if (y > 0 && y < tiles.length - 1 && x > 0 && x < tiles[0].length - 1) {
                tiles[y][x] = new FloorTile();
            }
        }
    }
}
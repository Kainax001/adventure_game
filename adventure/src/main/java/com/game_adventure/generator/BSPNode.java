package com.game_adventure.generator;

import java.util.Random;

// BSP 트리의 노드
public class BSPNode {
    public Rect rect; // 이 노드가 담당하는 구역
    public BSPNode leftChild;
    public BSPNode rightChild;

    // 이 노드(가 리프일 경우)에 생성된 실제 방
    private Rect room; 

    // 구역을 쪼개는 최소 크기
    private static final int MIN_LEAF_SIZE = 6;
    // 방의 최소 크기
    private static final int ROOM_MIN_SIZE = 4; 
    // 구역과 방 사이의 최소 여백
    private static final int ROOM_MARGIN = 1; 

    private static Random rand = new Random();

    public BSPNode(Rect rect) {
        this.rect = rect;
    }

    /**
     * [1단계: 구역 분할]
     * 이 노드의 구역(rect)을 재귀적으로 쪼갭니다.
     */
    public boolean split() {
        if (leftChild != null || rightChild != null) {
            return false; // 이미 쪼개졌음
        }

        // 분할 방향 결정 (true: 가로 분할, false: 세로 분할)
        boolean splitH = rand.nextBoolean();
        if (rect.w > rect.h && (double)rect.w / rect.h >= 1.25) splitH = false;
        else if (rect.h > rect.w && (double)rect.h / rect.w >= 1.25) splitH = true;

        int max = (splitH ? rect.h : rect.w) - MIN_LEAF_SIZE;
        if (max < MIN_LEAF_SIZE) {
            return false; // 쪼개기에 너무 작음 (리프 노드가 됨)
        }

        // (수정된 부분) nextInt(0) 오류를 막기 위해 + 1 추가
        int splitPos = rand.nextInt(max - MIN_LEAF_SIZE + 1) + MIN_LEAF_SIZE;

        if (splitH) { // 가로 분할 (위, 아래)
            leftChild = new BSPNode(new Rect(rect.x, rect.y, rect.w, splitPos));
            rightChild = new BSPNode(new Rect(rect.x, rect.y + splitPos, rect.w, rect.h - splitPos));
        } else { // 세로 분할 (왼쪽, 오른쪽)
            leftChild = new BSPNode(new Rect(rect.x, rect.y, splitPos, rect.h));
            rightChild = new BSPNode(new Rect(rect.x + splitPos, rect.y, rect.w - splitPos, rect.h));
        }

        // 자식들도 재귀적으로 쪼갬
        leftChild.split();
        rightChild.split();
        return true;
    }

    /**
     * [2단계: 방 생성]
     * 쪼개진 리프 노드에 실제 방을 만듭니다.
     */
    public void createRoom(DungeonGenerator generator) {
        if (leftChild != null || rightChild != null) {
            // 리프 노드가 아니면 자식들에게 위임

            // (수정된 부분) 재귀 호출 시 's'가 빠진 createRoom 호출
            if (leftChild != null) leftChild.createRoom(generator);
            if (rightChild != null) rightChild.createRoom(generator);
        } else {
            // 리프 노드이면, 여기에 방을 생성
            // (방 크기 랜덤 설정 시 0 또는 음수가 나오지 않도록 + 1)
            int w = rand.nextInt(rect.w - ROOM_MARGIN * 2 - ROOM_MIN_SIZE + 1) + ROOM_MIN_SIZE;
            int h = rand.nextInt(rect.h - ROOM_MARGIN * 2 - ROOM_MIN_SIZE + 1) + ROOM_MIN_SIZE;
            int x = rand.nextInt(rect.w - w - ROOM_MARGIN * 2 + 1) + rect.x + ROOM_MARGIN;
            int y = rand.nextInt(rect.h - h - ROOM_MARGIN * 2 + 1) + rect.y + ROOM_MARGIN;

            this.room = new Rect(x, y, w, h);
            generator.createRoom(this.room); 
        }
    }

    /**
     * [3단계: 복도 연결]
     * 자신의 두 자식(leftChild, rightChild)을 찾아 복도로 연결합니다.
     */
    public void createCorridors(DungeonGenerator generator) {
        // 리프 노드는 복도를 만들 필요 없음
        if (leftChild == null || rightChild == null) {
            return;
        }

        // 1. 자식 노드들이 먼저 (재귀적으로) 내부 복도를 만들도록 함
        leftChild.createCorridors(generator);
        rightChild.createCorridors(generator);

        // 2. 두 자식 노드에서 임의의 방을 하나씩 가져옴
        Rect room1 = leftChild.getRoom();
        Rect room2 = rightChild.getRoom();

        // 3. 두 방의 중심점을 L자 복도로 연결하도록 Generator에게 요청
        if (room1 != null && room2 != null) {
            generator.connectRooms(room1, room2);
        }
    }

    /**
     * [헬퍼 메서드]
     * 자신 또는 자신의 자식 노드(재귀)가 가진 '방(room)'을 찾아 반환합니다.
     */
    public Rect getRoom() {
        if (this.room != null) {
            // 1. 내가 리프 노드이고 방을 가지고 있으면, 내 방을 반환
            return this.room;
        } else {
            // 2. 내가 리프가 아니면, 자식 노드들에게 방을 찾아오라고 요청
            Rect leftRoom = null;
            Rect rightRoom = null;

            if (leftChild != null) {
                leftRoom = leftChild.getRoom();
            }
            if (rightChild != null) {
                rightRoom = rightChild.getRoom();
            }

            // 3. 자식들이 찾아온 방을 조합해서 반환
            if (leftRoom == null && rightRoom == null) {
                return null; // 자식들에게 방이 없음
            } else if (rightRoom == null) {
                return leftRoom; // 왼쪽 방만 있음
            } else if (leftRoom == null) {
                return rightRoom; // 오른쪽 방만 있음
            } else {
                // 둘 다 방이 있으면, 50% 확률로 둘 중 하나 반환
                return (rand.nextBoolean()) ? leftRoom : rightRoom;
            }
        }
    }
}
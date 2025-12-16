package com.game_adventure.generator;

import java.util.Random;

// BSP 트리의 노드
public class BSPNode {
    public Rect rect; // 이 노드가 담당하는 구역
    public BSPNode leftChild; // 왼쪽 자식 노드
    public BSPNode rightChild; // 오른쪽 자식 노드

    private Rect room; // 이 노드가 포함하는 방 (리프 노드일 때만 의미 있음)

    private static final int MIN_LEAF_SIZE = 6; // 구역의 최소 크기
    private static final int ROOM_MIN_SIZE = 4; // 방의 최소 크기
    private static final int ROOM_MARGIN = 1; // 방과 구역 경계 사이의 여유 공간

    private static Random rand = new Random(); // 난수 생성기

    public BSPNode(Rect rect) {
        this.rect = rect;
    }

    /**
     * [1단계: 구역 분할]
     * 이 노드의 구역(rect)을 재귀적으로 쪼갬
     */
    public boolean split() {
        if (leftChild != null || rightChild != null) { // 이미 쪼개졌으면
            return false; // 더 이상 쪼개지 않음
        }

        boolean splitH = rand.nextBoolean(); // 가로/세로 분할 여부 랜덤 결정

        if (rect.w > rect.h && (double)rect.w / rect.h >= 1.25) splitH = false; // 너비가 훨씬 크면 세로 분할, 상수로 비율 조정 가능
        else if (rect.h > rect.w && (double)rect.h / rect.w >= 1.25) splitH = true; // 높이가 훨씬 크면 가로 분할, 상수로 비율 조정 가능

        int max = (splitH ? rect.h : rect.w) - MIN_LEAF_SIZE; // 쪼갤 수 있는 최대 위치 계산
        if (max < MIN_LEAF_SIZE) { // 너무 작아서 쪼갤 수 없으면
            return false; // 쪼개지 않음
        }

        int splitPos = rand.nextInt(max - MIN_LEAF_SIZE + 1) + MIN_LEAF_SIZE; // 쪼갤 위치 랜덤 결정, 최소 크기 보장

        if (splitH) { // 가로 분할 (위, 아래)
            leftChild = new BSPNode(new Rect(rect.x, rect.y, rect.w, splitPos)); // 위쪽 구역
            rightChild = new BSPNode(new Rect(rect.x, rect.y + splitPos, rect.w, rect.h - splitPos)); // 아래쪽 구역
        } else { // 세로 분할 (왼쪽, 오른쪽)
            leftChild = new BSPNode(new Rect(rect.x, rect.y, splitPos, rect.h)); // 왼쪽 구역
            rightChild = new BSPNode(new Rect(rect.x + splitPos, rect.y, rect.w - splitPos, rect.h)); // 오른쪽 구역
        }

        // 자식들도 재귀적으로 쪼갬
        leftChild.split();
        rightChild.split();
        return true; // 쪼개기 성공
    }

    /**
     * [2단계: 방 생성]
     * 쪼개진 리프 노드에 실제 방을 만듬
     */
    public void createRoom(DungeonGenerator generator) {
        if (leftChild != null || rightChild != null) { // 리프 노드가 아니면 자식들에게 위임

            if (leftChild != null) leftChild.createRoom(generator); // 왼쪽 자식에게 방 생성 요청
            if (rightChild != null) rightChild.createRoom(generator); // 오른쪽 자식에게 방 생성 요청
        } 
        else { // 리프 노드이면, 여기에 방을 생성
            // (방 크기 랜덤 설정 시 0 또는 음수가 나오지 않도록 + 1)
            int w = rand.nextInt(rect.w - ROOM_MARGIN * 2 - ROOM_MIN_SIZE + 1) + ROOM_MIN_SIZE; // 방 너비
            int h = rand.nextInt(rect.h - ROOM_MARGIN * 2 - ROOM_MIN_SIZE + 1) + ROOM_MIN_SIZE; // 방 높이
            int x = rand.nextInt(rect.w - w - ROOM_MARGIN * 2 + 1) + rect.x + ROOM_MARGIN; // 방 좌상단 x 좌표
            int y = rand.nextInt(rect.h - h - ROOM_MARGIN * 2 + 1) + rect.y + ROOM_MARGIN; // 방 좌상단 y 좌표
            this.room = new Rect(x, y, w, h); // 방 생성
            generator.createRoom(this.room); // generator 클래스의 createRoom 메서드 호출(현재 클래스의 메서드 아님, 이름만 동일), 타일맵에 방을 파내는 메서드
        }
    }

    /**
     * [3단계: 복도 연결]
     * 자신의 두 자식(leftChild, rightChild)을 찾아 복도로 연결
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
     * 자신 또는 자신의 자식 노드가 가진 방을 찾아 반환
     */
    public Rect getRoom() {
        if (this.room != null) {
            // 1. 내가 리프 노드이고 방을 가지고 있으면, 내 방을 반환
            return this.room;
        } else {
            // 2. 내가 리프가 아니면, 자식 노드들에게 방을 찾아오라고 요청
            Rect leftRoom = null;
            Rect rightRoom = null;

            if (leftChild != null) { // 왼쪽 자식이 있으면
                leftRoom = leftChild.getRoom(); // 왼쪽 자식에게 방을 찾아오라고 요청
            }
            if (rightChild != null) { // 오른쪽 자식이 있으면
                rightRoom = rightChild.getRoom(); // 오른쪽 자식에게 방을 찾아오라고 요청
            }

            // 3. 자식들이 찾아온 방을 조합해서 반환
            if (leftRoom == null && rightRoom == null) { // 둘 다 방이 없으면
                return null; // null 반환
            } 
            else if (rightRoom == null) {
                return leftRoom; // 왼쪽 방만 있음
            } 
            else if (leftRoom == null) {
                return rightRoom; // 오른쪽 방만 있음
            } 
            else {
                return (rand.nextBoolean()) ? leftRoom : rightRoom; // 둘 다 방이 있으면, 50% 확률로 둘 중 하나 반환
            }
        }
    }
}
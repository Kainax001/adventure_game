# [Project Name: Java Native Adventure]

> **No Engine, Just Code.**
> 상용 게임 엔진(Unity, Unreal) 없이 Java 표준 라이브러리만으로 구축한 2D 어드벤처 게임
> *Custom 2D Game Engine Implementation using Java AWT/Swing*

## 📖 Project Overview
이 프로젝트는 게임 엔진에 의존하지 않고 **컴퓨터 그래픽스와 게임 루프의 원리를 바닥부터(Scratch) 구현**하기 위해 시작되었습니다.
Java의 기본 GUI 라이브러리인 AWT/Swing을 활용하여 렌더링 파이프라인, 스레드 기반의 게임 루프, 절차적 맵 생성(PCG) 알고리즘을 직접 설계했습니다.

* **개발 기간:** 2025.11 ~ 2025.12 (약 2개월)
* **개발 인원:** 1인 (개인 프로젝트)
* **주요 특징:** Zero-Dependency, Custom Game Loop, Procedural Map Generation

## 🛠 Tech Stack
* **Language:** Java 25 (Oracle OpenJDK)
* **Library:** Java AWT, Java Swing (Window Toolkit)
* **IDE:** [VSC / Eclipse]
* **VCS:** Git / GitHub

## ⚙️ Key Technical Features (핵심 구현 기술)

### 1. Custom Game Loop & Threading
게임의 `Update`(로직 연산)와 `Render`(화면 출력)를 제어하는 메인 루프를 직접 구현했습니다.
* `Runnable` 인터페이스와 `Thread`를 활용하여 독립적인 게임 실행 흐름을 제어했습니다.
* 시스템 성능에 따른 게임 속도 차이를 방지하기 위해 **FPS(초당 프레임) 제한 로직**과 **Delta Time** 개념을 적용하여 안정적인 구동 환경을 구축했습니다.

### 2. Swing Rendering Architecture (Double Buffering)
Java Swing의 `RepaintManager`가 제공하는 **내장 더블 버퍼링(Double Buffering)** 메커니즘을 적극 활용하여 렌더링 효율을 높였습니다.
* **Override `paintComponent`:** AWT의 `paint()` 메서드 대신 `paintComponent()`를 재정의하여, Swing의 오프스크린 버퍼(Off-screen Buffer)에 그래픽 요소를 렌더링함으로써 화면 깜빡임(Flickering)을 원천 차단했습니다.
* **Passive Rendering:** `GamePanel.repaint()` 호출을 통해 Swing의 이벤트 디스패치 스레드(EDT)가 렌더링 시점을 제어하도록 위임하여 스레드 안전성(Thread-Safety)을 확보했습니다.

### 3. Procedural Dungeon Generation (BSP Algorithm)
매번 새로운 맵을 제공하기 위해 **BSP(Binary Space Partitioning, 이진 공간 분할)** 알고리즘을 적용한 절차적 콘텐츠 생성(PCG) 시스템을 구축했습니다.

* **Recursive Partitioning (재귀적 분할):**
    * 맵의 전체 영역(`Rect`)을 `BSPNode` 트리 구조로 관리하며 재귀적으로 가로/세로 분할했습니다.
    * 너무 좁은 구역이 생기지 않도록 너비/높이 비율(Aspect Ratio) 제한 로직을 적용하여 맵의 효용성을 높였습니다.
* **Room & Corridor Connection:**
    * 분할된 트리의 **리프 노드(Leaf Node)**에만 랜덤한 크기의 방을 생성하여 방 간의 겹침 현상을 방지했습니다.
    * 형제 노드(Sibling Nodes)의 중심점을 잇는 **L자형 복도** 생성 로직을 통해 모든 방이 끊김 없이 연결되도록 구현했습니다.

### 4. Enemy AI & Pathfinding (BFS Algorithm)
적(Enemy)이 플레이어를 감지하고 최단 거리로 추적하는 로직을 **Grid 기반의 BFS(너비 우선 탐색)** 알고리즘으로 구현했습니다.

* **Array-based Map Processing:**
    * 맵 데이터를 `int[][]` 2차원 배열로 구조화했습니다. 복잡한 노드 객체 대신 **배열 인덱싱(Indexing)**을 사용하여 지형 정보와 장애물을 O(1) 시간 복잡도로 조회합니다.
* **Why BFS over A*? (Engineering Decision):**
    * 적의 최단 경로 추적을 보장하기 위해 탐색 알고리즘이 필요했습니다.
    * 개발 당시 맵의 크기(Scale)와 적 개체 수를 고려했을 때, 휴리스틱 함수 연산이 필요한 A* 알고리즘보다 **구현 복잡도가 낮고 메모리 관리가 직관적인 BFS**가 더 효율적이라고 판단했습니다.

### 5. OOP Design Pattern
* **Entity Component:** `Entity` 추상 클래스를 통해 플레이어, 적, NPC의 공통 속성(좌표, 히트박스, 상태)을 관리하여 코드 재사용성을 높였습니다.
* **State Management:** 캐릭터의 상태(Idle, Run, Attack)를 관리하여 애니메이션과 로직을 분리했습니다.

## 🐛 Troubleshooting

> **Issue 1: 충돌 처리 후 지속적인 충돌 판정 오류 (Sticky Collision Bug)**

* **문제 상황:** 충돌 후 밀림(Push-out) 로직이 실행되어 객체 간 거리가 멀어졌음에도 불구하고, 여전히 충돌 상태(IsColliding)로 판정되어 캐릭터가 진동하거나 끼이는 현상 발생.
* **원인 분석:** 충돌 해소(Resolution) 로직이 실행된 직후, 다음 프레임의 충돌 감지 로직이 실행되기 전까지 **충돌 플래그(Flag)가 초기화되지 않는 타이밍 이슈** 확인.
* **해결 방안:**
    * 충돌 반작용(Reaction) 계산이 끝난 직후, 강제로 충돌 상태를 재검증하고 플래그를 엄격하게(Strictly) 리셋하는 로직 추가.
    * AABB 판정 범위를 픽셀 단위로 미세 조정하여 '충돌 해소' 상태를 명확히 정의함.
* **결과:** 캐릭터 끼임 현상 없이 부드러운 물리 반작용 구현 성공.

> **Issue 2: 화면 깜빡임 및 스레드 동기화 문제**

* **문제 상황:** 단일 스레드 렌더링 시 화면 갱신 주기가 맞지 않아 발생하는 깜빡임(Flickering)과 티어링 현상.
* **원인 분석:** AWT의 직접 그리기(Direct Drawing) 방식이 고속 렌더링 시 버퍼 갱신 속도를 따라가지 못하는 한계 확인.
* **해결 방안:**
    1.  `JPanel`의 `paintComponent`를 오버라이딩하여 Swing 프레임워크의 **내장 더블 버퍼링** 기능을 활성화.
    2.  게임 루프 스레드에서 `repaint()`를 호출하여, 완성된 프레임만 화면에 노출되도록 렌더링 파이프라인 최적화.
* **결과:** 안정적인 30 FPS 확보 및 시각적 결함 해결.

## 📚 References
본 프로젝트는 외부 게임 엔진 없이 Java 표준 API 문서를 기반으로 정석적인 방식으로 설계되었습니다.

* **Official Java Documentation (Oracle)**
    * [Java SE 25 Specification](https://docs.oracle.com/en/java/javase/25/docs/api/index.html)
    * [java.awt.Graphics (Rendering)](https://docs.oracle.com/en/java/javase/25/docs/api/java.desktop/java/awt/Graphics.html)
    * [java.lang.Thread (Concurrency)](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/Thread.html)

# [Project Name: SimCityMod]

> **Advanced Server Management & Dynamic Economy System**
> Minecraft Forge(v1.20.1) API를 기반으로 구축한 대규모 서버 관리 및 경제 시뮬레이션 시스템
> *Custom Minecraft Mod Implementation focused on Data Persistence and Optimization*

## 📖 Project Overview
이 프로젝트는 마인크래프트 서버 운영의 효율성과 경제 시스템의 재미를 극대화하기 위해 시작되었습니다. 
상용 엔진이 제공하는 API의 한계를 극복하고, 수만 개의 청크 데이터와 실시간 거래 통계를 성능 저하 없이 관리하기 위해 **독립적인 데이터 영속성 레이어**와 **이벤트 기반 아키텍처**를 직접 설계했습니다.

* **개발 기간:** 2025.12 ~ 2026.01 (약 2개월)
* **개발 인원:** 1인 (개인 프로젝트)
* **주요 특징:** High Performance Data Management, Custom Admin System, Dynamic Event Engine

## 🛠 Tech Stack
* **Language:** Java 17 (Oracle OpenJDK)
* **Library:** Minecraft Forge API (v1.20.1), GSON (JSON Library)
* **IDE:** VS Code
* **VCS:** Git / GitHub

## ⚙️ Key Technical Features (핵심 구현 기술)

### 1. Robust Data Persistence (JSON-based Flat File DB)
엔진 내부 데이터 구조에 의존하지 않고 독립적인 데이터 관리 시스템을 구축했습니다.
* **GSON Integration:** `GSON`을 활용하여 플레이어 정보 및 청크 점유 현황을 JSON 형식으로 관리하는 **독립적인 데이터 매니저**를 구현했습니다.
* **Event-Driven Save:** `ServerStoppingEvent` 및 `LevelEvent.Save` 구독을 통해 서버 가동 중에도 데이터 유실을 최소화하는 자동 저장(Auto-save) 메커니즘을 구축했습니다.

### 2. High Performance Chunk Management
다차원 서버 환경에서 효율적인 영역 보호를 위해 데이터 구조를 최적화했습니다.
* **O(1) 검색 최적화:** 수만 개의 점유 데이터를 처리하기 위해 `HashMap<ChunkPos, PlayerInfo>` 구조를 채택하여 즉각적인 권한 판정이 가능하도록 설계했습니다.
* **복합 키 구조:** 차원과 좌표를 결합한 데이터 구조를 통해 다차원 서버 환경에서도 유일한 구역 소유권을 보장합니다.

### 3. Dynamic Wave & NPC AI System
서버의 긴장감을 유지하기 위한 자동화된 이벤트 엔진을 설계했습니다.
* **Custom Wave Manager:** 특정 주기에 따라 난이도별 몬스터 웨이브를 발생시키는 스케줄러를 설계하고, `Heightmap`을 활용하여 지형에 적응하는 스폰 알고리즘을 적용했습니다.
* **State Machine NPCs:** 추상 클래스 상속 구조를 통해 다양한 직업군 상인 NPC를 구현했으며, `TradeMonitorManager`를 통해 실시간 거래 통계를 수집하여 경제 밸런싱의 기초로 활용합니다.

### 4. Reactive Event Modification
직접적인 코드 변조(Hooking)가 제한된 환경에서 시스템 밸런스를 조절하기 위한 전략을 수립했습니다.
* **Event Interception:** 인챈트 생성 로직 자체를 수정하는 대신, 아이템 장착 및 사용 시점의 이벤트를 감지하는 리스너를 구축했습니다.
* **On-the-fly Correction:** 조건에 부합하지 않는 오버 밸런스 속성이 감지될 경우 실시간으로 데이터를 재계산하고 보정하는 **Reactive Modification** 로직을 구현했습니다.

## 🐛 Troubleshooting

> **Issue 1: 보안 강화로 인한 데이터 후킹 차단 문제**

* **문제 상황:** 최신 버전 포지 엔진에서 데이터 후킹이 차단되어, 청크 데이터를 게임 내부 NBT에 저장하거나 지형 생성 시드를 변조하는 기존 방식이 불가능해짐.
* **해결 방안:** * 엔진에 의존하지 않고 독립적으로 동작하는 **JSON 기반 데이터 영속성 레이어**를 구축하여 데이터 저장의 유연성 확보.
    * 시드 변조 대신, 기존 차원에서 미사용 구역을 탐색하여 논리적으로 야생 지형을 할당하는 **좌표 기반 지형 초기화 방식**으로 우회.
* **결과:** 엔진의 제약과 상관없이 안정적인 데이터 저장 및 월드 관리 성공.

> **Issue 2: 대량 데이터 프리로드 시 메모리 고갈 및 서버 프리징**

* **문제 상황:** 서버 시작 시 청크 및 플레이어 데이터를 미리 불러오는 프리로드 기능을 실행할 때, 과도한 RAM 점유와 함께 서버가 중단(Hang)되는 현상 발생.
* **원인 분석:** 로딩 프로세스가 메인 스레드의 모든 Tick 연산 자원을 독점하여, **JVM의 가비지 컬렉션 및 메모리 정리가 수행될 여유 틱이 확보되지 않음**을 확인.
* **해결 방안:**
    * **Time-Slicing 적용:** 데이터를 한 번에 로드하지 않고, 일정 간격마다 로딩 프로세스를 일시 중지하는 **휴식 시간**을 강제로 삽입.
    * 메인 틱 사이에 메모리 정리와 타 시스템 연산이 수행될 수 있도록 로딩 스케줄러를 최적화.
* **결과:** 메모리 누수 없이 안정적인 데이터 프리로드 구현 및 서버 기동 시간 단축 성공.

> **Issue 3: 차원 간 좌표 중복으로 인한 소유권 충돌**

* **문제 상황:** 청크 점유 데이터를 `ChunkPos(x, z)` 좌표로만 관리하여, 오버월드와 네더 등 서로 다른 차원에서 좌표가 겹치면 소유권이 중복 적용되는 논리적 오류 발생.
* **해결 방안:**
    * 데이터 유닛을 **[Dimension ID + Chunk Coordinates] 조합의 복합 객체**로 리팩토링하여 각 차원별 독립적인 권한 판정 보장.
* **결과:** 다차원 멀티플레이 환경에서도 정교한 구역 보호 시스템 구현 성공.

> **Issue 4: 시스템 코어 수정 불가에 따른 밸런싱 난항**

* **문제 상황:** 인챈트 테이블 생성 알고리즘 후킹이 막혀 있어 서버 정책에 맞는 인챈트 확률 조정이 불가능함.
* **원인 분석:** 엔진 코어 로직의 불변성으로 인해 생성 시점의 개입이 차단됨.
* **해결 방안:**
    * 아이템이 생성되거나 장착되는 **이벤트를 감지**하여, 조건에 부합하지 않는 인챈트 레벨을 실시간으로 강제 재계산 및 수정하는 방식 적용.
* **결과:** 엔진 수정 없이도 서버 운영자가 의도한 경제 및 전투 밸런스 강제 성공.

## 📚 References
본 프로젝트는 마인크래프트 포지 프레임워크와 Java 표준 라이브러리를 활용하여 설계되었습니다.

* **Official Minecraft Forge Documentation**
* **GSON Library Specification**
* **Java Persistence Best Practices (Flat File DB)**

---

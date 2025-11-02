package com.game_adventure.core; // 패키지 (core)

import com.game_adventure.map.Dungeon;
import com.game_adventure.entity.Player;
import com.game_adventure.generator.DungeonGenerator;

import javax.swing.JFrame; // 윈도우
import java.awt.event.KeyEvent; // 키보드 이벤트
import java.awt.event.KeyListener; // 키보드 리스너

// [수정 1] Runnable 인터페이스를 "구현" 목록에 추가
public class Game implements KeyListener, Runnable {

    private Dungeon dungeon;
    private Player player;
    private GamePanel gamePanel;
    private JFrame frame;

    // [추가 2] 게임 루프 스레드 및 FPS 변수
    private Thread gameThread;
    private final int FPS = 30; // 목표 프레임
    // [추가 1] 게임 상태 변수
    private boolean isAwaitingQuitConfirmation = false;

    public Game() {
        // 1. 맵 생성 (동일)
        DungeonGenerator generator = new DungeonGenerator();
        this.dungeon = generator.generate(50, 30); 
        this.player = this.dungeon.getPlayer();

        // 2. 그래픽 컴포넌트 생성 (동일)
        this.gamePanel = new GamePanel(dungeon); 

        // 3. 윈도우(JFrame) 설정 (동일)
        this.frame = new JFrame("The Adventure"); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.setResizable(false); 
        frame.add(gamePanel); 
        frame.pack(); 
        frame.addKeyListener(this); 
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true); 

        // [추가 3] 윈도우가 뜬 후, 게임 루프(스레드) 시작
        startGame();
    }

    /**
     * [추가 4] 게임 스레드를 시작하는 메서드
     */
    public void startGame() {
        gameThread = new Thread(this); // this (Game 객체)를 스레드로 만듦
        gameThread.start(); // 스레드가 run() 메서드를 실행하도록 함
    }

    /**
     * [수정 5] run 메서드에 30 FPS 게임 루프 구현
     */
    @Override
    public void run() {

        // 1초(1000ms) / 30 FPS = 33.33ms (1프레임당 목표 시간)
        double targetFrameTime = 1000.0 / FPS; 

        while (gameThread != null) {
            long startTime = System.currentTimeMillis(); // 루프 시작 시간

            // 1. 로직 업데이트 (현재는 키 입력으로만 처리하므로 비워둠)
            // updateLogic(); 

            // 2. 화면 갱신 (키 입력과 상관없이 1/30초마다 강제 갱신)
            gamePanel.repaint();

            // 3. FPS 제어 (시간 계산)
            long timeTaken = System.currentTimeMillis() - startTime; // 로직+그리기 소요 시간
            long sleepTime = (long) (targetFrameTime - timeTaken); // 목표 시간까지 남은 시간

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime); // 남은 시간만큼 스레드 정지
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // --- [KeyListener 인터페이스 메서드 구현] ---

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode(); 

        // [분기 1] "종료 확인 대기 중"일 때 (Y 또는 N 키만 받음)
        if (isAwaitingQuitConfirmation) {
            if (keyCode == KeyEvent.VK_Y) {
                System.exit(0); // 'Y' 누르면 종료
            } else if (keyCode == KeyEvent.VK_N) {
                isAwaitingQuitConfirmation = false; // 'N' 누르면
                gamePanel.setShowQuitMessage(false); // GamePanel에 경고창 숨김 요청
            }
            return; // (중요!) Y/N이 아니면 다른 키(WASD) 무시
        }

        // [분기 2] "일반 플레이 중"일 때 (WASD, P 키만 받음)
        switch (keyCode) {
            case KeyEvent.VK_W: player.move(0, -1, dungeon); break;
            case KeyEvent.VK_A: player.move(-1, 0, dungeon); break;
            case KeyEvent.VK_S: player.move(0, 1, dungeon); break;
            case KeyEvent.VK_D: player.move(1, 0, dungeon); break;
            case KeyEvent.VK_Q: // (Q키)
                isAwaitingQuitConfirmation = true; // 상태 변경
                gamePanel.setShowQuitMessage(true); // GamePanel에 경고창 표시 요청
                break;
        }
        // (repaint()는 run() 메서드가 하므로 여기서 호출 안 함)
    }

    @Override
    public void keyTyped(KeyEvent e) { 
        // (사용 안 함)
    }

    @Override
    public void keyReleased(KeyEvent e) { 
        // (사용 안 함)
    }
}
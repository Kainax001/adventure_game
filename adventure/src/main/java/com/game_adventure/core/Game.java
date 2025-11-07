package com.game_adventure.core;

import com.game_adventure.map.Dungeon;
import com.game_adventure.entity.Player;
import com.game_adventure.generator.DungeonGenerator;

import javax.swing.JFrame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game implements KeyListener, Runnable {

    private Dungeon dungeon;
    private Player player;
    private GamePanel gamePanel;
    private JFrame frame;
    private DungeonGenerator generator;

    // 맵 크기 상수
    private final int MAP_WIDTH = 50;
    private final int MAP_HEIGHT = 30;

    private Thread gameThread;
    private final int FPS = 30;

    public boolean isAwaitingQuitConfirmation = false;
    private boolean isAwaitingLevelTransition = false; 

    public Game() {
        this.generator = new DungeonGenerator();
        
        // 1. 맵 생성 및 초기화
        this.dungeon = this.generator.generate(MAP_WIDTH, MAP_HEIGHT); 
        this.player = this.dungeon.getPlayer();

        // 2. 그래픽 컴포넌트 생성
        this.gamePanel = new GamePanel(dungeon); 

        // 3. 윈도우(JFrame) 설정
        this.frame = new JFrame("The Adventure");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.setResizable(false); 
        frame.add(gamePanel); 
        frame.pack(); 
        frame.addKeyListener(this); 
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true); 

        // 4. 게임 루프(스레드) 시작
        startGame();
    }

    /**
     * 새 레벨을 생성하고 게임 상태를 초기화하는 메서드
     */
    private void generateNewLevel() {
        System.out.println("새로운 던전 생성 중...");
        
        // 새 맵을 그리기 전에 level 전환 메시지 숨기기
        gamePanel.setShowWinMessage(false); 
        
        // 1. 새로운 던전 생성
        Dungeon newDungeon = generator.generate(MAP_WIDTH, MAP_HEIGHT); 
        
        // 2. 새 던전에서 생성된 Player의 위치 정보를 가져옴
        Player newLevelPlayer = newDungeon.getPlayer();
        int newStartX = newLevelPlayer.getX();
        int newStartY = newLevelPlayer.getY();
        
        // 기존 Player 객체의 위치만 업데이트
        this.player.setPosition(newStartX, newStartY);
        
        // 3. 필드 업데이트: Player 객체는 그대로 두고 Dungeon 객체만 새 것으로
        this.dungeon = newDungeon; 
        
        // 4. GamePanel 업데이트 및 프레임 크기 재조정
        //    GamePanel은 setDungeon을 통해 새 던전을 받지만,
        //    던전 내부의 Player 객체는 Game.java의 Player와는 다릅니다.

        // Dungeon 객체 내부의 Player 객체도 Game.java의 객체와 동일하게 설정
        // Dungeon 클래스에 Player 객체를 교체하는 Setter가 필요
        this.dungeon.setPlayer(this.player);
        
        // GamePanel 업데이트 및 프레임 크기 재조정
        gamePanel.setDungeon(newDungeon); 
        frame.pack();
        frame.setLocationRelativeTo(null);
        
        System.out.println("새로운 던전이 생성되었습니다!");
    }

    public void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double targetFrameTime = 1000.0 / FPS; 

        while (gameThread != null) {
            long startTime = System.currentTimeMillis();

            gamePanel.repaint();

            long timeTaken = System.currentTimeMillis() - startTime;
            long sleepTime = (long) (targetFrameTime - timeTaken);

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
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
                System.exit(0);
            } else if (keyCode == KeyEvent.VK_N) {
                isAwaitingQuitConfirmation = false;
                gamePanel.setShowQuitMessage(false);
                gamePanel.repaint(); 
            }
            return;
        }

        // [분기 2] "레벨 전환 대기 중"일 때 (Y 또는 N 키만 허용) 
        if (isAwaitingLevelTransition) {
            if (keyCode == KeyEvent.VK_Y) {
                isAwaitingLevelTransition = false;
                gamePanel.setShowWinMessage(false);
                generateNewLevel(); // Y 누르면 새 레벨 생성 (내부에서 갱신됨)
            } else if (keyCode == KeyEvent.VK_N) {
                isAwaitingLevelTransition = false;
                gamePanel.setShowWinMessage(false);
                gamePanel.repaint(); // N 누르면 메시지 숨기고 현재 레벨 유지
            }
            return;
        }
        
        // [분기 3] "일반 플레이 중"일 때 (WASD, Q 키만 받음)
        int oldX = player.getX();
        int oldY = player.getY();
        
        switch (keyCode) {
            case KeyEvent.VK_W: player.move(0, -1, dungeon); break;
            case KeyEvent.VK_A: player.move(-1, 0, dungeon); break;
            case KeyEvent.VK_S: player.move(0, 1, dungeon); break;
            case KeyEvent.VK_D: player.move(1, 0, dungeon); break;
            case KeyEvent.VK_Q:
                isAwaitingQuitConfirmation = true; 
                gamePanel.setShowQuitMessage(true);
                gamePanel.repaint(); // Q를 눌러 메시지 표시 시작
                break;
        }

        // [추가] 이동 후, 출구 타일 위에 있는지 확인하고 대기 상태로 전환
        if ((oldX != player.getX() || oldY != player.getY())) {
            
            if (gamePanel.isPlayerAtExit()) { 
                 isAwaitingLevelTransition = true;
                 gamePanel.setShowWinMessage(true);
                 gamePanel.repaint(); // 메시지 띄울 때 갱신
            }
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}
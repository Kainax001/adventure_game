package com.game_adventure.core;

import com.game_adventure.map.Dungeon;
import com.game_adventure.entity.Enemy;
import com.game_adventure.entity.Player;
import com.game_adventure.generator.DungeonGenerator;

import javax.swing.JFrame;

public class Game implements Runnable {

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

    // isAwaitingQuitConfirmation Setter
    public void setIsAwaitingQuitConfirmation(boolean state) {
        this.isAwaitingQuitConfirmation = state;
    }
    // isAwaitingLevelTransition Getter/Setter
    public boolean isAwaitingLevelTransition() {
        return isAwaitingLevelTransition;
    }
    public void setIsAwaitingLevelTransition(boolean state) {
        this.isAwaitingLevelTransition = state;
    }

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
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);

        InputHandler inputHandler = new InputHandler(this, this.player, this.dungeon, this.gamePanel);
        frame.addKeyListener(inputHandler);

        // 4. 게임 루프(스레드) 시작
        startGame();
    }

    // **[추가]** 1. 상태/액션 키 처리 로직 (InputHandler로부터 위임)
    public void handleActionKey(ActionKey action) {
        if (isAwaitingQuitConfirmation) {
            if (action == ActionKey.YES) { System.exit(0); } 
            else if (action == ActionKey.NO) { 
                setIsAwaitingQuitConfirmation(false);
                gamePanel.setShowQuitMessage(false);
                gamePanel.repaint(); 
            }
            return;
        }

        if (isAwaitingLevelTransition()) {
            if (action == ActionKey.YES) {
                setIsAwaitingLevelTransition(false);
                gamePanel.setShowWinMessage(false);
                generateNewLevel();
            } else if (action == ActionKey.NO) {
                setIsAwaitingLevelTransition(false);
                gamePanel.setShowWinMessage(false);
                gamePanel.repaint();
            }
            return;
        }
        
        // 일반 플레이 중 Q 키 처리
        if (action == ActionKey.QUIT) {
            setIsAwaitingQuitConfirmation(true);
            gamePanel.setShowQuitMessage(true);
            gamePanel.repaint();
        }
    }

    private void checkAndHandleLevelTransition(int oldX, int oldY) {
        // 플레이어의 위치가 실제로 바뀌었는지 확인
        if (player.getX() != oldX || player.getY() != oldY) {
            // 출구 타일 위에 있는지 확인
            if (gamePanel.isPlayerAtExit()) { 
                setIsAwaitingLevelTransition(true);
                gamePanel.setShowWinMessage(true);
                gamePanel.repaint();
            }
        }
    }


    public void handleMovement(int dx, int dy) {
        if (isAwaitingQuitConfirmation || isAwaitingLevelTransition()) {
            return;
        }

        int oldX = player.getX();
        int oldY = player.getY();
        
        player.move(dx, dy, dungeon); // 플레이어 이동

        checkAndHandleLevelTransition(oldX, oldY);
    }


    public void handleDashMovement(int dx, int dy) {
        if (isAwaitingQuitConfirmation || isAwaitingLevelTransition()) {
            return;
        }

        int oldX = player.getX();
        int oldY = player.getY();

        player.dash(dx, dy, dungeon); // 플레이어 대시 실행

        checkAndHandleLevelTransition(oldX, oldY);
    }

    /**
     * 새 레벨을 생성하고 게임 상태를 초기화하는 메서드
     */
    private void generateNewLevel() {
        System.out.println("Generating new level...");
        
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

        // Dungeon 객체 내부의 Player 객체도 Game.java의 객체와 동일하게 설정
        // Dungeon 클래스에 Player 객체를 교체하는 Setter가 필요
        this.dungeon.setPlayer(this.player);
        
        // GamePanel 업데이트 및 프레임 크기 재조정
        gamePanel.setDungeon(newDungeon); 
        frame.pack();
        frame.setLocationRelativeTo(null);
        
        System.out.println("New level generated.");
    }

    public void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void updateLogic() {
        
        // 적 업데이트
        if (dungeon.getEnemies() != null) {             // 적이 존재할 경우에만
            for (Enemy enemy : dungeon.getEnemies()) {  // 적의 수만큼
                enemy.update(dungeon, player);          // 적 업데이트
            }
        }
            
        player.update(); // 플레이어 업데이트
    }
    
    @Override
    public void run() {
        double targetFrameTime = 1000.0 / FPS; 

        while (gameThread != null) {
            long startTime = System.currentTimeMillis();

            // 게임 로직 업데이트
            // **[핵심 추가]** 게임 로직 업데이트 호출
            // 레벨 전환/종료 대기 중이 아닐 때만 로직을 실행합니다.
            if (!isAwaitingQuitConfirmation && !isAwaitingLevelTransition) {
                 updateLogic(); 
            }

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
}
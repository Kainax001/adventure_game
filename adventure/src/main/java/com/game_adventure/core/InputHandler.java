package com.game_adventure.core;

import com.game_adventure.map.Dungeon;
import com.game_adventure.entity.Player;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

    // InputHandler는 오직 Game 객체에 명령을 내립니다.
    private final Game game;

    public InputHandler(Game game, Player player, Dungeon dungeon, GamePanel gamePanel) {
        this.game = game;
        // player, dungeon, gamePanel은 Game 객체가 관리해야 하므로 여기서는 불필요
        // 다만, 현재 Game 생성자가 이들을 요구하므로, 필드에서 제거하는 것은 나중에 고려
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode(); 
        boolean isShiftDown = e.isShiftDown(); // Shift 키 상태 확인

        // 모든 상태 판정과 실행을 Game 클래스에 위임
        
        // 1. 상태 전환 키 (Y, N, Q) 처리
        if (keyCode == KeyEvent.VK_Y) {
            this.game.handleActionKey(ActionKey.YES);
            return;
        } else if (keyCode == KeyEvent.VK_N) {
            this.game.handleActionKey(ActionKey.NO);
            return;
        } else if (keyCode == KeyEvent.VK_Q) {
            this.game.handleActionKey(ActionKey.QUIT);
            return;
        }

        // 2. 이동 키 (WASD) 처리
        int dx = 0;
        int dy = 0;
        
        switch (keyCode) {
            case KeyEvent.VK_W: dy = -1; break;
            case KeyEvent.VK_A: dx = -1; break;
            case KeyEvent.VK_S: dy = 1; break;
            case KeyEvent.VK_D: dx = 1; break;
        }

        if (dx != 0 || dy != 0) {
            if (isShiftDown) {
            // **[핵심]** Shift가 눌렸다면 DASH 명령을 Game에 전달
            this.game.handleDashMovement(dx, dy); // Game에 새로운 메서드 필요
        } else {
            // Shift가 눌리지 않았다면 일반 이동
            this.game.handleMovement(dx, dy); 
        } 
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}
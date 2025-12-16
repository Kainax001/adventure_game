package com.game_adventure.core;

import com.game_adventure.map.Dungeon;
import com.game_adventure.entity.Player;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// InputHandler는 오직 Game 객체에 명령을 내리는 역할만 담당
public class InputHandler implements KeyListener {

    private final Game game; // Game 객체 참조

    private boolean up, down, left, right, shift; // 이동 및 대시 상태 플래그

    public InputHandler(Game game, Player player, Dungeon dungeon, GamePanel gamePanel) { // player, dungeon, gamePanel은 현재 필요 없음
        this.game = game;
        // player, dungeon, gamePanel은 Game 객체가 관리해야 하므로 여기서는 불필요
        // 다만 현재 Game 생성자가 이들을 요구하므로, 필드에서 제거하는 것은 나중에 고려
    }

    public void update() { // 매 프레임마다 호출되어 현재 키 상태에 따라 이동 명령 전달
        int dx = 0; // 이동 방향 x
        int dy = 0; // 이동 방향 y

        // 저장된 키 상태에 따라 이동 방향 결정
        if (up)    dy = -1;
        if (down)  dy = 1;
        if (left)  dx = -1;
        if (right) dx = 1;

        // 이동 입력이 있을 때만 게임에 명령 전달
        if (dx != 0 || dy != 0) {
            if (shift) {
                // Shift 누른 상태면 대시
                this.game.handleDashMovement(dx, dy);
            } else {
                // 아니면 일반 이동 (Player 내부의 쿨다운 덕분에 너무 빠르지 않게 이동됨)
                this.game.handleMovement(dx, dy);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode(); 

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
        
        // 2. 이동 키, 쉬프트: Flag를 true로 변경
        switch (keyCode) {
            case KeyEvent.VK_W: up = true; break;
            case KeyEvent.VK_A: left = true; break;
            case KeyEvent.VK_S: down = true; break;
            case KeyEvent.VK_D: right = true; break;
            case KeyEvent.VK_SHIFT: shift = true; break;
        }
        
        
    }

    @Override public void keyTyped(KeyEvent e) {} // 사용하지 않음

    @Override public void keyReleased(KeyEvent e) { // 키 해제 이벤트 처리
        int keyCode = e.getKeyCode();

        // 키를 떼면 Flag를 false로 변경
        switch (keyCode) {
            case KeyEvent.VK_W: up = false; break;
            case KeyEvent.VK_A: left = false; break;
            case KeyEvent.VK_S: down = false; break;
            case KeyEvent.VK_D: right = false; break;
            case KeyEvent.VK_SHIFT: shift = false; break;
        }
    }
}
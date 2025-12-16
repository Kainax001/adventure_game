package com.game_adventure; 

import com.game_adventure.core.Game; 

public class Main {
    public static void main(String[] args) {
        // Swing 윈도우는 메인 스레드에서 실행, 이벤트 디스패치 스레드 사용
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Game();
            }
        });
    }
}
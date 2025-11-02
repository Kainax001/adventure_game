package com.game_adventure; 

// (수정) core 패키지에서 Game 클래스를 가져옴
import com.game_adventure.core.Game; 

public class Main {
    public static void main(String[] args) {
        // Swing 윈도우는 별도 스레드에서 실행하는 것이 권장됨
        // (이렇게 하면 Swing GUI가 더 안정적으로 작동합니다)
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Game();
                // game.run()은 이제 호출할 필요 없음
            }
        });
    }
}
package com.game_adventure;

// import com.game_adventure.core.Game; // Game을 여기서 직접 부르지 않으므로 제거 가능
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Swing 윈도우는 메인 스레드에서 실행, 이벤트 디스패치 스레드 사용
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Game 대신 MainMenu를 먼저 실행
                new MainMenu();
            }
        });
    }
}
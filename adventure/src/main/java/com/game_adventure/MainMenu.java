package com.game_adventure;

import com.game_adventure.core.Game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Adventure Game Title");
        setSize(800, 600); // 메뉴 화면 크기
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 화면 중앙 배치
        setLayout(new GridBagLayout()); // 버튼을 중앙에 두기 위한 레이아웃

        // 게임 시작 버튼 생성
        JButton startButton = new JButton("GAME START");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setPreferredSize(new Dimension(400, 160));

        // 버튼 클릭 이벤트
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame(); // 게임 시작 메소드 호출
            }
        });

        add(startButton);
        setVisible(true); // 메뉴 창 보이기
    }

    private void startGame() {
        // 1. 현재 메인 메뉴 창을 닫음 (자원 해제)
        this.dispose(); 
        // 2. 게임 클래스의 인스턴스를 생성하여 게임 시작
        new Game(); 
    }
}
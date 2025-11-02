package com.game_adventure.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:adventure_data.db"; // DB 파일

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // 게임 시작 시 "데이터 시트"를 생성하고 SQL로 입력
    public static void initializeDatabase() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS entity_templates ("
                              + " type_name TEXT PRIMARY KEY NOT NULL,"
                              + " base_hp INTEGER NOT NULL,"
                              + " base_atk INTEGER NOT NULL,"
                              + " color_r INTEGER NOT NULL,"
                              + " color_g INTEGER NOT NULL,"
                              + " color_b INTEGER NOT NULL"
                              + ");";
        
        // try-with-resources (자동으로 conn, stmt 종료)
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(createTableSql);
            
            // 데이터 시트 입력 (중복 방지를 위해 INSERT OR IGNORE)
            stmt.execute("INSERT OR IGNORE INTO entity_templates VALUES ('goblin', 10, 3, 34, 139, 34);"); // 초록색
            stmt.execute("INSERT OR IGNORE INTO entity_templates VALUES ('slime', 5, 1, 100, 149, 237);"); // 파란색
            stmt.execute("INSERT OR IGNORE INTO entity_templates VALUES ('orc', 25, 8, 220, 20, 60);"); // 붉은색
            
        } catch (SQLException e) {
            System.err.println("DB 초기화 실패: " + e.getMessage());
        }
    }
}
package com.game_adventure.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.awt.Color; // Color를 여기서 import

// "데이터 시트" 자체를 표현하는 도우미 클래스 (DTO)
class EntityTemplate {
    String typeName;
    int hp;
    int atk;
    Color color;

    public EntityTemplate(String name, int hp, int atk, Color color) {
        this.typeName = name; this.hp = hp; this.atk = atk; this.color = color;
    }
}

// Data Access Object (DAO)
public class EntityTemplateDAO {

    // DB에서 읽어온 스탯 정보를 메모리에 캐시(저장)
    private Map<String, EntityTemplate> templateCache = new HashMap<>();

    // 게임 시작 시 DB의 모든 템플릿을 읽어서 캐시에 저장
    public void loadAllTemplates() {
        String sql = "SELECT * FROM entity_templates";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("type_name");
                int hp = rs.getInt("base_hp");
                int atk = rs.getInt("base_atk");
                Color color = new Color(
                    rs.getInt("color_r"), 
                    rs.getInt("color_g"), 
                    rs.getInt("color_b")
                );
                
                templateCache.put(name, new EntityTemplate(name, hp, atk, color));
            }
            System.out.println(templateCache.size() + "개의 엔티티 템플릿 로드 완료.");

        } catch (SQLException e) {
            System.err.println("템플릿 로드 실패: " + e.getMessage());
        }
    }

    // 캐시에서 템플릿(스탯 정보) 가져오기
    public EntityTemplate getTemplate(String typeName) {
        return templateCache.get(typeName);
    }
}
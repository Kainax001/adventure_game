package com.game_adventure.entity;

import com.game_adventure.map.Dungeon;
import java.awt.Color; // import
import java.awt.Graphics; // import

public class Enemy extends Entity{
    public Enemy (int x, int y){
        super(x, y);
    }

    public void move(int dx, int dy, Dungeon dungeon) {
        int newX = this.x + dx;
        int newY = this.y + dy;

        if (dungeon.isWalkable(newX, newY)) {
            this.x = newX;
            this.y = newY;
        }
    }

    public void update(Dungeon dungeon, Player player) {
        // 간단한 추적 AI: 플레이어 방향으로 한 칸 이동
        int dx = Integer.compare(player.getX(), this.x);
        int dy = Integer.compare(player.getY(), this.y);
        move(dx, dy, dungeon);
    }

    @Override
    public void draw(Graphics g, int tileSize) {
        g.setColor(Color.RED); 
        g.fillOval(x * tileSize, y * tileSize, tileSize, tileSize);
    }
}

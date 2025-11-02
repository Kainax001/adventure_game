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

    @Override
    public void draw(Graphics g, int tileSize) {
        g.setColor(Color.RED); 
        g.fillOval(x * tileSize, y * tileSize, tileSize, tileSize);
    }
}

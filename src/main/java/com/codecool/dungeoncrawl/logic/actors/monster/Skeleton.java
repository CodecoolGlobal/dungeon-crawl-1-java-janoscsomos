package com.codecool.dungeoncrawl.logic.actors.monster;

import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.map.Cell;
import com.codecool.dungeoncrawl.logic.util.Direction;

public class Skeleton extends Monster {
    public Skeleton(Cell cell) {
        super(cell);
        super.setAttack(3);
        super.setDefense(1);
        super.setHealth(6);
    }

    @Override
    public void monsterMove(Cell playerCell) {
        moveInX(playerCell);
        moveInY(playerCell);
    }

    private void moveInX(Cell playerCell) {
        int playerX = playerCell.getX();
        if (getX() > playerX) {
            move(Direction.WEST.getX(), Direction.WEST.getY());
        } else if (getX() < playerX) {
            move(Direction.EAST.getX(), Direction.EAST.getY());
        }
    }

    private void moveInY(Cell playerCell) {
        int playerY = playerCell.getY();
        if (getY() > playerY) {
            move(Direction.NORTH.getX(), Direction.NORTH.getY());
        } else if (getY() < playerY) {
            move(Direction.SOUTH.getX(), Direction.SOUTH.getY());
        }
    }

    @Override
    public String getTileName() {
        return "skeleton";
    }
}

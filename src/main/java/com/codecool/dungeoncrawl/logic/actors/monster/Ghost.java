package com.codecool.dungeoncrawl.logic.actors.monster;

import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.map.Cell;
import com.codecool.dungeoncrawl.logic.util.Direction;

public class Ghost extends Monster {

    public Ghost(Cell cell) {
        super(cell);
    }

    private void moveInX(Cell playerCell) {
        int playerX = playerCell.getX();
        if (getX() < playerX) {
            move(Direction.WEST.getX(), Direction.WEST.getY());
        } else if (getX() > playerX) {
            move(Direction.EAST.getX(), Direction.EAST.getY());
        }
    }

    private void moveInY(Cell playerCell) {
        int playerY = playerCell.getY();
        if (getY() < playerY) {
            move(Direction.NORTH.getX(), Direction.NORTH.getY());
        } else if (getY() > playerY) {
            move(Direction.SOUTH.getX(), Direction.SOUTH.getY());
        }
    }

    @Override
    public void monsterMove(Cell playerCell) {
        moveInX(playerCell);
        moveInY(playerCell);
    }

    @Override
    public void move(int dx, int dy) {
        Cell nextCell = super.getCell().getNeighbor(dx, dy);
        if (gameConditions.checkNextCellGhost(nextCell)) {
            super.getCell().setActor(null);
            nextCell.setActor(this);
            super.setCell(nextCell);
        }
    }

    @Override
    public String getTileName() {
        return "ghost";
    }
}

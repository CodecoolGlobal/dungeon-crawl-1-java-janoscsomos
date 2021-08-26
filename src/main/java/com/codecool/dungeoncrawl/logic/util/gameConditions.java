package com.codecool.dungeoncrawl.logic.util;

import com.codecool.dungeoncrawl.logic.map.Cell;
import com.codecool.dungeoncrawl.logic.map.CellType;
import com.codecool.dungeoncrawl.logic.map.GameMap;

public class gameConditions {

    public boolean checkFakeDoor(int playerX, int playerY, GameMap map) {
        return map.getCell(playerX, playerY)
                .getType() == CellType.FAKE_DOOR;
    }

    public boolean checkDoorInDirection(int playerX, int playerY, Direction direction, GameMap map) {
        return map.getCell(playerX + direction.getX(), playerY + direction.getY())
                .getType() == CellType.CLOSED_DOOR;
    }

    public boolean checkOpenDoor(int playerX, int playerY, GameMap map) {
        return map.getCell(playerX, playerY)
                .getType() == CellType.OPEN_DOOR;
    }


    public boolean checkNextCell(Cell nextCell) {
        return (nextCell.getType().equals(CellType.FLOOR) ||
                nextCell.getType().equals(CellType.OPEN_DOOR)) &&
                !isCellOccupied(nextCell);
    }

    public boolean isItemOnPlayerPosition(int playerX, int playerY, GameMap map) {
        return map.getCell(playerX, playerY).getItem() != null;
    }

    public boolean doorNextToPlayer(int playerX, int playerY, GameMap map) {
        boolean doorToTheLeft = checkDoorInDirection(playerX, playerY, Direction.NORTH, map);
        boolean doorToTheRight = checkDoorInDirection(playerX, playerY, Direction.SOUTH, map);
        boolean doorBelow = checkDoorInDirection(playerX, playerY, Direction.EAST, map);
        boolean doorAbove = checkDoorInDirection(playerX, playerY, Direction.WEST, map);
        return doorToTheLeft || doorToTheRight || doorBelow || doorAbove;
    }

    public boolean isCellOccupied(Cell cell) {
        return cell.getActor() != null;
    }

    public boolean isDead(int actorHealth) {
        return actorHealth <= 0;
    }
}

package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.map.Cell;
import com.codecool.dungeoncrawl.logic.map.Drawable;
import com.codecool.dungeoncrawl.logic.util.Direction;
import com.codecool.dungeoncrawl.logic.util.GameConditions;

public abstract class Actor implements Drawable {

    private Cell cell;
    private int health = 10;
    private int attack = 3;
    private int defense = 0;

    protected GameConditions gameConditions = new GameConditions();

    public Actor(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
    }

    public int getHealth() {
        return health;
    }

    public Cell getCell() {
        return cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }



    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        if (gameConditions.checkNextCell(nextCell)) {
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
        }
    }


}

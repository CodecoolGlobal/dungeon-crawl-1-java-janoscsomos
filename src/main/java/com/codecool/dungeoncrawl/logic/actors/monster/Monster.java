package com.codecool.dungeoncrawl.logic.actors.monster;

import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.map.Cell;

public abstract class Monster extends Actor {
    public Monster(Cell cell) {
        super(cell);
    }
    public abstract void monsterMove(Cell playerCell);
}

package com.codecool.dungeoncrawl.logic.actors.monster;

import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.map.Cell;
import com.codecool.dungeoncrawl.logic.util.Direction;

public class Orc extends Monster {

    Direction direction = Direction.NORTH;

    public Orc(Cell cell) {
        super(cell);
        super.setAttack(0);
        super.setHealth(4);
        super.setDefense(4);
    }

    @Override
    public void monsterMove(Cell playerCell) {
        if (direction == Direction.NORTH){
            direction = Direction.WEST;
        }
        else if(direction == Direction.WEST){
            direction = Direction.SOUTH;
        }
        else if (direction == Direction.SOUTH){
            direction = Direction.EAST;
        }
        else if (direction == Direction.EAST){
            direction = Direction.NORTH;
        }
        int newX = direction.getX();
        int newY = direction.getY();
        move(newX,newY);
    }

    @Override
    public String getTileName() {
        return "orc";
    }
}

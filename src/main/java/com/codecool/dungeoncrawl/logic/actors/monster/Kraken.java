package com.codecool.dungeoncrawl.logic.actors.monster;

import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.map.Cell;
import com.codecool.dungeoncrawl.logic.map.CellType;
import com.codecool.dungeoncrawl.logic.map.GameMap;
import com.codecool.dungeoncrawl.logic.util.Util;

import java.util.ArrayList;

public class Kraken extends Monster {
    private final GameMap map;
    ArrayList<Cell> waterCells = new ArrayList<>();
    public Kraken(Cell cell, GameMap map) {
        super(cell);
        super.setAttack(0);
        super.setHealth(0);
        this.map = map;
        waterFieldSearcher();
    }

    @Override
    public void move(int dx, int dy) {
        Cell nextCell = map.getCell(dx,dy);
        if (gameConditions.checkNextCellKraken(nextCell)) {
            super.getCell().setActor(null);
            nextCell.setActor(this);
            super.setCell(nextCell);
        }
    }

    @Override
    public void monsterMove(Cell playerCell) {
        int randomIndex = Util.getRandomNumber(0,waterCells.size()-1);
        Cell randomCell = waterCells.get(randomIndex);
        move(randomCell.getX(),randomCell.getY());
    }

    @Override
    public String getTileName() {
        return "kraken";
    }

    private void waterFieldSearcher(){
        for (Cell [] cells : map.getCells()){
            for(Cell cell : cells){
                if (cell.getType().equals(CellType.WATER)){
                    waterCells.add(cell);
                }
            }
        }
    }
}

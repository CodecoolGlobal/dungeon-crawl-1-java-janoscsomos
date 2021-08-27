package com.codecool.dungeoncrawl.logic.map;

import com.codecool.dungeoncrawl.logic.actors.*;
import com.codecool.dungeoncrawl.logic.actors.monster.*;
import com.codecool.dungeoncrawl.logic.util.Direction;
import com.codecool.dungeoncrawl.logic.util.GameConditions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameMap {

    private final int width;
    private final int height;
    public GameConditions gameConditions;
    private final Cell[][] cells;
    private final List<Monster> monsters = new LinkedList<>();
    private final List<Actor> skeletons = new LinkedList<>();
    private final List<Actor> orcs = new ArrayList<>();
    private final List<Actor> undeads = new ArrayList<>();
    private final List<Actor> krakens = new ArrayList<>();
    private final List<Actor> ghosts = new ArrayList<>();
    private Player player;
    private CellType exit;

    public GameMap(int width, int height, CellType defaultCellType) {
        this.width = width;
        this.height = height;
        this.gameConditions = new GameConditions();
        this.exit = CellType.CLOSED_DOOR;
        cells = defineCells(width, height, defaultCellType);
    }

    public List<Actor> getKraken() {
        return krakens;
    }

    public List<Actor> getGhosts() {
        return ghosts;
    }

    public Player getPlayer() {
        return player;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Actor> getSkeletons() {
        return skeletons;
    }

    public List<Actor> getOrcs() {
        return orcs;
    }

    public List<Actor> getUndeads() {
        return undeads;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public CellType getExit() {
        return exit;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void addKraken(Kraken kraken) {
        this.monsters.add(kraken);
    }

    public void addGhost(Ghost ghost) {
        this.monsters.add(ghost);
    }

    public void addSkeleton(Skeleton skeleton) {
        this.monsters.add(skeleton);
    }

    public void addOrc(Orc orc) {
        this.monsters.add(orc);
    }

    public void addUndead(Undead undead) {
        this.monsters.add(undead);
    }

    public void removeMonster(Monster monster) {
        this.monsters.remove(monster);
    }

    private Cell[][] defineCells(int width, int height, CellType defaultCellType) {
        final Cell[][] cells;
        cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(this, x, y, defaultCellType);
            }
        }
        return cells;
    }

    public void openDoor() {
        this.exit = CellType.OPEN_DOOR;
        for (Cell[] value : cells) {
            for (Cell cell : value) {
                if (cell.getType() == CellType.CLOSED_DOOR)
                    cell.setType(CellType.OPEN_DOOR);
            }
        }
    }

    private void monsterInteractions() {
        for (var monster :monsters) {
            if (gameConditions.isDead(monster.getHealth()))
                this.removeMonster(monster);
            else
                monster.monsterMove(this.getCell(direction.getX(), direction.getY()));
        }
    }

    public void moveAcotrs(Direction direction) {
        player.move(Direction.NORTH.getX(), Direction.NORTH.getY());
        monsterInteractions();
    }
}

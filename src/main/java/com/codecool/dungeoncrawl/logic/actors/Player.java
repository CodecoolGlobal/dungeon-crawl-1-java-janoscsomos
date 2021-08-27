package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.map.Cell;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.ItemType;

import java.util.HashMap;
import java.util.Map;

public class Player extends Actor {
    private final Map<Item, Integer> inventory = new HashMap<>();
    private boolean isDrunk = false;

    public Player(Cell cell) {
        super(cell);
    }

    public String getTileName() {
        return "player";
    }

    public Map<Item, Integer> getInventory() {
        return inventory;
    }

    public boolean isDrunk() {
        return isDrunk;
    }

    public void setDrunk(boolean drunk) {
        isDrunk = drunk;
    }

    public void addToInventory(Item item, Integer quantity) {
        inventory.put(item, quantity);
    }

    public void removeFromInventory(Item item) {
        inventory.remove(item);
    }

    public boolean hasItem(ItemType itemType) {
        for (Item item: inventory.keySet()) {
            if (item.getItemType().equals(itemType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void move(int dx, int dy) {
        boolean boatChecker = hasItem(ItemType.BOAT);
        Cell nextCell = super.getCell().getNeighbor(dx, dy);
        if (gameConditions.checkNextCellPlayer(nextCell, boatChecker)) {
            super.getCell().setActor(null);
            nextCell.setActor(this);
            super.setCell(nextCell);
        }
    }

}

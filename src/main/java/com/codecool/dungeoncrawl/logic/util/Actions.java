package com.codecool.dungeoncrawl.logic.util;

import com.codecool.dungeoncrawl.logic.actors.*;
import com.codecool.dungeoncrawl.logic.actors.monster.*;
import com.codecool.dungeoncrawl.logic.items.ItemActions;
import com.codecool.dungeoncrawl.logic.items.ItemType;
import com.codecool.dungeoncrawl.logic.map.Cell;
import com.codecool.dungeoncrawl.logic.map.GameMap;
import com.codecool.dungeoncrawl.logic.items.Item;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Map;

public class Actions {

    GameConditions gameConditions = new GameConditions();
    ItemActions itemActions = new ItemActions();

/*######################################################################################################################
* Inventory method:
*#####################################################################################################################*/

    public void pickUpItem(GameMap map) {
        int playerX = map.getPlayer().getX();
        int playerY = map.getPlayer().getY();
        if (gameConditions.isItemOnPlayerPosition(playerX, playerY, map)) {
            Item item = map.getCell(playerX, playerY).getItem();
            int addedQuantity = 1;
            boolean inInventory = false;
            Map<Item, Integer> playerInventory = map.getPlayer().getInventory();
            for (Item itemInLoop: playerInventory.keySet()) {
                if (itemInLoop.getName().equals(item.getName())) {
                    item = itemInLoop;
                    inInventory = true;
                }
            }
            if (!inInventory) {
                map.getPlayer().addToInventory(item, addedQuantity);
                if (gameConditions.checkIfArmor(item)) {
                    itemActions.equipArmor(map, item.getName());
                }
                if (gameConditions.checkIfWeapon(item)) {
                    itemActions.equipWeapon(map, item.getName());
                }
            } else {
                map.getPlayer().addToInventory(item, playerInventory.get(item) + addedQuantity);
            }
            map.getCell(playerX, playerY).setItem(null);
        }
    }


/*######################################################################################################################
* Player methods:
*#####################################################################################################################*/

    // Wrapper for player methods:
    public void movePlayer(int moveInRow, int moveInColumn, GameMap map, Label actionLabel) {
        map.getPlayer().move(moveInRow, moveInColumn);
        lookForDoor(map);
        checkNearbyMonsters(map.getPlayer(), actionLabel);
    }

    // Search for door on neighboring cells and open them if player has the key for it:
    private void lookForDoor(GameMap map) {
        int playerX = map.getPlayer().getCell().getX();
        int playerY = map.getPlayer().getCell().getY();
        if (gameConditions.doorNextToPlayer(playerX, playerY, map) && map.getPlayer().hasItem(ItemType.KEY))
            map.openDoor();
    }

    // Check neighboring fields for monsters:
    private void checkNearbyMonsters(Actor player, Label actionLabel) {
        Cell cell = player.getCell();
        checkForEnemies(player, cell, Direction.WEST, actionLabel);
        checkForEnemies(player, cell, Direction.EAST, actionLabel);
        checkForEnemies(player, cell, Direction.NORTH, actionLabel);
        checkForEnemies(player, cell, Direction.SOUTH, actionLabel);
    }

    // Initiate fight if finding a monster near by:
    private void checkForEnemies(Actor player, Cell playerCell, Direction currentDirection, Label actionLabel) {
        Cell nearbyCell = playerCell.getNeighbor(currentDirection.getX(), currentDirection.getY());
        if (gameConditions.isCellOccupied(nearbyCell))
            fight(nearbyCell, player, actionLabel);
    }

/*######################################################################################################################
* Fighting:
*#####################################################################################################################*/

    private void fight(Cell nearbyCell, Actor player, Label actionLabel) {
        actionLabel.setText("");
        Actor enemy = nearbyCell.getActor();
        while (true) {
            int playerHealth = 100;
            // Player's turn:
            if (playerTurn(nearbyCell, player, actionLabel, enemy, playerHealth)) break;
            // Enemy turn:
            enemyTurn(player, actionLabel, enemy);
        }
    }

    private void enemyTurn(Actor player, Label actionLabel, Actor enemy) {
        int playerHealth;
        playerHealth = hit(actionLabel, enemy, player, StringFactory.ENEMY_HIT.message);
        if (gameConditions.isDead(playerHealth))
            die();
    }

    private boolean playerTurn(Cell nearbyCell, Actor player, Label actionLabel, Actor enemy, int playerHealth) {
        int enemyHealth = hit(actionLabel, player, enemy, StringFactory.HIT_ENEMY.message);
        if (gameConditions.isDead(enemyHealth)) {
            killEnemy(nearbyCell, player, actionLabel, playerHealth, enemy);
            return true;
        }
        return false;
    }

    // Decrease and the defender's health according to the attack value of the attacking Actor:
    private int hit(Label actionLabel, Actor attacker, Actor defender, String message) {
        int attackerHit = Util.getAttackerHit(attacker, defender);
        defender.setHealth(defender.getHealth() - attackerHit);
        actionLabel.setText(actionLabel.getText() + message + attackerHit + StringFactory.DAMAGE.message);
        return defender.getHealth();
    }

    // If player's health 0 or less:
    private void die() {
        Util.exitGame();
    }

    // If enemy's health 0 or less:
    private void killEnemy(Cell nearbyCell, Actor player, Label actionLabel, int playerHealth, Actor enemy) {
        if (enemy instanceof Ghost){
            Util.exitGame();
        }
        nearbyCell.setActor(null);
        actionLabel.setText(actionLabel.getText() + StringFactory.KILL_ENEMY.message);
        player.setHealth(playerHealth);
        enemy.setHealth(enemy.getHealth());
    }
}

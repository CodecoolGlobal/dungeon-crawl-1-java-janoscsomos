package com.codecool.dungeoncrawl.logic.util;

import com.codecool.dungeoncrawl.logic.items.ItemType;
import com.codecool.dungeoncrawl.logic.map.Cell;
import com.codecool.dungeoncrawl.logic.map.GameMap;
import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Orc;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.actors.Undead;
import com.codecool.dungeoncrawl.logic.items.Item;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Map;

public class Actions {

    gameConditions gameConditions = new gameConditions();
    Util util = new Util();

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
            } else {
                map.getPlayer().addToInventory(item, playerInventory.get(item) + addedQuantity);
            }
            map.getCell(playerX, playerY).setItem(null);
        }
    }

/*######################################################################################################################
* Monster methods:
*#####################################################################################################################*/

    // Wrapper for smaller methods handling monsters:
    public void monsterInteractions(GameMap map) {
        removeDeadMonsters(map);
        moveMonsters(map.getSkeletons(), map.getPlayer().getCell());
        moveMonsters(map.getOrcs(), map.getPlayer().getCell());
    }


    // Wrapper of finding dead monsters:
    private void removeDeadMonsters(GameMap map) {
        removeBodies(map.getSkeletons(), map);
        removeBodies(map.getOrcs(), map);
        removeBodies(map.getUndeads(), map);
    }

    // Finding dead monsters:
    private void removeBodies(ArrayList<Actor> monsters, GameMap map) {
        for (int index = 0; index < monsters.size(); index++) {
            if (gameConditions.isDead(monsters.get(index).getHealth()))
                removeBody(monsters.get(index), map, index);
        }
    }

    // Dump a dead monster from it's collection:
    private void removeBody(Actor monster, GameMap map, int index) {
        if (monster instanceof Skeleton) map.removeSkeleton(index);
        else if (monster instanceof Orc) map.removeOrc(index);
        else if (monster instanceof Undead) map.removeUndead(index);
    }

    // Iterating through a collection of monsters and activate it's move method:
    private void moveMonsters(ArrayList<Actor> monstersToMove, Cell playerCell) {
        for (Actor monster : monstersToMove) monster.monsterMove(playerCell);
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

    private boolean playerTurn(
            Cell nearbyCell,
            Actor player,
            Label actionLabel,
            Actor enemy,
            int playerHealth
    ) {
        int enemyHealth = hit(actionLabel, player, enemy, StringFactory.HIT_ENEMY.message);
        if (gameConditions.isDead(enemyHealth)) {
            killEnemy(nearbyCell, player, actionLabel, playerHealth, enemy);
            return true;
        }
        return false;
    }

    // Decrease and the defender's health according to the attack value of the attacking Actor:
    private int hit(Label actionLabel, Actor attacker, Actor defender, String message) {
        int attackerHit = util.getAttackerHit(attacker, defender);
        defender.setHealth(defender.getHealth() - attackerHit);
        actionLabel.setText(actionLabel.getText() + message + attackerHit + StringFactory.DAMAGE.message);
        return defender.getHealth();
    }

    // If player's health 0 or less:
    private void die() {
        util.exitGame();
    }

    // If enemy's health 0 or less:
    private void killEnemy(
            Cell nearbyCell,
            Actor player,
            Label actionLabel,
            int playerHealth,
            Actor enemy
    ) {
        nearbyCell.setActor(null);
        actionLabel.setText(actionLabel.getText() + StringFactory.KILL_ENEMY.message);
        player.setHealth(playerHealth);
        enemy.setHealth(enemy.getHealth());
    }
}

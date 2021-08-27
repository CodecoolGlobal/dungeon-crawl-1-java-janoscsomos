package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.items.ItemActions;
import com.codecool.dungeoncrawl.logic.items.ItemType;
import com.codecool.dungeoncrawl.logic.map.Tiles;
import com.codecool.dungeoncrawl.logic.map.*;
import com.codecool.dungeoncrawl.logic.util.*;
import com.codecool.dungeoncrawl.logic.map.Cell;
import com.codecool.dungeoncrawl.logic.map.GameMap;
import com.codecool.dungeoncrawl.logic.map.MapLoader;
import com.codecool.dungeoncrawl.logic.items.Item;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Game extends Application {

    int mapCounter = 1;

    Scene scene;

    GameMap map;
    Canvas canvas = new Canvas(
            NumberParameters.TILE_WIDTH_MULTIPLIER_V.getValue() * Tiles.TILE_WIDTH,
            NumberParameters.TILE_WIDTH_MULTIPLIER_V1.getValue() * Tiles.TILE_WIDTH
    );
    GraphicsContext context = canvas.getGraphicsContext2D();

    Actions actions = new Actions();
    GameConditions gameConditions = new GameConditions();
    boolean confirmQuit = false;

    Label healthLabel = new Label();
    Label defenseLabel = new Label();
    Label attackLabel = new Label();
    Label inventoryLabel = new Label();
    Label quitLabel = new Label();
    Label actionLabel = new Label();
    Label pickUpInfo = new Label();

    Pane lineBreak = new Pane();
    Pane lineBreak2 = new Pane();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane ui = new GridPane();
        setUpUi(ui);

        BorderPane borderPane = new BorderPane();
        setUpBorderPane(ui, borderPane);

        scene = new Scene(borderPane);
        setUpScene(primaryStage, scene, MapName.MAP1.getMapName(), null);
    }

    private void setUpBorderPane(GridPane ui, BorderPane borderPane) {
        borderPane.setCenter(canvas);
        borderPane.setRight(ui);
    }

    private void setUpScene(Stage primaryStage, Scene scene, String mapToLoad, GameMap previousMap) {
        primaryStage.setScene(scene);
        int[] coordinates = MapLoader.getPlayerPosition(mapToLoad);
        map = MapLoader.loadMap(coordinates[2], mapToLoad, previousMap);
        refresh(coordinates[1], coordinates[0]);
        scene.setOnKeyPressed(this::onKeyPressed);
        primaryStage.setTitle(StringFactory.TITLE.message);
        primaryStage.show();
    }

    private void setUpSecondScene(String mapToLoad, GameMap previousMap){
        int[] coordinates = MapLoader.getPlayerPosition(mapToLoad);
        map = MapLoader.loadMap(coordinates[2], mapToLoad, previousMap);
        refresh(coordinates[1], coordinates[0]);
    }

    private void setUpUi(GridPane ui) {
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));
        lineBreak.minHeightProperty().bind(inventoryLabel.heightProperty());
        lineBreak2.minHeightProperty().bind(inventoryLabel.heightProperty());
        setLabels(ui);
        pickUpInfo.setText(StringFactory.PICK_UP_ITEMS.message);
        pickUpInfo.setWrapText(true);
        quitLabel.setWrapText(true);
    }

    private void setLabels(GridPane ui) {
        ui.add(new Label(StringFactory.HEALTH_LABEL.message), 0, 0);
        ui.add(healthLabel, 1, 0);
        ui.add(new Label(StringFactory.DEFENSE_LABEL.message), 0, 1);
        ui.add(defenseLabel, 1, 1);
        ui.add(new Label(StringFactory.ATTACK_LABEL.message), 0, 2);
        ui.add(attackLabel, 1, 2);
        ui.add(new Label(StringFactory.ACTION_LABEL.message), 0, 3);
        ui.add(actionLabel, 1, 3);
        ui.add(new Label(StringFactory.INVENTORY_LABEL.message), 0, 4);
        ui.add(inventoryLabel, 1, 4);
        ui.add(quitLabel, 0, 8, 2, 1);
        ui.add(lineBreak, 0, 5);
        ui.add(pickUpInfo, 0, 6, 2, 1);
        ui.add(lineBreak2, 0, 7);
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        ItemActions itemActions = new ItemActions();
        switch (keyEvent.getCode()) {
            case UP:
                map.moveAcotrs(Direction.NORTH);

                enterTheDoor();
                refresh(map.getPlayer().getX(), map.getPlayer().getY());
                break;
            case DOWN:
                actions.movePlayer(Direction.SOUTH.getX(), Direction.SOUTH.getY(), map, actionLabel);
                actions.monsterInteractions(map);
                actions.moveMonsters(map.getGhosts(), map.getPlayer().getCell());
                enterTheDoor();
                refresh(map.getPlayer().getX(), map.getPlayer().getY());
                break;
            case LEFT:
                actions.movePlayer(Direction.WEST.getX(), Direction.WEST.getY(), map, actionLabel);
                actions.monsterInteractions(map);
                enterTheDoor();
                refresh(map.getPlayer().getX(), map.getPlayer().getY());
                break;
            case RIGHT:
                actions.movePlayer(Direction.EAST.getX(), Direction.EAST.getY(), map, actionLabel);
                actions.monsterInteractions(map);
                enterTheDoor();
                refresh(map.getPlayer().getX(), map.getPlayer().getY());
                break;
            case Q:
                quitLabel.setText(StringFactory.WANT_TO_QUIT.message);
                confirmQuit = true;
                break;
            case ENTER:
                actions.pickUpItem(map);
                break;
            case Y:
                if (confirmQuit) {
                    Util.exitGame();
                }
                break;
            case N:
                confirmQuit = false;
                quitLabel.setText("");
                break;
            case F:
                Item foodItem = itemActions.searchForItemByType(map, ItemType.FOOD);
                if (foodItem != null) {
                    itemActions.consumeFood(map, foodItem.getName());
                }
                break;
            case H:
                itemActions.consumePotion(map, StringFactory.HEALING_POTION.message);
                break;
            case G:
                itemActions.consumePotion(map, StringFactory.STONE_SKIN_POTION.message);
                break;
            case J:
                itemActions.consumePotion(map, StringFactory.MIGHT_POTION.message);
                break;
            case B:
                if (map.getPlayer().hasItem(ItemType.BOAT)) {
                    itemActions.leaveBoat(map.getPlayer());
                }
                break;
            case A:
                if (map.getPlayer().hasItem((ItemType.ALCOHOL))) {
                    itemActions.consumeAlcohol(map, StringFactory.BEER_CAP.message);
                }
                break;
        }
    }

    private void refresh(int x, int y) {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        int diffX = (int) (canvas.getWidth() / (NumberParameters.TILE_WIDTH_MULTIPLIER.getValue() * Tiles.TILE_WIDTH));
        int diffY = (int) (canvas.getHeight() / (NumberParameters.TILE_WIDTH_MULTIPLIER.getValue() * Tiles.TILE_WIDTH));
        drawingCells(x, y, diffX, diffY);
        refreshUi();
    }


    private void checkIfInventoryIsEmpty(Map<Item, Integer> playerInventory, String inventoryContents) {
        if (playerInventory.size() == 0) {
            inventoryLabel.setText(StringFactory.INVENTORY_EMPTY.message);
        } else {
            inventoryLabel.setText(inventoryContents);
        }
    }

    private String buildInventoryString(Map<Item, Integer> playerInventory) {
        List<String> itemsInInventory = new ArrayList<>();
        for (Item item : playerInventory.keySet()) {
            itemsInInventory.add(item.getName());
            itemsInInventory.add(playerInventory.get(item).toString() + "\n");
        }
        return String.join(" ", itemsInInventory);
    }

    private void drawingCells(int playerX, int playerY, int diffX, int diffY) {
        for (int x = 0; x < canvas.getWidth() && Math.max(playerX - diffX, 0) + x < map.getWidth(); x++) {
            for (int y = 0; y < canvas.getHeight() && Math.max(playerY - diffY, 0) + y < map.getHeight(); y++) {
                Cell cell = map.getCell(Math.max(playerX - diffX, 0) + x, Math.max(playerY - diffY, 0) + y);
                drawingTiles(x, y, cell);
            }
        }
    }

    private void drawingTiles(int x, int y, Cell cell) {
        if (gameConditions.isCellOccupied(cell)) {
            Tiles.drawTile(context, cell.getActor(), x, y);
        } else if (cell.getItem() != null) {
            Tiles.drawTile(context, cell.getItem(), x, y);
        } else {
            Tiles.drawTile(context, cell, x, y);
        }
    }

    private void refreshUi() {
        healthLabel.setText("" + map.getPlayer().getHealth());
        defenseLabel.setText("" + map.getPlayer().getDefense());
        attackLabel.setText("" + map.getPlayer().getAttack());
        Map<Item, Integer> playerInventory = map.getPlayer().getInventory();
        String inventoryContents = buildInventoryString(playerInventory);
        checkIfInventoryIsEmpty(playerInventory, inventoryContents);
    }

    private void enterTheDoor(){
        if (doorIsOpen()) {
            switch (mapCounter) {
                case 1:
                    map.getPlayer().setDrunk(false);
                    goToNextMap(MapName.MAP2);
                    mapCounter++;
                    break;
                case 2:
                    map.getPlayer().setDrunk(false);
                    goToNextMap(MapName.MAP3);
                    mapCounter++;
                    break;
                case 3:
                    map.getPlayer().setDrunk(false);
                    goToNextMap(MapName.MAP4);
                    mapCounter++;
                    break;
                case 4:
                    map.getPlayer().setDrunk(false);
                    goToNextMap(MapName.MAP5);
                    mapCounter++;
                    break;
            }
        }
        else if (doorIsFake()){
            goToNextMap(MapName.DEAD);
            }
        }

    private void goToNextMap(MapName mapName) {
        setUpSecondScene( mapName.getMapName(), map);
    }


    private boolean doorIsOpen(){
        return gameConditions.checkOpenDoor(map.getPlayer().getX(), map.getPlayer().getY(), map);
    }

    private boolean doorIsFake() {
        return gameConditions.checkFakeDoor(map.getPlayer().getX(), map.getPlayer().getY(), map);
    }

}

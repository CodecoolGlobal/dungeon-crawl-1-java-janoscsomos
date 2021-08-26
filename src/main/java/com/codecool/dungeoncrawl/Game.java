package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.items.ItemActions;
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
    Util util = new Util();
    gameConditions gameConditions = new gameConditions();
    boolean confirmQuit = false;

    Label healthLabel = new Label();
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
        setUpScene(primaryStage, scene, "/map1.txt", null);
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
        ui.add(new Label(StringFactory.ACTION_LABEL.message), 0, 1);
        ui.add(actionLabel, 1, 1);
        ui.add(new Label(StringFactory.INVENTORY_LABEL.message), 0, 2);
        ui.add(inventoryLabel, 1, 2);
        ui.add(quitLabel, 0, 6, 2, 1);
        ui.add(lineBreak, 0, 3);
        ui.add(pickUpInfo, 0, 4, 2, 1);
        ui.add(lineBreak2, 0, 5);
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        ItemActions itemActions = new ItemActions();
        switch (keyEvent.getCode()) {
            case UP:
                actions.movePlayer(Direction.NORTH.getX(), Direction.NORTH.getY(), map, actionLabel);
                actions.monsterInteractions(map);
                enterTheDoor();
                refresh(map.getPlayer().getX(), map.getPlayer().getY());
                break;
            case DOWN:
                actions.movePlayer(Direction.SOUTH.getX(), Direction.SOUTH.getY(), map, actionLabel);
                actions.monsterInteractions(map);
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
                    util.exitGame();
                }
                break;
            case N:
                confirmQuit = false;
                quitLabel.setText("");
                break;
            case F:
                itemActions.consumeFood(map, StringFactory.BREAD.message);
                break;
            case P:
                itemActions.consumePotion(map, StringFactory.POTION.message);
                break;
            case B:
                itemActions.boatActions(map);
                break;
        }
    }

    private void refresh(int playerX, int playerY) {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        int diffX = (int) (canvas.getWidth() / (NumberParameters.TILE_WIDTH_MULTIPLIER.getValue() * Tiles.TILE_WIDTH));
        int diffY = (int) (canvas.getHeight() / (NumberParameters.TILE_WIDTH_MULTIPLIER.getValue() * Tiles.TILE_WIDTH));
        drawingCells(playerX, playerY, diffX, diffY);
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
        Map<Item, Integer> playerInventory = map.getPlayer().getInventory();
        String inventoryContents = buildInventoryString(playerInventory);
        checkIfInventoryIsEmpty(playerInventory, inventoryContents);
    }

    private void enterTheDoor(){
        if (doorIsOpen()) {
            switch (mapCounter) {
                case 1:
                    goToNextMap(MapName.MAP2);
                    mapCounter++;
                    break;
                case 2:
                    goToNextMap(MapName.MAP3);
                    mapCounter++;
                    break;
                case 3:
                    goToNextMap(MapName.MAP4);
                    mapCounter++;
                    break;
                case 4:
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

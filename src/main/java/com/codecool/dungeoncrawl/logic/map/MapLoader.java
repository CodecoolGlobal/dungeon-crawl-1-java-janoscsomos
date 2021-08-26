package com.codecool.dungeoncrawl.logic.map;

import com.codecool.dungeoncrawl.logic.actors.*;
import com.codecool.dungeoncrawl.logic.items.*;

import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class MapLoader {

    public static int[] getPlayerPosition(String map) {
        InputStream inputStream = MapLoader.class.getResourceAsStream(map);
        Scanner scanner = new Scanner(inputStream);

        scanner.nextLine(); // empty line
        int[] result = new int[3];
        int y = -1;
        while (true) {
            y++;
            String line;
            try {
                line = scanner.nextLine();
            } catch (NoSuchElementException e) {
                break;
            }
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == '@') {
                    result[0] = y;
                    result[1] = x;
                }
            }
        }
        result[2] = y;
        return result;
    }

    public static GameMap loadMap(int height, String mapToLoad, GameMap previousMap) {
        InputStream inputStream = MapLoader.class.getResourceAsStream(mapToLoad);
        Scanner scanner = new Scanner(inputStream);

        String line = scanner.nextLine(); // empty line
        int width = line.length();

        GameMap map = new GameMap(width, height, CellType.EMPTY);
        int y = -1;
        while (true) {
            y++;
            try {
                line = scanner.nextLine();
            } catch (NoSuchElementException e) {
                break;
            }
            for (int x = 0; x < line.length(); x++) {
                if (x < line.length()) {
                    Cell cell = map.getCell(x, y);
                    switch (line.charAt(x)) {
                        case ' ':
                            cell.setType(CellType.EMPTY);
                            break;
                        case '#':
                            cell.setType(CellType.WALL);
                            break;
                        case '.':
                            cell.setType(CellType.FLOOR);
                            break;
                        case '-':
                            cell.setType(CellType.FLOOR2);
                            break;
                        case 's':
                            cell.setType(CellType.FLOOR);
                            map.addSkeleton(new Skeleton(cell));
                            break;
                        case '@':
                            cell.setType(CellType.FLOOR);
                            setPlayer(previousMap, map, cell);
                            break;
                        case 'k':
                            cell.setType(CellType.FLOOR);
                            new Key("Key", cell, ItemType.KEY);
                            break;
                        case 'a':
                            cell.setType(CellType.FLOOR);
                            new Armor("Shield", cell, ItemType.ARMOR, 10);
                            break;
                        case 'w':
                            cell.setType(CellType.FLOOR);
                            new Weapon("Sword", cell, ItemType.WEAPON, 10);
                            break;
                        case 'f':
                            cell.setType(CellType.FLOOR);
                            new Food("Bread", cell, ItemType.FOOD);
                            break;
                        case 'p':
                            cell.setType(CellType.FLOOR);
                            new Potion("Potion", cell, ItemType.POTION);
                            break;
                        case 'B':
                            cell.setType(CellType.WATER);
                            new Boat("Boat", cell, ItemType.BOAT);
                            break;
                        case 'd':
                            cell.setType(map.getExit());
                            break;
                        case 'o':
                            cell.setType(CellType.FLOOR);
                            map.addOrc(new Orc(cell));
                            break;
                        case 'u':
                            cell.setType(CellType.FLOOR);
                            map.addUndead(new Undead(cell));
                            break;
                        case '/':
                            cell.setType(CellType.BRICK_WALL);
                            break;
                        case '~':
                            cell.setType(CellType.WATER);
                            break;
                        case '_':
                            cell.setType(CellType.RIVER);
                            break;
                        case '^':
                            cell.setType(CellType.HOUSE);
                            break;
                        case '«':
                            cell.setType(CellType.RAMP_START);
                            break;
                        case '|':
                            cell.setType(CellType.RAMP_MIDDLE);
                            break;
                        case '»':
                            cell.setType(CellType.RAMP_END);
                            break;
                        case '!':
                            cell.setType(CellType.TORCH);
                            break;
                        case 'L':
                            cell.setType(CellType.LADDER);
                            break;
                        case 'l':
                            cell.setType(CellType.LADDER_UPPER);
                            break;
                        case 'H':
                            cell.setType(CellType.LAKE_HOUSE);
                            break;
                        case 'F':
                            cell.setType(CellType.FAKE_DOOR);
                            break;
                        default:
                            throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }
        }
        return map;
    }

    private static void setPlayer(GameMap previousMap, GameMap map, Cell cell) {
        if (previousMap != null) {
            setExistingPlayer(previousMap, map, cell);
        } else {
            map.setPlayer(new Player(cell));
        }
    }

    private static void setExistingPlayer(GameMap previousMap, GameMap map, Cell cell) {
        Player previousPlayer = previousMap.getPlayer();
        cell.setActor(previousPlayer);
        previousPlayer.setCell(cell);
        map.setPlayer(previousPlayer);
    }
}

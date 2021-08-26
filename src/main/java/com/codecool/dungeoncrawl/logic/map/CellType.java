package com.codecool.dungeoncrawl.logic.map;

public enum CellType {
    EMPTY("empty"),
    FLOOR("floor"),
    FLOOR2("floor second"),
    WALL("wall"),
    BRICK_WALL("redbrick"),
    WATER("water"),
    HOUSE("house"),
    CLOSED_DOOR("closed door"),
    OPEN_DOOR("open door"),
    FAKE_DOOR("fake door"),
    RIVER("river"),
    RAMP_START("ramp start"),
    RAMP_MIDDLE("ramp middle"),
    RAMP_END("ramp end"),
    TORCH("torch"),
    LADDER("ladder"),
    LADDER_UPPER("ladder upper"),
    LAKE_HOUSE("lake house");

    private final String tileName;

    CellType(String tileName) {
        this.tileName = tileName;
    }

    public String getTileName() {
        return tileName;
    }
}

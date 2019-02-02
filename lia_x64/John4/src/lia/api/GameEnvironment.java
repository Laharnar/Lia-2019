package lia.api;

public class GameEnvironment {
    public long uid;
    public MessageType type;
    public boolean[][] map;
    public UnitLocation[] unitLocations;

    public GameEnvironment(long uid, MessageType type, boolean[][] map, UnitLocation[] unitLocations) {
        this.uid = uid;
        this.type = type;
        this.map = map;
        this.unitLocations = unitLocations;
    }
}

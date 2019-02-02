package lia.api;

public class GameState {
    public long uid;
    public MessageType type;
    public float time;
    public int numberOfRemainingOpponents;
    public boolean canSaySomething;
    public UnitData[] units;

    public GameState(long uid, MessageType type, float time, int numberOfRemainingOpponents, 
                     boolean canSaySomething, UnitData[] units) {
        this.uid = uid;
        this.type = type;
        this.time = time;
        this.numberOfRemainingOpponents = numberOfRemainingOpponents;
        this.canSaySomething = canSaySomething;
        this.units = units;
    }
}



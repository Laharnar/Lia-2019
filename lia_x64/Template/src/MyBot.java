import lia.api.*;
import lia.*;

/**
 * Here is where you control your bot. Initial implementation keeps sending units
 * to random locations on the map and makes them shoot when they see opponents.
 */
public class MyBot implements Bot {

    // Here we store the map as a 2D array of booleans. If map[x][y] equals True that
    // means that at (x,y) there is an obstacle. x=0, y=0 points to bottom left corner.
    boolean[][] map;

    // In this method we receive the basic information about the game environment.
    // - GameEnvironment reference: https://docs.liagame.com/api/#gameenvironment
    @Override
    public synchronized void processGameEnvironment(GameEnvironment gameEnvironment) {

        // We store the game map so that we can use it later.
        map = gameEnvironment.map;
    }

    // This is the main method where you control your bot. 10 times per game second it receives
    // current game state. Use Api object to call actions on your units.
    // - GameState reference: https://docs.liagame.com/api/#gamestate
    // - Api reference:       https://docs.liagame.com/api/#api-object
    @Override
    public synchronized void processGameState(GameState gameState, Api api) {


// Iterate through all of your units.
        for (int i = 0; i < gameState.units.length; i++) {
            UnitData unit = gameState.units[i];

            // If the unit sees at least one of the opponents start turning towards it.
            if (unit.opponentsInView.length > 0) {

                // Get the first opponent that the unit sees.
                OpponentInView opponent = unit.opponentsInView[0];

                // Calculate the aiming angle between units orientation and the opponent. The closer
                // the angle is to 0 the closer is the unit aiming towards the opponent.
                float aimAngle = MathUtil.angleBetweenUnitAndPoint(unit, opponent.x, opponent.y);

                // Stop the unit.
                api.setSpeed(unit.id, Speed.NONE);

                // Based on the aiming angle turn towards the opponent.
                if (aimAngle < 0) {
                    api.setRotation(unit.id, Rotation.RIGHT);
                } else {
                    api.setRotation(unit.id, Rotation.LEFT);
                }
            }
        }

    }

    // Connects your bot to Lia game engine, don't change it.
    public static void main(String[] args) throws Exception {
        NetworkingClient.connectNew(args, new MyBot());
    }
}

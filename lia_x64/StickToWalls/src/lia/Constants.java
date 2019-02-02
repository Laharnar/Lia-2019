package lia;

import com.google.gson.JsonObject;

/**
 * Constants are set on runtime from game engine, changing them has
 * no effect. Find the predefined values in data/game-config.json
 * or print them out in processGameEnvironment() method in your bot
 * implementation.
 */
public class Constants {
    /** The width of the map in world units. */
    public static int MAP_WIDTH;
    /** The height of the map in world units. */
    public static int MAP_HEIGHT;
    /** The duration of the game in seconds. */
    public static float GAME_DURATION;
    /** The diameter of the unit in world units. */
    public static float UNIT_DIAMETER;
    /** A full health of a unit when the game starts. */
    public static int UNIT_FULL_HEALTH;
    /** The velocity in world units per second with which the unit moves forward. */
    public static float UNIT_FORWARD_VELOCITY;
    /** The velocity in world units per second with which the unit moves backward. */
    public static float UNIT_BACKWARD_VELOCITY;
    /** The angle with which the unit's orientation changes per second when rotating normally. */
    public static float UNIT_ROTATION_VELOCITY;
    /** The angle with which the unit's orientation changes per second when rotating slowly. */
    public static float UNIT_SLOW_ROTATION_VELOCITY;
    /** Delay between shooting two pre-loaded bullets. */
    public static float DELAY_BETWEEN_SHOTS;
    /** The time to reload one bullet. */
    public static float RELOAD_TIME;
    /** A maximum number of bullets that a unit can hold at once. */
    public static int MAX_BULLETS;
    /** The time after which the unit starts to regenerate health after being hit by a bullet. */
    public static float HEALTH_REGENERATION_DELAY;
    /** The amount of health points per second that the unit receives when recovering. */
    public static int HEALTH_REGENERATION_PER_SECOND;
    /** The length of unit's viewing area. */
    public static float VIEWING_AREA_LENGTH;
    /** The width of unit's viewing area at the side that is the furthest away from the unit. */
    public static float VIEWING_AREA_WIDTH;
    /** The amount by which is the start of a viewing area offset from the unit's center
     * (negative means towards the back). */
    public static float VIEWING_AREA_OFFSET;
    /** The diameter of the bullet in world units. */
    public static float BULLET_DIAMETER;
    /** The speed in world units per second with which the bullet moves forward. */
    public static float BULLET_VELOCITY;
    /** The damage that a unit receives when it is hit by a bullet. */
    public static int BULLET_DAMAGE;
    /** The range of the bullet in world units. */
    public static float BULLET_RANGE;
    /** The maximum duration of processing processGameEnvironment() method. */
    public static float PROCESS_GAME_ENVIRONMENT_TIMEOUT;
    /** The maximum duration of processing processGameState() method. */
    public static float PROCESS_GAME_STATE_TIMEOUT;

    protected static void load(JsonObject constantsJson) {
        MAP_WIDTH = constantsJson.get("MAP_WIDTH").getAsInt();
        MAP_HEIGHT = constantsJson.get("MAP_HEIGHT").getAsInt();
        GAME_DURATION = constantsJson.get("GAME_DURATION").getAsFloat();
        UNIT_DIAMETER = constantsJson.get("UNIT_DIAMETER").getAsFloat();
        UNIT_FULL_HEALTH = constantsJson.get("UNIT_FULL_HEALTH").getAsInt();
        UNIT_FORWARD_VELOCITY = constantsJson.get("UNIT_FORWARD_VELOCITY").getAsFloat();
        UNIT_BACKWARD_VELOCITY = constantsJson.get("UNIT_BACKWARD_VELOCITY").getAsFloat();
        UNIT_ROTATION_VELOCITY = constantsJson.get("UNIT_ROTATION_VELOCITY").getAsFloat();
        UNIT_SLOW_ROTATION_VELOCITY = constantsJson.get("UNIT_SLOW_ROTATION_VELOCITY").getAsFloat();
        DELAY_BETWEEN_SHOTS = constantsJson.get("DELAY_BETWEEN_SHOTS").getAsFloat();
        RELOAD_TIME = constantsJson.get("RELOAD_TIME").getAsFloat();
        MAX_BULLETS = constantsJson.get("MAX_BULLETS").getAsInt();
        HEALTH_REGENERATION_DELAY = constantsJson.get("HEALTH_REGENERATION_DELAY").getAsFloat();
        HEALTH_REGENERATION_PER_SECOND = constantsJson.get("HEALTH_REGENERATION_PER_SECOND").getAsInt();
        VIEWING_AREA_LENGTH = constantsJson.get("VIEWING_AREA_LENGTH").getAsFloat();
        VIEWING_AREA_WIDTH = constantsJson.get("VIEWING_AREA_WIDTH").getAsFloat();
        VIEWING_AREA_OFFSET = constantsJson.get("VIEWING_AREA_OFFSET").getAsFloat();
        BULLET_DIAMETER = constantsJson.get("BULLET_DIAMETER").getAsFloat();
        BULLET_VELOCITY = constantsJson.get("BULLET_VELOCITY").getAsFloat();
        BULLET_DAMAGE = constantsJson.get("BULLET_DAMAGE").getAsInt();
        BULLET_RANGE = constantsJson.get("BULLET_RANGE").getAsFloat();
        PROCESS_GAME_ENVIRONMENT_TIMEOUT = constantsJson.get("PROCESS_GAME_ENVIRONMENT_TIMEOUT").getAsFloat();
        PROCESS_GAME_STATE_TIMEOUT = constantsJson.get("PROCESS_GAME_STATE_TIMEOUT").getAsFloat();
    }
}
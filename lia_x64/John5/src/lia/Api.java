package lia;

import com.google.gson.Gson;
import lia.api.*;

import java.util.ArrayList;

/**
 * Used for building a response message that is later
 * sent to the game engine.
 **/
public class Api {

    private long uid;
    private int currentIndex = 0;

    private ArrayList<SpeedEvent> speedEvents;
    private ArrayList<RotationEvent> rotationEvents;
    private ArrayList<ShootEvent> shootEvents;
    private ArrayList<NavigationStartEvent> navigationStartEvents;
    private ArrayList<NavigationStopEvent> navigationStopEvents;
    private ArrayList<SaySomethingEvent> saySomethingEvents;

    protected Api() {
        speedEvents = new ArrayList<>();
        rotationEvents = new ArrayList<>();
        shootEvents = new ArrayList<>();
        navigationStartEvents = new ArrayList<>();
        navigationStopEvents = new ArrayList<>();
        saySomethingEvents = new ArrayList<>();
    }

    private int getNextIndex() {
        return currentIndex++;
    }

    protected void setUid(long uid) {
        this.uid = uid;
    }

    /** Change thrust speed of a unit */
    public void setSpeed(int unitId, Speed speed) {
        speedEvents.add(new SpeedEvent(getNextIndex(), unitId, speed));
    }

    /** Change rotation speed of a unit */
    public void setRotation(int unitId, Rotation rotation) {
        rotationEvents.add(new RotationEvent(getNextIndex(), unitId, rotation));
    }

    /** Make a unit shoot */
    public void shoot(int unitId) {
        shootEvents.add(new ShootEvent(getNextIndex(), unitId));
    }

    /** Start navigation */
    public void navigationStart(int unitId, float x, float y) {
        navigationStartEvents.add(new NavigationStartEvent(getNextIndex(), unitId, x, y));
    }

    /** Stop navigation */
    public void navigationStop(int unitId) {
        navigationStopEvents.add(new NavigationStopEvent(getNextIndex(), unitId));
    }

    /** Make your unit say something */
    public void saySomething(int unitId, String text) {
        saySomethingEvents.add(new SaySomethingEvent(getNextIndex(), unitId, text));
    }

    protected String toJson() {
        Response response = new Response(
                uid,
                MessageType.RESPONSE,
                speedEvents.toArray(new SpeedEvent[speedEvents.size()]),
                rotationEvents.toArray(new RotationEvent[rotationEvents.size()]),
                shootEvents.toArray(new ShootEvent[shootEvents.size()]),
                navigationStartEvents.toArray(new NavigationStartEvent[navigationStartEvents.size()]),
                navigationStopEvents.toArray(new NavigationStopEvent[navigationStopEvents.size()]),
                saySomethingEvents.toArray(new SaySomethingEvent[saySomethingEvents.size()])
        );
        return (new Gson()).toJson(response);
    }
}
import com.sun.javafx.geom.Vec2d;
import lia.api.*;
import lia.*;

import java.nio.channels.FileChannel;
import java.security.acl.Group;
import java.util.*;

class TestEnv{
    public TargetIterator wallsSrc;

    TestEnv (){
        wallsSrc = new TargetIterator(new ArrayList<MapNode>());
    }
}

interface UnitBehaviour{
    void Behave();
}

public class MyBot implements Bot {



    // Here we store the map as a 2D array of booleans. If map[x][y] equals True that
    // means that at (x,y) there is an obstacle. x=0, y=0 points to bottom left corner.
    boolean[][] map;
    MyMap myMap;
    int myMode = 0;
    TestEnv env;
    boolean firstInit = false;
    CombatManager manager;

    // generalize these groups into strategies
    ScoutingGroup scouts;
    OffensiveGroup[] attacking;

    // In this method we receive the basic information about the game environment.
    // - GameEnvironment reference: https://docs.liagame.com/api/#gameenvironment
    @Override
    public synchronized void processGameEnvironment(GameEnvironment gameEnvironment) {

        // We store the game map so that we can use it later.
        map = gameEnvironment.map;
        myMap = new MyMap(map);
        env = new TestEnv();
        env.wallsSrc = new TargetIterator(myMap.getWalls());
        myMap.printDrawWallCount();
        myMap.printWallRegionId();
        manager = new CombatManager();
    }
    int state = 0;

    // This is the main method where you control your bot. 10 times per game second it receives
    // current game state. Use Api object to call actions on your units.
    // - GameState reference: https://docs.liagame.com/api/#gamestate
    // - Api reference:       https://docs.liagame.com/api/#api-object
    @Override
    public synchronized void processGameState(GameState gameState, Api api) {

        if (firstInit == false) {
            Vec2d startingPos = gameState.units[0].getPos();
            firstInit = true;
            scouts = new ScoutingGroup(GroupFactory.PickMaximalToCoverMapHeight(), myMap, startingPos);
            scouts.ExecutePlans(api);

            manager.ApplyBehaviors(scouts.group, scouts);

            attacking = new OffensiveGroup[5];
            for (int i = 0; i < 5; i++) {
                int[] newGroup =  manager.GetFreeUnits(3);
                if (manager.ValidGroup(newGroup)){
                    System.out.println("Creating attack group of 3 id: "+i);
                    attacking[i] = new OffensiveGroup(newGroup, myMap, startingPos);
                    manager.ApplyBehaviors(newGroup, attacking[i]);
                }
            }

            return;
        }
        // Update manager
        manager.gameState = gameState;
        for (int i = 0; i < gameState.units.length; i++) {
            manager.data.put(gameState.units[i].id, gameState.units[i]);
        }

        // scouts
        if (scouts.Done(gameState.units) && state == 0){
            state++;
            scouts.NextState();
            scouts.ExecutePlans(api);
        }

        // [Processing detection]
        manager.visibleEnemies = OffensiveGroup.CollectEnemyData(gameState.units);

        // Send aggressive squads
        if (manager.visibleEnemies.opponents.size() > 0){
            System.out.println("EnemyCount "+manager.visibleEnemies.opponents.size());
            for (int i = 0; i < attacking.length; i++) {
                if (attacking[i] != null)
                attacking[i].targetPos = manager.RandomEnemy();

            }
        }
        for (int i = 0; i < attacking.length; i++) {
            if (attacking[i] == null) continue;;
            attacking[i].behave = manager.visibleEnemies.opponents.size() > 0;
            attacking[i].ExecutePlans(api, manager);
        }

        if (true)
            return;
        // go to random, or follow target.
        // We iterate through all of our units that are still alive.
        for (int i = 0; i < gameState.units.length; i++) {
            UnitData unit = gameState.units[i];

            if (i == 0) {
                // move to wall edges.
                if (unit.navigationPath.length == 0) {
                    int x = 0; int y = 0;

                    // go over every wall starting from 1,1
                    MapNode xy = myMap.getMapNode((int) unit.x, (int) unit.y);
                    //System.out.println(String.format("%s %s %s %s %s",MapNode.addVec(env.wallsSrc.getItem().getPos(), MapNode.mulVec(MapNode.normalizeVec(MapNode.subVec(env.wallsSrc.getItem().getPos(), unit.getPos())), 6)), MapNode.mulVec(MapNode.normalizeVec(MapNode.subVec(env.wallsSrc.getItem().getPos(), unit.getPos())), 6), MapNode.subVec(env.wallsSrc.getItem().getPos(), unit.getPos()), env.wallsSrc.getItem().getPos().toString(), unit.getPos().toString()));

                    MapNode v2 = env.wallsSrc.getItem(true, myMap.width, myMap.height);
                    if (v2 != null) {
                        Vec2d pos = MapNode.addVec(env.wallsSrc.getItem().getPos(), MapNode.mulVec(MapNode.normalizeVec(MapNode.subVec(unit.getPos(), env.wallsSrc.getItem().getPos())), 2));
                        x = (int) pos.x;
                        y = (int) pos.y;
                        System.out.println(String.format("Nav id= %d %d %d", env.wallsSrc.getTargetId(), x + 1, y + 1));

                        if (!map[y][x] && x + 1 < myMap.width-1 && y+1 < myMap.height-1) {
                            api.navigationStart(unit.id, x + 1, y + 1);
                            api.saySomething(unit.id, String.format("Nav id= %d %d %d", env.wallsSrc.getTargetId(), x+1, y+1));
                        }
                    }
                    env.wallsSrc.goNext();


                    /*// go to nearest wall near unit pos.
                    MapNode xy = myMap.getMapNode((int) unit.x, (int) unit.y);
                    if (xy != null) {
                        int[] wallxy = xy.closestPointToWall();
                        x = (int) wallxy[0];
                        y = (int) wallxy[1];

                        if (x == 0) x= 1;
                        if(y == 0)y = 1;
                        if (x>=myMap.width-1)x = x = myMap.width-2;
                        if (x>=myMap.height-1)x = x = myMap.width-2;

                    }
                    api.navigationStart(unit.id, x, y);
*/
                    /*
                    // go to corners
                    if (myMode == 0)
                        api.navigationStart(unit.id, 1, 1);
                    if (myMode == 1)
                        api.navigationStart(unit.id, myMap.width-2, myMap.height-1);
                    if (myMode == 2)
                        api.navigationStart(unit.id, myMap.width-2, 1);
                    if (myMode == 3)
                        api.navigationStart(unit.id, 1, myMap.height-2);
                    if (myMode < 4) {
                        System.out.println("going to path in mode "+myMode);
                        myMode = (myMode+1)%4;
                    }*/
                }
            }
            else if (false)
            // If the unit is not going anywhere, we choose a new valid point on the
            // map and send the unit there.
            if (unit.navigationPath.length == 0) {

                int x = 0, y = 0;
                if (gameState.time < 1) {
                    System.out.println(String.format("%d %d", unit.y, unit.x));
                    MapNode xy = myMap.getMapNode((int) unit.x, (int) unit.y);
                    if (xy != null) {
                        int[] wallxy = xy.closestPointToWall();
                        x = (int) wallxy[0];
                        y = (int) wallxy[1];
                        System.out.println(i+" "+myMap.width + " " + myMap.height + " " + unit.x + "," + unit.y + " " + xy.idToWallExclusive + " "+x+" "+y);
                        // Generate new x and y until you get a position on the map where there
                        // is no obstacle.
                            /*while (true) {
                                MapNode xy = myMap.getMapNodes()[(int)unit.x][(int)unit.y];
                                x = (int) xy.idToWall.x; //(Math.random() * (map.length));
                                y = (int) xy.idToWall.y;//(Math.random() * (map[0].length));
                                // False means that on (x,y) there is no obstacle.
                                if (!map[x][y]) {
                                    break;
                                }
                            }*/

                        // Make the unit go to the chosen x and y.
                        //}
                    }
                    api.navigationStart(unit.id, x + 1, y + 1);


                }
            }


            // If the unit sees an opponent then make it shoot.
            if (unit.opponentsInView.length > 0) {
                //api.shoot(unit.id);

                // Don't forget to make your unit brag. :)
                //api.saySomething(unit.id, "I see you afk!");
            }
        }
    }

    // Connects your bot to Lia game engine, don't change it.
    public static void main(String[] args) throws Exception {
        NetworkingClient.connectNew(args, new MyBot());
    }
}

import com.sun.javafx.geom.Vec2d;
import com.sun.javafx.geom.Vec2f;
import lia.MathUtil;

/**
 * Here is where you control your bot. Initial implementation keeps sending units
 * to random locations on the map and makes them shoot when they see opponents.
 */
public class MapNode{
    boolean hasWall = false;
    public int x, y;
    public Vec2d idToWallExclusive;
    public Vec2d idToWallInclusive;
    public boolean walkable = false;
    public int nodeType = 0;
    public int wallNeighbours;// bits -> 0000 = no walls, 1111 = 4 walls
    public int wallRegion = MyMap.DEFAULT_REGION;

    public MapNode(boolean hasWall, int idX, int idY) {
        this.hasWall = hasWall;
        this.x = idX;
        this.y = idY;
        idToWallExclusive = new Vec2d(0, 0);
        idToWallInclusive = new Vec2d(0, 0);

    }

    public Vec2d getPos(){
        return new Vec2d(x, y);
    }

    public void setIdToWallExclusive(Vec2d idToWallExclusive) {
        this.idToWallExclusive = idToWallExclusive;
    }

    public void setIdToWallInclusive(Vec2d idToWallInclusive) {
        this.idToWallInclusive = idToWallInclusive;
    }



    public int[] closestWallId(){
        return new int[]{(int)idToWallExclusive.x-x, (int)idToWallExclusive.y-y};
    }

    public int[] closestPointToWall(){
        int idX = x, idY = y;

        if (idToWallExclusive.x-x < 0)
            idX = x-(int)idToWallExclusive.x  +1;
        else if (idToWallExclusive.x-x > 0) idX = (int)idToWallExclusive.x - x -1;
        if (idToWallExclusive.y-y < 0)
            idY = y-(int)idToWallExclusive.y +1;
        else if (idToWallExclusive.y-y > 0) idY = (int)idToWallExclusive.y-y -1;

        return new int[]{idX, idY};
    }

    public static Vec2d subVec(Vec2d v1, Vec2d v2) {
        return new Vec2d(v1.x - v2.x, v1.y - v2.y);
    }

    public static Vec2d addVec(Vec2d v1, Vec2d v2) {
        return new Vec2d(v1.x + v2.x, v1.y + v2.y);
    }

    public static Vec2d divVec(Vec2d v1, double val) {
        return new Vec2d(v1.x /val, v1.y /val);
    }

    public static double lengthVec(Vec2d v){
        return Math.sqrt(v.x*v.x + v.y*v.y);
    }

    public static Vec2d normalizeVec(Vec2d v1) {
        return divVec(v1, lengthVec(v1));
    }

    public static Vec2d mulVec(Vec2d v1, double val) {
        return new Vec2d(v1.x *val, v1.y *val);
    }

    public void setWallsAround(boolean right, boolean top, boolean left, boolean bot) {
        int r = right ? 1: 0, t = top ? 1 : 0, l = left ? 1 : 0, b = bot ? 1 : 0;
        wallNeighbours  = (r << 0) | (t << 1) | (l << 2) | (b << 3);
        // System.out.println(Integer.toBinaryString(wallNeighbours) + " "+wallNeighbours);
    }
}
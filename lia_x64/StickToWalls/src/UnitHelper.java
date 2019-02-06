import com.sun.javafx.geom.Vec2d;
import lia.api.OpponentInView;
import lia.api.UnitData;

public class UnitHelper{
    public static Vec2d GetPos(UnitData unit) {
        return new Vec2d(unit.x, unit.y);
    }

    public static Vec2d GetPosOpponent(OpponentInView unit){
        return new Vec2d(unit.x, unit.y);
    }

    public static double GetLen(Vec2d pt) {
        return Math.sqrt(pt.x * pt.x + pt.y * pt.y);
    }

    public static Vec2d GetUp(UnitData unit){
        return new Vec2d(Math.cos(unit.orientationAngle), Math.sin(unit.orientationAngle));
    }
    public static float GetRightAngle(float unitOrientationAngle) {
        float angle = unitOrientationAngle-90;

        if (angle > 180) angle -= 360;
        else if (angle < -180) angle += 360;
        return angle;
    }
    public static Vec2d GetRight(UnitData unitState) {
        float angle = GetRightAngle(unitState.orientationAngle);
        return new Vec2d(Math.sin(angle), Math.cos(angle));
    }
}

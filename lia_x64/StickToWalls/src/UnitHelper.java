import com.sun.javafx.geom.Vec2d;
import lia.api.OpponentInView;
import lia.api.UnitData;

public class UnitHelper{
    public static void GetPos(UnitData unit){
       // todo
    }

    public Vec2d GetPosOpponent(OpponentInView unit){
        return new Vec2d(unit.x, unit.y);
    }
}

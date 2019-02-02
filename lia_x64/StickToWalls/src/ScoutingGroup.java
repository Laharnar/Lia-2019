import com.sun.javafx.geom.Vec2d;
import lia.Api;
import lia.api.UnitData;

public class ScoutingGroup implements UnitBehaviour {
    UnitData[] group;
    LongPlanning[] planner;

    ScoutingGroup(UnitData[] units, MyMap map){
        group = units;
        planner = new LongPlanning[units.length];

        for (int i = 0; i < group.length; i++) {
            planner[i] = new LongPlanning();
            planner[i].SetPlan_2Sides(units[i].getPos().x > 300,
                    (double)(i+1)/(group.length) *(map.height-40f), map.width);
        }
    }

    void ExecutePlans(int i, UnitData unit, LongPlanning plan, Api api){
        //api.navigationStart(unit.id, (int)plan.plan[1].x+1,(int) plan.plan[1].y+1);
    }

    @Override
    public void Behave() {

    }

    public void ExecutePlans(Api api) {
        for (int i = 0; i < group.length; i++) {
            api.navigationStart(group[i].id, (int)planner[i].GetCurrent().x+1,(int) planner[i].GetCurrent().y+1);
        }
    }

    public void NextState(){
        for (int i = 0; i < group.length; i++) {
            planner[i].ToNext();
        }
    }
boolean once = false;
    public boolean Done() {
        for (int i = 0; i < group.length; i++) {
            if (once == false) {
                System.out.println("id: " + group[i].id + " " + group[i].navigationPath.length);
            }
            if (IsDone(group[i]) == false){
                return false;
            }
        }
        once = true;

        return  true;
    }


    boolean IsDone(UnitData unit) {
        return unit.navigationPath.length == 0;
    }
}

class LongPlanning{
    Vec2d[] plan;
    int active;

    void SetPlan_2Sides(boolean mirror, double y, int mapWidth){
        plan = new Vec2d[]{
                new Vec2d(!mirror ? mapWidth-20 : 20, y),
                new Vec2d(!mirror ? 20 : mapWidth-20, y)
        };
    }

    public Vec2d GetCurrent(){
        return plan[active];
    }

    public void ToNext(){
        active = (active+1)%plan.length;
    }

}
class Sector{
    int sectorX,  sectorY;
    int pixelsWidth, pixelsHeight;
    boolean done;

    void PickLocation(){
        // returns random location in this sector
    }
}
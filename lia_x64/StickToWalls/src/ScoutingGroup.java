import com.sun.javafx.geom.Vec2d;
import lia.Api;
import lia.api.OpponentInView;
import lia.api.UnitData;

import java.util.ArrayList;

class EnemiesInfo{
    public ArrayList<OpponentInView> opponents = new ArrayList<>();
    // must be updated every frame


}

public class ScoutingGroup extends UnitGroup {

    // Note: concept of zones - tell how many scouts you need by the number of zones, which can be based on numer of
    // general paths.
    // Note: fog of war - have scouts explore it.

    LongPlanning[] planner;

    ScoutingGroup(int[] unitIds, MyMap map, Vec2d startingPos){
        group = unitIds;
        planner = new LongPlanning[unitIds.length];

        for (int i = 0; i < group.length; i++) {
            planner[i] = new LongPlanning();
            planner[i].SetPlan_2Sides(startingPos.x > 300,
                    (double)(i+1)/(group.length) *(map.height-5), map.width);
        }
    }

    public void ExecutePlans(Api api) {
        for (int i = 0; i < group.length; i++) {
            System.out.println("Navigation : "+group[i]);
            api.navigationStart(group[i], (int)planner[i].GetCurrent().x+1,(int) planner[i].GetCurrent().y+1);
        }
    }

    public void NextState(){
        for (int i = 0; i < group.length; i++) {
            planner[i].ToNext();
        }
    }
    public boolean Done(UnitData[] units) {
        for (int i = 0; i < group.length; i++) {
            for (int j = 0; j < units.length; j++) {
                if (units[i].id == group[i]){
                    if (IsDone(units[group[i]]) == false){
                        return false;
                    }
                }
            }
        }
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
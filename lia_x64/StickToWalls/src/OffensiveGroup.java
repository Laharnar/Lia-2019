import com.sun.javafx.geom.Vec2d;
import lia.Api;
import lia.api.OpponentInView;
import lia.api.UnitData;

import java.util.Dictionary;
import java.util.Hashtable;

public class OffensiveGroup extends UnitGroup {
    // Offensive group behaviours: stick together, focus on single found enemy.

    Vec2d targetPos;

    OffensiveGroup(int[] unitIds, MyMap map, Vec2d startingPos){
        group = unitIds;
    }

    public void ExecutePlans(Api api, CombatManager manager) {
        if (!behave)return;
        EnemiesInfo info = manager.visibleEnemies;
        if (info.opponents.size() > 0)
        for (int i = 0; i < group.length; i++) {
            UnitData unit = manager.data.get(group[i]);

            if (unit.opponentsInView.length == 0) {
                if (unit.navigationPath.length == 0) {
                    System.out.println("Offensive Navigation : " + group[i]);
                    api.navigationStart(group[i], (int) targetPos.x, (int) targetPos.y);
                }
            }else  {
                api.shoot(group[i]);
            }
        }
    }

    // collect from n units
    public static EnemiesInfo CollectEnemyData(int[] group, UnitData[] allAllies){
        EnemiesInfo info = new EnemiesInfo();
        Dictionary<Integer, UnitData> dict = new Hashtable(); // generalize this --> export it to manager
        for (int j = 0; j < allAllies.length; j++) {
            allAllies[allAllies[j].id] = allAllies[j];
        }
        for (int i = 0; i < group.length; i++) {
            if(dict.get(group[i])!= null){
                CollectEnemyData(allAllies[group[i]], info);
            }
        }
        return info;
    }

    // Collect enemy data from ally units
    public static EnemiesInfo CollectEnemyData(UnitData[] allies){
        EnemiesInfo info = new EnemiesInfo();
        for (int i = 0; i < allies.length; i++) {
            CollectEnemyData(allies[i], info);
        }
        return info;
    }

    static void CollectEnemyData(UnitData ally, EnemiesInfo perUnit){
        for (int i = 0; i < ally.opponentsInView.length; i++) {
            perUnit.opponents.add(ally.opponentsInView[i]);
        }
    }

}

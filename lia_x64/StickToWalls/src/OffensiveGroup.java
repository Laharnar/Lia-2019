import com.sun.javafx.geom.Vec2d;
import lia.Api;
import lia.Constants;
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
        // nav to target position, or if have an enemy, attack
        EnemiesInfo info = manager.visibleEnemies;
        if (info.opponents.size() > 0)
        for (int i = 0; i < group.length; i++) {
            UnitInfo unit = manager.data.get(group[i]);
            if (unit.unitState.opponentsInView.length == 0) {
                api.saySomething(unit.unitState.id, "Nav out of combat");
                NavigateOutOfCombat(api, group[i], unit);
            }else  {
                api.saySomething(unit.unitState.id, "Nav in combat, shoot");
                if (unit.mechanics.ManeveurNearClosestEnemy(unit, Constants.VIEWING_AREA_LENGTH)){
                    api.shoot(group[i]);
                }
            }
        }
    }

    private void NavigateOutOfCombat(Api api, int i, UnitInfo unit) {
        if (unit.unitState.navigationPath.length > 0) return;
        System.out.println("Offensive Navigation : " + i);
        api.navigationStart(i, (int) targetPos.x, (int) targetPos.y);
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

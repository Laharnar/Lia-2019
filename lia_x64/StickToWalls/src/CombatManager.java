import com.sun.javafx.geom.Vec2d;
import lia.Api;
import lia.api.OpponentInView;
import lia.api.Speed;
import lia.api.UnitData;
import lia.api.GameState;

import java.util.*;

class UnitNavigation {

}

public class CombatManager{

    public static Api api;
    public EnemiesInfo visibleEnemies;

    public Map<Integer, UnitGroup> behaviour = new HashMap<Integer, UnitGroup>();
    public Map<Integer, UnitInfo> data = new HashMap<Integer, UnitInfo>();// per frame updated data

    Random rand;
    public GameState gameState;

    CombatManager(){
        for (int i = 0; i < 16; i++) {
            behaviour.put(i, null);
        }
    }

    public void ApplyBehaviors(int[] group, UnitGroup scouts) {
        for (int i = 0; i < group.length; i++) {
            if (behaviour.get(group[i]) != null) {
                System.out.println("Overriding behaviour "+group[i]+" "+behaviour.get(group[i]).getClass().getTypeName()
                +" --> "+scouts.getClass().getTypeName());
            }else
                System.out.println("Applying behaviour "+group[i]+" "
                        +" == "+scouts.getClass().getTypeName());
            behaviour.put(group[i], scouts);
        }
    }

    public int[] GetFreeUnits(int size) {
        int[] group = new int[size];
        for (int i = 0; i < size; i++) {
            group[i] = -1;
        }
        int gid = 0;
        for (int i = 0; i < 16 && gid< size; i++) {
            if (behaviour.get(i) == null) {
                group[gid] = i;
                gid++;
            }
        }
        return group;
    }

    public boolean GroupProperlyCreated(int[] newGroup) {
        for (int i = 0; i < newGroup.length; i++) {
            if (newGroup[i] <0)
                return false;
        }
        return  true;
    }
    public Vec2d RandomEnemy() {
        if(rand == null)
            rand = new Random();
        OpponentInView oiv = visibleEnemies.opponents.get(rand.nextInt(visibleEnemies.opponents.size()));
        return new Vec2d(oiv.x, oiv.y);
    }
}

import java.util.ArrayList;
import java.util.List;

public class TargetIterator{
    // iterates through map points - long term planning pathfinding
    public List<MapNode> targets = new ArrayList<MapNode>();
    int targetId = 0;

    TargetIterator(List<MapNode> targets){
        this.targets = targets;
    }

    public List<MapNode> getTargets() {
        return targets;
    }

    public int getTargetId(){
        return targetId;
    }

    public MapNode getItem(boolean inclusive, int width, int height){
        MapNode n = targets.size() > 0 ? targets.get(getTargetId()) : null;
        if (inclusive){
            if (n.x == 0 || n.x == width-1 || n.y == 0 || n.y == height-1)
                n = null;
        }
        return n;
    }

    public MapNode getItem(){
        return targets.size() > 0 ? targets.get(getTargetId()) : null;
    }

    public void goNext(){
        targetId ++;
        if (targetId == targets.size())
            targetId--;
    }
}

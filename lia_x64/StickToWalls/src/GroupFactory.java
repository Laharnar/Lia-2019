import lia.Constants;

public class GroupFactory{

    public static int[] PickMaximalToCoverMapHeight(){
        int numOfUnits = (int)Math.ceil(Constants.MAP_HEIGHT/ Constants.VIEWING_AREA_WIDTH)+1;
        return CreateGroup (numOfUnits);
    }
    public static int[] PickMinimalToCoverMapHeight(){
        int numOfUnits = (int)Math.floor(Constants.MAP_HEIGHT/ Constants.VIEWING_AREA_WIDTH);
        return CreateGroup (numOfUnits);
    }

    // TODO: have a list of jobs for units.
    public static int[] CreateGroup(int numOfUnits){
        int[] groupIDs = new int[numOfUnits];
        for (int i = 0; i < numOfUnits; i++) {
            groupIDs[i] = i;
        }
        return groupIDs;
    }
}

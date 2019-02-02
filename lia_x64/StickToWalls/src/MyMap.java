import com.sun.javafx.geom.Vec2d;

import java.util.*;

public class MyMap{

    public static final int DEFAULT_REGION = 99;
    static boolean LOGGER = true;

    int width, height;
    private boolean[][] map;
    private MapNode[][] mapNodes;// manhatt distance to closest wall
    private List<MapNode> walls;

    MyMap(boolean[][] map){
        this.map = map;
        width = map.length;
        height = map[0].length;

        // calculations
        // get all walls, then calculatue directions to closest walls
        mapNodes = new MapNode[width][];
        for (int i = 0; i < width; i++) {
            mapNodes[i] = new MapNode[height];
        }
        walls = new ArrayList();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (getMap(i, j)) {
                    MapNode node = new MapNode(getMap(i, j), i, j);
                    setMapNode(i, j, node);
                    walls.add(node);
                }
            }
        }


        // init each node - wall edges
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (getMap(i, j)) continue;;

                MapNode node = new MapNode(getMap(i, j), i, j);

                node.setIdToWallExclusive(getClosestWallDir(i, j, walls, width, height, false));
                node.setIdToWallInclusive(getClosestWallDir(i, j, walls, width, height, true));


                setMapNode(i, j, node);
                if (i == width-1 && j == 1)
                    System.out.println(String.format("%d %d %s ", i, j, node.idToWallExclusive.toString()));
            }
        }
        // init - for every pt, exclusive, mark walls around
        for (int i = 1; i < width-1; i++) {
            for (int j = 1; j < height-1; j++) {
                getMapNode(i, j).setWallsAround(getMap(i+1, j), getMap(i, j-1), getMap(i-1, j), getMap(i, j+1));
            }
        }

        // init - step 1: 8 direction wall regions
        CalculateRegions_goodEnough();
        if (LOGGER) System.out.println(String.format("LOG: MyMap initialized. w:%d h:%d w*h:%d walls:%d .walls =%f", width, height, width*height, walls.size(),  (float)((float)walls.size()/(float)(width*height))));

        List<MapNode>m = new ArrayList<MapNode>();
        m.add(new MapNode(true, 1, 1));
        m.add(new MapNode(true, 0, 1));
//        System.out.println(String.format("%s %s ", (getClosestWallDir(0, 0, m, false).x == 1), getClosestWallDir(0, 0, m, false).y == 1));
    }

    private void CalculateRegions_goodEnough() {
        // some spots get ignored.
        int regionId = 10;
        int[] jointRegions = new int[100];
        for (int i = 0; i < 100; i++) {
            jointRegions [i] = 99;
        }
        boolean nextRegion = false;
        for (int i = 1; i < width-1; i++) {
            for (int j = 1; j < height-1; j++) {
                MapNode mn = getMapNode(i, j);
                if (!mn.hasWall) {
                    nextRegion = true;
                    continue;
                }
                // new unmarked corner
                if (getMapNode(i-1, j).wallRegion == DEFAULT_REGION && getMapNode(i, j-1).wallRegion == DEFAULT_REGION && getMapNode(i-1, j-1).wallRegion ==DEFAULT_REGION) {
                    if (nextRegion == true) {
                        nextRegion = false;
                        regionId++;
                    }
                    getMapNode(i, j).wallRegion = regionId;
                    jointRegions[getMapNode(i, j).wallRegion]
                            = jointRegions[getMapNode(i-1, j).wallRegion]
                            = jointRegions[getMapNode(i, j-1).wallRegion]
                            = jointRegions[getMapNode(i-1, j-1).wallRegion]
                            = Math.min(getMapNode(i, j).wallRegion,
                            Math.min(getMapNode(i-1, j).wallRegion,
                                    Math.min(getMapNode(i, j-1).wallRegion,
                                            getMapNode(i-1, j-1).wallRegion)));
                }
                else {
                    // all same -> copy label
                    if (getMapNode(i - 1, j).wallRegion == getMapNode(i, j - 1).wallRegion && getMapNode(i - 1, j).wallRegion == getMapNode(i - 1, j - 1).wallRegion){
                        getMapNode(i, j).wallRegion = getMapNode(i - 1, j).wallRegion;
                        jointRegions[getMapNode(i, j).wallRegion] = getMapNode(i, j).wallRegion;
                        /*   = jointRegions[getMapNode(i-1, j).wallRegion]
                                = jointRegions[getMapNode(i, j-1).wallRegion]
                                = jointRegions[getMapNode(i-1, j-1).wallRegion]
                                = Math.min(getMapNode(i, j).wallRegion,
                                Math.min(getMapNode(i-1, j).wallRegion,
                                        Math.min(getMapNode(i, j-1).wallRegion,
                                                getMapNode(i-1, j-1).wallRegion)));*/
                    }
                    else {
                        // find where the conflict is
                        jointRegions[getMapNode(i, j).wallRegion]
                                = jointRegions[getMapNode(i-1, j).wallRegion]
                                = jointRegions[getMapNode(i, j-1).wallRegion]
                                = jointRegions[getMapNode(i-1, j-1).wallRegion]
                                = Math.min(getMapNode(i, j).wallRegion,
                                Math.min(getMapNode(i-1, j).wallRegion,
                                Math.min(getMapNode(i, j-1).wallRegion,
                                    getMapNode(i-1, j-1).wallRegion)));
                        getMapNode(i, j).wallRegion = jointRegions[getMapNode(i, j).wallRegion];

/*
                        if(getMapNode(i - 1, j).wallRegion != DEFAULT_REGION){
                            getMapNode(i, j).wallRegion = getMapNode(i - 1, j).wallRegion;
                            jointRegions[getMapNode(i, j).wallRegion] = getMapNode(i, j).wallRegion;
                        }
                        if(getMapNode(i, j - 1).wallRegion != DEFAULT_REGION){
                            getMapNode(i, j).wallRegion = Math.min(jointRegions[getMapNode(i-1, j).wallRegion], getMapNode(i, j).wallRegion);;
                            if (getMapNode(i, j).wallRegion != getMapNode(i, j - 1).wallRegion) {
                                jointRegions[getMapNode(i-1, j).wallRegion] = jointRegions[getMapNode(i, j - 1).wallRegion]
                                        = Math.min(jointRegions[getMapNode(i, j - 1).wallRegion], getMapNode(i, j).wallRegion);
                            }
                        }
                        if (getMapNode(i - 1, j - 1).wallRegion != DEFAULT_REGION){
                            getMapNode(i, j).wallRegion = Math.min(jointRegions[getMapNode(i, j).wallRegion], getMapNode(i-1, j-1).wallRegion);;
                            if (getMapNode(i, j).wallRegion != getMapNode(i - 1, j - 1).wallRegion) {
                                jointRegions[getMapNode(i, j).wallRegion] = jointRegions[getMapNode(i - 1, j - 1).wallRegion]
                                        = Math.min(jointRegions[getMapNode(i, j).wallRegion], getMapNode(i-1, j-1).wallRegion);
                            }
                        }*/

                        // different -> take minimum and mark conflict
                        //getMapNode(i, j).wallRegion = Math.min(getMapNode(i - 1, j).wallRegion, getMapNode(i, j - 1).wallRegion);
                        //getMapNode(i, j).wallRegion = Math.min(getMapNode(i, j).wallRegion, getMapNode(i - 1, j - 1).wallRegion);

                        /*if (!jointRegions.containsKey(getMapNode(i - 1, j).wallRegion))
                            jointRegions.put(getMapNode(i - 1, j).wallRegion, new HashSet<>());
                        if (!jointRegions.containsKey(getMapNode(i, j - 1).wallRegion))
                            jointRegions.put(getMapNode(i, j - 1).wallRegion, new HashSet<>());
                        if (!jointRegions.containsKey(getMapNode(i - 1, j - 1).wallRegion))
                            jointRegions.put(getMapNode(i - 1, j - 1).wallRegion, new HashSet<>());*/

                        //if(getMapNode(i - 1, j).wallRegion != DEFAULT_REGION) jointRegions[getMapNode(i - 1, j).wallRegion] = Math.min(jointRegions[getMapNode(i - 1, j).wallRegion], getMapNode(i, j).wallRegion);
                        //if(getMapNode(i, j - 1).wallRegion != DEFAULT_REGION) jointRegions[getMapNode(i, j - 1).wallRegion] =  Math.min(jointRegions[getMapNode(i , j - 1).wallRegion], getMapNode(i, j).wallRegion);
                        //if(getMapNode(i - 1, j - 1).wallRegion != DEFAULT_REGION) jointRegions[getMapNode(i - 1, j - 1).wallRegion] = Math.min(jointRegions[getMapNode(i - 1, j - 1).wallRegion], getMapNode(i, j).wallRegion);

                    }
                }
            }
        }

        for (int j = 0; j < 300; j++) {
            for (int i = 0; i < 100; i++) {
                jointRegions[i] = Math.min(jointRegions[i], jointRegions[jointRegions[i]]);
            }
        }


        for (int i = 0; i < 100; i++) {
            if (jointRegions [i] != 99)
                System.out.println("id: "+i +" <-- "+jointRegions[i]);
        }
        // step 2: fixing wall regions
        for (int i = 1; i < width-1; i++) {
            for (int j = 1; j < height-1; j++) {
                MapNode mn = getMapNode(i, j);
                if (!mn.hasWall) continue;
                getMapNode(i, j).wallRegion = jointRegions[getMapNode(i, j).wallRegion];

                /*getMapNode(i, j).wallRegion = Math.min(getMapNode(i, j).wallRegion, getMapNode(i+1, j).wallRegion);
                getMapNode(i, j).wallRegion = Math.min(getMapNode(i, j).wallRegion, getMapNode(i, j+1).wallRegion);
                getMapNode(i, j).wallRegion = Math.min(getMapNode(i, j).wallRegion, getMapNode(i-1, j+1).wallRegion);
                getMapNode(i, j).wallRegion = Math.min(getMapNode(i, j).wallRegion, getMapNode(i+1, j-1).wallRegion);
                getMapNode(i, j).wallRegion = Math.min(getMapNode(i, j).wallRegion, getMapNode(i+1, j+1).wallRegion);*/
                //if (getMapNode(i+1, j).wallRegion != getMapNode(i, j+1).wallRegion)
                //if (getMapNode(i, j).wallRegion != getMapNode(i+1, j+1).wallRegion)
            }
        }
        System.out.println(getMapNode(1, 1).wallRegion);
    }


    static Vec2d getClosestWallDir(int x, int y, List<MapNode> walls, int width, int height, boolean includeEdges){
        // if (LOGGER) System.out.println(String.format("LOG: getClosestWallDir(%d, %d, wallCount:%d)", x, y, walls.size()));

        /// Calulates DIRECTION to closest wall. 0,0 if no walls.
        if (walls.size() == 0) return new Vec2d(0, 0);
        int minx = walls.get(0).x-x, miny = walls.get(0).y-y;// id's

        for (int i = 0; i < walls.size(); i++) {
            int wx = walls.get(i).x;
            int wy = walls.get(i).y;
            if (!includeEdges && (wx == 0 || wy == 0 || wx == width-1 || wy == height-1))
                continue;
            if (Math.abs((wx-x)) + Math.abs(wy-y) < Math.abs(minx) + Math.abs(miny)){
                minx = wx-x;
                miny = wy-y;
            }
        }
        return new Vec2d( miny, minx);
    }

    public void setMapNode(int x, int y, MapNode value) {
        if (x < width && y < height)
            mapNodes[x][y] = value;
    }

    public boolean getMap(int x, int y){
        //if (LOGGER) System.out.println(String.format("LOG: getMap(%d %d)", x, y));
        if (x < width && y < height)
            return map[x][y];
        if (LOGGER) System.out.println(String.format("LOG: getMap(%d %d) - out of range", x, y, width, height));
        return false;
    }
    public MapNode[][] getMapNodes(){
        return  mapNodes;
    }

    public List<MapNode> getWalls(){
        return walls;
    }

    public MapNode getMapNode(int x, int y) {
        //if (LOGGER) System.out.println(String.format("LOG: getMapNode(%d %d)", x, y));
        if (x < width && y < height)
            return mapNodes[x][y];
        if (LOGGER) System.out.println(String.format("LOG: getMapNode(%d %d) == null, %d %d", x, y, width, height));
        return null;
    }

    public MapNode getMapNodeInt(int x, int y) {
        //if (LOGGER) System.out.println(String.format("LOG: getMapNode(%d %d)", x, y));
        if (x < width && y < height)
            return mapNodes[x][y];
        if (LOGGER) System.out.println(String.format("LOG: getMapNode(%d %d) == null, %d %d", x, y, width, height));
        return null;
    }

    void printDrawWallCount(){
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int nbours = getMapNode(i, j).wallNeighbours;

                if (getMapNode(i, j).hasWall){System.out.print("H"); } // wall
                else if (nbours == 0)System.out.print("."); // 0000 none
                else if (nbours == 1)System.out.print(">"); // 0001 r
                else if (nbours == 2)System.out.print("^"); // 0010 t
                else if (nbours == 3)System.out.print("/"); // 0011 r t
                else if (nbours == 4)System.out.print("<"); // 0100 l
                else if (nbours == 5)System.out.print("-"); // 0101 l r
                else if (nbours == 6)System.out.print("\\"); // 0110 l t
                else if (nbours == 7)System.out.print("T"); // 0111 l t r
                else if (nbours == 8)System.out.print("ˇ"); // 1000 b
                else if (nbours == 9)System.out.print("\\"); // 1001 b r
                else if (nbours == 10)System.out.print("I"); // 1010 b t
                else if (nbours == 11)System.out.print("F"); // 1011 b t r
                else if (nbours == 12)System.out.print("/"); // 1100 b l
                else if (nbours == 13)System.out.print("M"); // 1101 b l r
                else if (nbours == 14)System.out.print("#"); // 1110 b l t
                else if (nbours == 15)System.out.print("O"); // 1111 b l t r
            }
            System.out.println();
        }
    }

    void printDrawMap(){
        boolean[][] b = map;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                    System.out.print(b[i][height-j-1] ? "1" : "0");
            }
            System.out.println();
        }
    }
    void printWallRegionId(){
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < 40 && i <width; i++) {
                System.out.print(getMapNode(i, height-j-1).wallRegion == DEFAULT_REGION ? ".. " : getMapNode(i, height-j-1).wallRegion+" ");
            }
            System.out.println();
        }
    }

}

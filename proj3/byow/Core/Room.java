package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.Random;
import java.util.HashMap;
import java.awt.Rectangle;


public class Room {
    public static final int ROOMMAX = 30;
    public static final int ROOMMIN = 3;

    public static final HashMap coords = new HashMap<Integer, Integer>();
    public static final HashMap walls = new HashMap<Integer, Integer>();
    public static final HashMap floors = new HashMap<Integer, Integer>();


    public static void drawRoom() {
        Random r = new Random(1);
        int width = r.nextInt(ROOMMIN, ROOMMAX);
        int height = r.nextInt(ROOMMIN, ROOMMAX);
        Random r2 = new Random(1);
        int x = r2.nextInt(ROOMMIN, ROOMMAX);
        int y = r2.nextInt(ROOMMIN, ROOMMAX);

        Rectangle room = new Rectangle(x,y,width, height);
        for (int i=x ; i<=width+x;i=+1) {
            for (int iy=y ; iy<=height+y;iy=+1) {
                coords.put(i,iy);
                if (x==width-1 || y==height-1||x==i || y==iy) {
                    walls.put(i, iy);
                }
                else  {
                    floors.put(i,iy);
                }

            }
        }
        System.out.println(coords);

    }



    public static void main(String[] args) {
        Random r = new Random(1);
        int width = r.nextInt(ROOMMIN, ROOMMAX);
        int height = r.nextInt(ROOMMIN, ROOMMAX);




        TERenderer ter = new TERenderer();
        ter.initialize(width, height);

        TETile[][] room = new TETile[width][height];

        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                if (x==width-1 || y==height-1||x==0 || y==0) {
                    room[x][y] = Tileset.WALL;
                }
                else {
                    room[x][y] = Tileset.FLOOR;
                }
            }
        }






        // draws the world to the screen
        ter.renderFrame(room);
    }


}

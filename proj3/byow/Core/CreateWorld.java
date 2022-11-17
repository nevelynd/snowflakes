package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.HashMap;
import java.util.Random;
import java.awt.Rectangle;
public class CreateWorld {



    private static final  String S = "123";
    private  static final int ROOMMAX = 30;
    private static final int ROOMMIN = 3;




    public static void main(String[] args) {
        HashMap coords = new HashMap<Integer, Integer>();
        HashMap walls = new HashMap<Integer, Integer>();
        HashMap floors = new HashMap<Integer, Integer>();
        int width = Engine.WIDTH;
        int height = Engine.HEIGHT;





        TERenderer ter = new TERenderer();
        ter.initialize(width, height);
        TETile[][] world = new TETile[width][height];

        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.FLOWER;
            }
        }

        //determine how many times to generate rooms
        Random r3 = new Random(Integer.parseInt(S));
        int numofrooms = 5;

        while (numofrooms>0) {
            //determine width and height of room
            Random r = new Random();
            int rwidth = r.nextInt(ROOMMIN, 5);
            int rheight = r.nextInt(ROOMMIN, 5);



            //random x,y position to place bottom left corner of room
            Random r2 = new Random();
            int xr = r2.nextInt( ROOMMAX-rwidth);
            int yr = r2.nextInt( ROOMMAX-rheight);





            for (int i=xr ; i<=rwidth+xr;i+=1) {
                for (int iy=yr ; iy<=rheight+yr;iy+=1) {
                    if (coords.containsKey(i) && coords.get(i).equals(iy)) {
                        numofrooms+=1;
                        break;
                    }

                    if (i==rwidth+xr || iy==rheight+yr||xr==i || yr==iy) {
                        world[i][iy] = Tileset.WALL;
                        walls.put(i, iy);
                        coords.put(i, iy);
                    }
                    else  {
                        world[i][iy] = Tileset.FLOOR;
                        floors.put(i, iy);
                        coords.put(i, iy);
                    }

                }
            }
            numofrooms-=1;
        }














        ter.renderFrame(world);





    }



}

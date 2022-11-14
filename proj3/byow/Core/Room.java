package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.Random;

public class Room {
    public static final int ROOMMAX = 30;
    public static final int ROOMMIN = 3;
    public static final Random r = new Random(1);
    public static final int WIDTH = r.nextInt(ROOMMIN, ROOMMAX);
    public static final int HEIGHT = r.nextInt(ROOMMIN, ROOMMAX);




    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(ROOMMAX, ROOMMAX);

        TETile[][] world = new TETile[ROOMMAX][ROOMMAX];
        for (int x = 0; x < ROOMMAX; x += 1) {
            for (int y = 0; y < ROOMMAX; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        for (int x = 0; x < WIDTH; x += 1) {
            int y = 0;
            world[x][y] = Tileset.FLOWER;
        }
        for (int x = 0; x < WIDTH; x += 1) {
            int y = HEIGHT - 1;
            world[x][y] = Tileset.FLOWER;
        }

        for (int y = 0; y < HEIGHT; y += 1) {
            int x = WIDTH -1 ;
            world[x][y] = Tileset.FLOWER;
        }
        for (int y = 0; y < HEIGHT; y += 1) {
            int x = 0;

            world[x][y] = Tileset.FLOWER;
        }




        // draws the world to the screen
        ter.renderFrame(world);
    }


}

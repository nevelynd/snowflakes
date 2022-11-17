package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.Random;

public class Hallway {
    public static final int HALLWAYMAX = 10;
    public static final int HALLWAYMIN = 3;
    public static final Random r = new Random(1);
    public static final int WIDTH = r.nextInt(HALLWAYMIN, HALLWAYMAX);
    public static final int HEIGHT = r.nextInt(HALLWAYMIN, HALLWAYMAX);


    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(HALLWAYMIN, HALLWAYMAX);

        TETile[][] world = new TETile[HALLWAYMIN][HALLWAYMAX];
        for (int x = 0; x < HALLWAYMIN; x += 1) {
            for (int y = 0; y < HALLWAYMIN; y += 1) {
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
            int x = WIDTH - 1;
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


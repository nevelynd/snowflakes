package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.Random;
public class CreateWorld extends Engine {

    static TETile[][] world;
    private static final int width = Engine.WIDTH;
    private static final int height = Engine.HEIGHT;
    private static final String S = "123";



    public static void main(String[] args) {
        world = new TETile[width][height];
        TERenderer ter = new TERenderer();
        ter.initialize(width, height);

        Random r = new Random(Integer.parseInt(S));



        //Integer ni = r.nextInt();



        ter.renderFrame(world);





    }



}

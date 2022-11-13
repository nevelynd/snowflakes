package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.Random;
public class CreateWorld {

    static TETile[][] world = null;
    private static final int X = 10;
    private static final int Y = 10;
    private static final String S = "123";
    public static void main(String[] args) {
        world = new TETile[X][Y];
        TERenderer ter = new TERenderer();
        ter.initialize(X, Y);
        TETile[][] world = new TETile[X][Y];
        Random r = new Random(Integer.parseInt(S));


        //Integer ni = r.nextInt();



        ter.renderFrame(world);





    }



}

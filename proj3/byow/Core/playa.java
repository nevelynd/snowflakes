package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;


import java.util.*;
import java.util.List;

import java.util.HashSet;
import java.util.HashMap;


import static java.lang.Math.abs;

public class playa {

    public static void Player (TETile[][] world, Point startingpos) {
        Point playerposition = startingpos;

        //while (true) {

        //}
    }
    public static void moveplayer (TETile[][] world, Point startingpos, TETile initialtile) {
        int posx = startingpos.x;
        int posy = startingpos.y;

        while (StdDraw.hasNextKeyTyped()) {
            char letter = StdDraw.nextKeyTyped();
            if (letter == 'w' || letter == 'W') {
                world[posx][posy] = initialtile;
                initialtile = world[posx][posy + 1];
                posy = posy + 1;
                world[posx][posy] = Tileset.FLOWER;


            }
        }
    }
}

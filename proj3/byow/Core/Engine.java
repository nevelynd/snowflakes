package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;


import java.util.Random;

import static byow.Core.WorldDemo.makeRooms;
import java.util.List;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {

        /** main menu*/
        StdDraw.setCanvasSize(WIDTH * 16 , HEIGHT * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        Font smallfont = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, 4* HEIGHT / 5, "CS61B: THE GAME");
        StdDraw.show();
        StdDraw.setFont(smallfont);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Load Game (L)");
        StdDraw.show();
        StdDraw.text(WIDTH / 2,  HEIGHT / 2 + 2, "New Game (N)");
        StdDraw.show();
        StdDraw.text(WIDTH / 2,  HEIGHT / 2 - 2, "Quit (Q)");
        StdDraw.show();

        /** array of numbers to identify seed*/
        List<Character> nums = new ArrayList();
        for (int i = 0; i <=9; i++) {
            nums.add((char) i);
        }

        String seed = "";
        int count = 0;
        while (StdDraw.hasNextKeyTyped()) {
            char letter = StdDraw.nextKeyTyped();
            if (letter == 'n' || letter == 'N') {
                StdDraw.clear(Color.BLACK);
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.text(WIDTH / 2,  HEIGHT / 2, "Please enter a random seed");
                StdDraw.show();
            }
            else if (nums.contains(letter)) {
                seed += letter;
            }
            else if (letter == 's' || letter == 'S') {
                break;
            }
        }




    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        Random RANDOM = null;
        String seed = "";


        /** array of numbers to identify seed*/
        List<Character> nums = new ArrayList();
        for (int i = 0; i <=9; i++) {
            nums.add((char) i);
        }
        for (int i = 0; i < input.length(); i++ ) {
            if (nums.contains(input.charAt(i))) {
                seed += input.charAt(i);
            }
        }


        long SEED = Integer.parseInt(seed);
        RANDOM = new Random(SEED);
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        WorldDemo.makeRooms(finalWorldFrame, RANDOM);
        return finalWorldFrame;



    }


}

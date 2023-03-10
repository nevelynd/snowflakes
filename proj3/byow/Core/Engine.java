package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.temporal.ValueRange;
public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH =  90;
    public static final int HEIGHT = 45;
    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        StdDraw.setCanvasSize(WIDTH * 16 , HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);

        /** main menu*/
        mainmenu();
        int avatar = 0;
        boolean seedstart = false;



        String[] s = new String[2];
        s[0] = "false";




        while (!StdDraw.hasNextKeyTyped()) {
        StdDraw.pause(1000);
        }


        String seed = "";
        boolean spressed = false;
        while (!spressed   && s[0] !="true") {
            if (StdDraw.hasNextKeyTyped() ) {

            char letter = StdDraw.nextKeyTyped();
            if (letter == 'c' || letter == 'C') {
                avatar = changeavatar();
                mainmenu();


            }

            if (letter == 's' || letter == 'S') {
                break;
            }
            if (letter == 'n' || letter == 'N') {
                StdDraw.clear(Color.BLACK);
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.text(WIDTH / 2,  HEIGHT / 2, "Please enter a random seed");
                StdDraw.show();
                seedstart = true;

            }
            if (letter == 'l' || letter == 'L') {
                s[0] = "true";
            }

            if (letter == 'r' || letter == 'R') {
                s[0] = "replay";
            }
            if (seedstart) {
            seed += letter; }
            }



        }

        if (!s[0].equals("true") && !s[0].equals("replay")) {
            s[0]= (seed);
        }
        s[1] = String.valueOf(avatar);
        WorldDemo.main(s);

    }

    public void mainmenu() {

        Font font = new Font("Monaco", Font.BOLD, 30);
        Font smallfont = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(font);

        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, 4* HEIGHT / 5, "CS61B: THE GAME");
        StdDraw.show();
        StdDraw.setFont(smallfont);
        StdDraw.text(WIDTH / 2,  HEIGHT / 2 + 2, "New Game (N)");
        StdDraw.show();
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 1, "Load Game (L)");
        StdDraw.show();
        StdDraw.text(WIDTH / 2,  HEIGHT / 2 , "Quit (Q)");
        StdDraw.show();
        StdDraw.text(WIDTH / 2,  HEIGHT / 2 - 1, "Replay Game (R)");
        StdDraw.show();
        StdDraw.text(WIDTH / 2,  HEIGHT / 2 - 2, "Change Avatar (C)");
        StdDraw.show();
    }
    public int changeavatar() {
        boolean bpressed = false;
        int selected = 0;
        Font font = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font);

        StdDraw.enableDoubleBuffering();
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);


        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 12, "current avatar:");

        StdDraw.show();
        int avatar = loadavatar();
        if (avatar == '0') {
            StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "@");
        }
        if (avatar == '1') {
            StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "???");
        }
        if (avatar == '2') {
            StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "???");
        }
        if (avatar == '3') {
            StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "???");
        }
        if (avatar == '4') {
            StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "???");
        }
        if (avatar == '5') {
            StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "???");
        }
        StdDraw.show();


        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 4, "(1) ??? curly heart ???");
        StdDraw.show();
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 2, "(2) ??? star ???");
        StdDraw.show();
        StdDraw.text(WIDTH / 2, HEIGHT / 2 , "(3) ??? smiley ???");
        StdDraw.show();
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, "(4) ??? crown ???");
        StdDraw.show();
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 4, "(5) ??? yinyang ???");
        StdDraw.show();
        StdDraw.text(WIDTH / 2, 2, "Back (B)");
        StdDraw.show();


        while (!bpressed) {
            if (StdDraw.hasNextKeyTyped() ) {

                char letter = StdDraw.nextKeyTyped();

                if (letter == 'b' || letter == 'B') {
                    bpressed = true;
                    break;

                }
                selected = Integer.parseInt(String.valueOf(letter));
                StdDraw.setPenColor(Color.BLACK);
                StdDraw.filledRectangle(WIDTH / 2, HEIGHT / 2 + 10, 2, 1);
                StdDraw.setPenColor(Color.WHITE);
                if (letter == '0') {
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "@");
                }
                if (letter == '1') {
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "???");
                }
                if (letter == '2') {
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "???");
                }
                if (letter == '3') {
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "???");
                }
                if (letter == '4') {
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "???");
                }
                if (letter == '5') {
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "???");
                }
                StdDraw.show();

            }
        }
        return selected;
    }


    public char loadavatar() {
        char avatar = '0';
        try {
            BufferedReader games = new BufferedReader(new FileReader("save.txt"));
            avatar = games.readLine().charAt(0);
        } catch (Exception e) {
            System.out.println("please play a game to load first");
        }
        return avatar;

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
        String[] s = new String[2];
        ValueRange vr = ValueRange.of(0, 6);
        String newinput = "";
        int i ;
        s[0] = (input);
        s[1] = String.valueOf(0);
        //"c12b5bn123ssddwsa:q"

        //"c4bc2blwwd:q"
        if (input.charAt(0) == 'c' || input.charAt(0) == 'C') {
            for (i = 1; i < input.length(); i++) {
                if (input.charAt(i) =='1' ||input.charAt(i) =='2' || input.charAt(i) =='3' ||input.charAt(i) =='4' ||input.charAt(i) =='5') {
                    s[1] = String.valueOf(input.charAt(i));
                }
                if (input.charAt(i) =='B' ||input.charAt(i) =='b') {
                    if (input.length() > i+1 && (input.charAt(i+1) == 'c' || input.charAt(1+i) == 'C') ){
                        continue;
                    }
                    else {
                        break;
                    }
                }


            }
            for (int j = i + 1; j < input.length(); j++) {
                newinput += input.charAt(j);


            }
            s[0] = newinput;



        }
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        WorldDemo.main(s);
        return finalWorldFrame;
    }


}

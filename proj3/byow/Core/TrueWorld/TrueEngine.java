package byow.Core.TrueWorld;

import byow.Core.WorldDemo;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.time.temporal.ValueRange;
import java.util.Random;

public class TrueEngine {

    public static final int WIDTH = 90;
    public static final int HEIGHT = 45;

    public void interactWithKeyboard(Menu menu, int avatar) {
        String[] s = new String[3];
        s[0] = String.valueOf(false);
        String seed = "";
        Object[] addTo = new Object[2];
        boolean sPress = false;
        while (!sPress && s[0] != String.valueOf(true)) {
            if (StdDraw.hasNextKeyTyped()) {
                char letter = StdDraw.nextKeyTyped();
                if (letter == 'c' || letter == 'C') {
                    avatar = menu.changeAvatar();
                    menu.mainMenu(this, avatar);
                    return;
                }
                if (letter == 's' || letter == 'S') {
                    break;
                }
                if (letter == 'n' || letter == 'N') {
                    addTo = menu.seedMenu();
                    s[2] = String.valueOf(addTo[1]);
                    seed += letter;
                    break;
                }
                if (letter == 'l' || letter == 'L') {
                    TrueDemo myWorld = new TrueDemo();
                    myWorld.load();
                    return;
                }
                if (letter == 'r' || letter == 'R') {
                    TrueDemo myWorld = new TrueDemo();
                    myWorld.replay();
                    return;
                }
            }
        }
        seed += addTo[0];
        if (!s[0].equals("true") && !s[0].equals("replay")) {
            s[0] = (seed);
        }
        s[1] = String.valueOf(avatar);
        TrueDemo myWorld = new TrueDemo();
        myWorld.TrueDemo(s);
    }

    public TETile[][] interactWithInputString(String input) {
        int avatar = 0;
        String seed = "";
        String remaining = "";
        if (input.charAt(0) == 'n' || input.charAt(0) == 'N') {
            for (int i = 1; i < input.length(); i++) {
                char current = input.charAt(i);
                if (Character.isDigit(current)) {
                    seed += current;
                } else {
                    remaining += current;
                }
            }
        }
        TETile[][] myWorld = new TETile[WIDTH][HEIGHT];
        TrueDemo myDemo = new TrueDemo();
        myDemo.continueTrueDemo(seed, remaining, input, avatar, myWorld, false);
        return myWorld;
    }

    public TETile[][] interactWithInputString(String input, int avatar) {
        String seed = "";
        String remaining = "";
        if (input.charAt(0) == 'n' || input.charAt(0) == 'N') {
            for (int i = 1; i < input.length(); i++) {
                char current = input.charAt(i);
                if (Character.isDigit(current)) {
                    seed += current;
                } else {
                    remaining += current;
                }
            }
        }
        TETile[][] myWorld = new TETile[WIDTH][HEIGHT];
        TrueDemo myDemo = new TrueDemo();
        myDemo.continueTrueDemo(seed, remaining, input, avatar, myWorld, false);
        return myWorld;
    }

    public TETile[][] interactWithInputString(String input, int avatar, Boolean replay) {
        String seed = "";
        String remaining = "";
        if (input.charAt(0) == 'n' || input.charAt(0) == 'N') {
            for (int i = 1; i < input.length(); i++) {
                char current = input.charAt(i);
                if (Character.isDigit(current)) {
                    seed += current;
                } else {
                    remaining += current;
                }
            }
        }
        TETile[][] myWorld = new TETile[WIDTH][HEIGHT];
        TrueDemo myDemo = new TrueDemo();
        myDemo.continueTrueDemo(seed, remaining, input, avatar, myWorld, replay);
        return myWorld;
    }


}
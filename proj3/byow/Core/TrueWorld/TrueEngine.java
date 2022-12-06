package byow.Core.TrueWorld;

import byow.Core.WorldDemo;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.time.temporal.ValueRange;
import java.util.Random;

public class TrueEngine {

    public static final int WIDTH =  90;
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




    public void initializeWorld(TrueEngine engine) {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        mainMenu(engine, 0);
    }

    public void mainMenu(TrueEngine engine, int avatar) {
        Font font = new Font("Monaco", Font.BOLD, 30);
        Font smallFont = new Font("Monaco", Font.BOLD, 15);
        Font tinyFont = new Font("Monaco", Font.BOLD, 7);
        StdDraw.setFont(font);

        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, 4* HEIGHT / 5, "CS61B: THE GAME");
        StdDraw.setFont(tinyFont);
        StdDraw.text(5, 1, "Credits: Evelyn Ho, Carlos Rodriguez");
        StdDraw.setFont(smallFont);
        StdDraw.text(WIDTH / 2,  HEIGHT / 2 + 2, "New Game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 1, "Load Game (L)");
        StdDraw.text(WIDTH / 2,  HEIGHT / 2 , "Quit (Q)");
        StdDraw.text(WIDTH / 2,  HEIGHT / 2 - 1, "Replay Game (R)");
        StdDraw.text(WIDTH / 2,  HEIGHT / 2 - 2, "Change Avatar (C)");
        StdDraw.show();
        //engine.interactWithKeyboard(this, avatar);
    }

    public Object[] seedMenu() {
        String seed = "";
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2,  HEIGHT / 2 + 10, "Please enter a random seed:");
        StdDraw.show();

        boolean sPress = false;
        while (!sPress) {
            if (StdDraw.hasNextKeyTyped()) {
                char num = StdDraw.nextKeyTyped();
                Boolean flag = Character.isDigit(num);
                if (flag) {
                    seed += num;
                }
                if (num == 's' || num == 'S' || seed.length() >= 18|| Long.valueOf(seed) * 10 + 9 > Long.MAX_VALUE || Long.valueOf(seed) == Long.MAX_VALUE) {
                    break;
                }
                StdDraw.clear(Color.black);
                StdDraw.text(WIDTH / 2,  HEIGHT / 2 + 10, "Please enter a random seed:");
                StdDraw.text(WIDTH / 2,  HEIGHT / 2 + 7, seed);
                StdDraw.show();
            }
        }
        long trueSeed = Long.valueOf(seed);
        seed += 's';
        Object[] seeds = new Object[2];
        seeds[0] = seed;
        seeds[1] = trueSeed;
        return seeds;
    }

    public int changeAvatar(){
        int selected = 0;
        Font font = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.enableDoubleBuffering();
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 12, "Current avatar:");
        StdDraw.show();
        int avatar = 0;
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "@");
        StdDraw.show();
        switch(avatar) {
            case 0:
                StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "@"); break;
            case 1:
                StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "ღ"); break;
            case 2:
                StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "✶"); break;
            case 3:
                StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "ツ"); break;
            case 4:
                StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "♕"); break;
            case 5:
                StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "☯"); break;

        }
        StdDraw.show();
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 4, "(1) ღ Curly Heart ღ");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 2, "(2) ✶ Star ✶");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 , "(3) ツ Smiley ツ");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, "(4) ♕ Crown ♕");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 4, "(5) ☯ Yin-Yang ☯");
        StdDraw.text(WIDTH / 2, 2, "Back (B)");
        StdDraw.show();
        while (true) {
            if (StdDraw.hasNextKeyTyped() ) {
                char letter = StdDraw.nextKeyTyped();
                if (letter == 'b' || letter == 'B') {
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
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "ღ");
                }
                if (letter == '2') {
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "✶");
                }
                if (letter == '3') {
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "ツ");
                }
                if (letter == '4') {
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "♕");
                }
                if (letter == '5') {
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "☯");
                }
                StdDraw.show();
            }
        }
        return selected;
    }

}


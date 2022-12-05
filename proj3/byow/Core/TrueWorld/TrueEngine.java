package byow.Core.TrueWorld;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.Random;

public class TrueEngine {
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
                    s[0] = String.valueOf(true);
                }
                if (letter == 'r' || letter == 'R') {
                    s[0] = "replay";
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
    }

package byow.Core.TrueWorld;

import byow.Core.Engine;

public class TrueMain {
    public static void main(String[] args) {
        TrueEngine engine = new TrueEngine();
        Menu menu = new Menu();
        menu.initializeWorld(engine);
    }
}
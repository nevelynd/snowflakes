package byow.Core;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.FileInputStream;


public class LoadandSave {
    long seed;
    public LoadandSave(long seed) {
        this.seed = seed;
    }
    public static void save() {
        try {
            ObjectOutputStream games = new ObjectOutputStream(new FileOutputStream(new File("save.dat")));
            Data ds = new Data();
            ds.health = 0 ;
            ds.score = 0;
            ds.playerx = 0;
            ds.playery = 0;
            games.writeObject(ds);

        }
        catch (Exception e) {
            System.out.println("please play a game to save first");
        }

    }

    public static void load() {
        try {
            ObjectInputStream loadgames = new ObjectInputStream(new FileInputStream(new File("save.dat")));
            Data ds = (Data) loadgames.readObject();
            ds.health = 0;
            ds.score = 0;
            ds.playerx = 0;
            ds.playery = 0;

        }
        catch (Exception e) {
            System.out.println("please play a game to load first");
        }

    }
}

package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.List;

import java.util.HashSet;
import java.util.HashMap;


import static java.lang.Math.abs;
import static java.lang.Math.random;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class WorldDemo {
    public static final int WIDTH =  90;
    public static final int HEIGHT = 45;

    /** Max width/height integer for a room.*/
    public static final int ROOM_MAX = 10;

    /** Minimum width/height integer for a room.*/
    public static final int ROOM_MIN = 6;
    private static final List<Rectangle> rooms = new ArrayList<>();
    //public int health;
    public int posx;
    public int posy;
    public Boolean gameOver;
    public long randomseed;
    public int avatar;
    int numofsnowflakes;




    public static void makeRooms(TETile[][] tiles, Random random) {
        TETile[][] myWorldDemo;

        //public static final long seed = Engine.;

        /** Not entirely sure why I created these.*/
        HashMap<Rectangle, Point> points = new HashMap<>();

        HashMap<Rectangle, Integer> pointsx = new HashMap<>();
        HashMap<Rectangle, Integer> pointsy = new HashMap<>();
        Set<Rectangle> connectedrectangles = new HashSet<>();


        /** Set the background as a series of empty tiles.*/
        Random rr = new Random(123);

        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT ; y += 1) {
                int worldlayout = rr.nextInt(10);
                if (y == HEIGHT -1) {
                    tiles[x][y] = Tileset.NOTHING;
                } else {
                    if (worldlayout == 1 || worldlayout == 6 || worldlayout == 9 || worldlayout == 8) {
                        tiles[x][y] = Tileset.GRASS;
                    }
                    if (worldlayout == 2) {
                        tiles[x][y] = Tileset.MOUNTAIN;
                    }
                    if (worldlayout == 3) {
                        tiles[x][y] = Tileset.TREE;
                    }
                    if (worldlayout == 4 || worldlayout == 7) {
                        tiles[x][y] = Tileset.SAND;
                    }
                    if (worldlayout == 5 || worldlayout == 0) {
                        tiles[x][y] = Tileset.WATER;
                    }
                }
            }
        }

        /** Initialize variables.*/
        int xs;
        int ys;
        int room_width;
        int room_height;
        Rectangle ra = null;
        Point door = null;

        int maxRooms = random.nextInt(7,15);


        for(int i = 0; i < maxRooms; i++) {
            cont:
                while (true) {
                    /** Set a random origin for the rectangle, these are the variables xs and ys.*/


                    room_height = random.nextInt(ROOM_MIN, ROOM_MAX + 1);
                    room_width = random.nextInt(ROOM_MIN, ROOM_MAX + 1);
                    xs = random.nextInt(0, WIDTH );
                    ys = random.nextInt(0, HEIGHT  - room_height);

                   // Point p = new Point(xs, ys);
                    /**rectangle takes in point  from  upper left*/
                    ra = new Rectangle(xs, ys + room_height, room_width, room_height);

                    /** If the rectangle intersects with another rectangle within our known list, then
                     * we must generate a new rectangle.*/
                    if (ra.x + room_width > WIDTH   || ra.y  > HEIGHT - 1) {
                        continue ;
                    }

                    for (Rectangle rb : rooms) {
                        /** If the room that is about to be created does not meet the specified requirements,
                         * then we cycle through the while statement again. Thus, generating a new room.
                         * added some stuff for overlapping borders*/

                        if (ra.intersects(rb) || rb.intersects(ra) || rb.contains(ra) || rb.contains(ra) ||
                                ra.y - 1  == rb.y - rb.height  || ra.y - ra.height  == rb.y - 1
                                || ra.x +ra.width == rb.x -1 || ra.x -1  == rb.x + rb.width ||
                                ra.y   == rb.y - rb.height  || ra.y - ra.height  == rb.y
                                || ra.x +ra.width == rb.x  || ra.x   == rb.x + rb.width
                                //|| rb.contains(ra.x, ra.y) || rb.contains(ra.x + ra.width, ra.y) || ra.contains(rb.x, rb.y) || ra.contains(rb.x + rb.width, rb.y)
                                //|| rb.contains(ra.x, ra.y - ra.height) || rb.contains(ra.x + ra.width, ra.y - ra.height) || ra.contains(rb.x, rb.y - rb.height) || ra.contains(rb.x + rb.width, rb.y - rb.height)

                                 ) {
                            continue cont;
                        }
                    }




                    break;
            }
            /** Add valid rectangle to array.*/

            rooms.add(ra);
            if (door == null || (door.x < ra.x || door.y < ra.y) ) {
                door = new Point(ra.x + ra.width/2, ra.y - 1);
            }


            Point p = new Point(ra.x + room_width / 2 + 1, ra.y - ra.height / 2 - 1 );
            points.put(ra, p);
            pointsx.put(ra, p.x);
            pointsy.put(ra, p.y);

            /** Generate tiles */
            for (int x = xs; x < room_width + xs; x += 1) {
                for (int y = ys; y < room_height + ys; y += 1) {
                    if (x == xs || y == ys  || x == (xs + room_width -1) || y == (ys + room_height - 1)) {
                        tiles[x][y] = Tileset.WALL;
                    } else {
                        tiles[x][y] = Tileset.FLOOR;
                    }
                }
            }

        }

        //Rectangle bound = new Rectangle(minx, maxy, maxx - minx, maxy - miny );
        for (int r = 0; r<rooms.size() ; r++) {
            Rectangle rrr = rooms.get(r);
            if (r == rooms.size() - 1) {
                createLinearPathe(tiles, points.get(rrr).x, points.get(rrr).y, points.get(rooms.get(0)).x, points.get(rooms.get(0)).y, random);
                //createLinearPath(tiles, rrr.x, rrr.y, (rooms.get(0)).x, (rooms.get(0)).y, random);

            } else {
                //createLinearPath(tiles, rrr.x, rrr.y, (rooms.get(r+1)).x, (rooms.get(r+1)).y, random);
                createLinearPathe(tiles, points.get(rrr).x, points.get(rrr).y, points.get(rooms.get(r+1)).x, points.get(rooms.get(r+1)).y, random);

            }


        }


/**
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                if (tiles[x][y] == Tileset.FLOOR && ((tiles[x][y + 1] != Tileset.FLOOR && tiles[x][y + 1] != Tileset.WALL)
                        ||(tiles[x + 1][y] != Tileset.FLOOR && tiles[x + 1][y] != Tileset.WALL) ||
                        (tiles[x][y - 1] != Tileset.FLOOR && tiles[x][y - 1] != Tileset.WALL) ||
                        (tiles[x - 1][y] != Tileset.FLOOR && tiles[x - 1][y] != Tileset.WALL))) {
                    tiles[x][y] = Tileset.WALL;

                }
            }
        }*/


        int doorx = door.x;
        int doory = door.y;
        //tiles[doorx][doory] = Tileset.FLOWER;
        //TETile initialtile = tiles[doorx][doory];
        //Player.createplayer(5, doorx, doory);




        /** I was attempting to see if I could find a very simple way of connecting rooms, and the following code does
         * an okay job at creating connections. For each rectangle that we define as a room, we take a random rectangle out
         * of our array and connect the two. Thus, every room will have a connection. Very stupid, I know! Uncomment and run main
         * if you'd like to see the result. Must fix edge cases, there are no walls around some corners. This is an error within
         * the linearPath method.*/
/**
         int index = random.nextInt(0, rooms.size() );
         for (Rectangle rec : rooms) {
           createLinearPath(tiles, (int) rec.getCenterX(), (int) rec.getCenterY(),
                    (int) rooms.get(index).getCenterX(), (int) rooms.get(index).getCenterY(), random);
         }

        /** If you'd like to see what createLinearPath generates given parameters, try uncommenting the following examples:*/

         //createLinearPath(tiles, 20, 10, 50, 10, random);
         //createLinearPath(tiles, 20, 5, 50, 20, random);

    }
    /** Generation of a linear path, that takes in start and end values. Very predictable. I've looked through several algorithms
     * and most path generations are disgustingly similar with regard to the use of brute force. It's really nasty, and I'm working
     * diligently to create something more elegant.*/
    public static void createLinearPath(TETile[][] tiles, int fromX, int fromY, int destinationX, int destinationY, Random random) {
        if (fromX > destinationX) {
            for (int x = fromX; x != destinationX; x--) {
                for (int count = -1; count != 2; count++) {
                    if (fromY+count < HEIGHT || fromY+count > 0) {
                        if (tiles[x][fromY+count] != Tileset.FLOOR) {
                            tiles[x][fromY+count] = Tileset.WALL;
                        }
                    }
                }
                tiles[x][fromY] = Tileset.FLOOR;
            }
        } else {
            for (int x = fromX; x != destinationX; x++) {
                for (int count = -1; count != 2; count++) {
                    if (fromY+count < HEIGHT || fromY+count > 0) {
                        if (tiles[x][fromY+count] != Tileset.FLOOR) {
                            tiles[x][fromY+count] = Tileset.WALL;
                        }
                    }
                }
                tiles[x][fromY] = Tileset.FLOOR;
            }
        }
        if (fromY > destinationY) {
            for (int y = fromY; y != destinationY; y--) {
                for (int count = -1; count != 2; count++) {
                    if (fromX+count < WIDTH || fromX+count > 0) {
                        if (tiles[fromX+count][y] != Tileset.FLOOR) {
                            tiles[fromX+count][y] = Tileset.WALL;
                        }
                    }
                }
                tiles[fromX][y] = Tileset.FLOOR;
            }
        } else {
            for (int y = fromY; y != destinationY; y++) {
                for (int count = -1; count != 2; count++) {
                    if (fromX+count < WIDTH || fromX+count > 0) {
                        if (tiles[fromX+count][y] != Tileset.FLOOR) {
                            tiles[fromX+count][y] = Tileset.WALL;
                        }
                    }
                }
                tiles[fromX][y] = Tileset.FLOOR;
            }
        }

    }

    public static void createLinearPathe(TETile[][] tiles, int fromX, int fromY, int destinationX, int destinationY, Random rndom) {
        if (fromX > destinationX) {
            for (int x = fromX; x >= destinationX; x--) {
                for (int count = -1; count != 2; count++) {
                    if (fromY + count < HEIGHT && fromY + count > 0) {
                        if (tiles[x][fromY + count] != Tileset.FLOOR) {
                            tiles[x][fromY + count] = Tileset.WALL;
                        }
                    }
                }





                tiles[x][fromY] = Tileset.FLOOR;
            }
        } else {
            for (int x = fromX; x <= destinationX; x++) {
                for (int count = -1; count != 2; count++) {
                    if (fromY+count < HEIGHT && fromY+count > 0) {
                        if (tiles[x][fromY+count] != Tileset.FLOOR) {
                            tiles[x][fromY+count] = Tileset.WALL;
                        }
                    }
                }
                tiles[x][fromY] = Tileset.FLOOR;
            }
        }

        if (fromY > destinationY) {
            for (int y = fromY; y >= destinationY ; y--) {
                for (int count = -1; count != 2; count++) {
                    if (destinationX+count < WIDTH && destinationX+count > 0) {
                        if (tiles[destinationX+count][y] != Tileset.FLOOR) {
                            tiles[destinationX+count][y] = Tileset.WALL;
                        }
                    }
                }

                tiles[destinationX][y] = Tileset.FLOOR;
            }
        } else {
            for (int y = fromY; y<= destinationY ; y++) {
                for (int count = -1; count != 2; count++) {
                    if (destinationX+count < WIDTH && destinationX+count > 0) {
                        if (tiles[destinationX+count][y] != Tileset.FLOOR) {
                            tiles[destinationX+count][y] = Tileset.WALL;
                        }
                    }
                }
                tiles[destinationX][y] = Tileset.FLOOR;
            }
        }

    }

    public void randomizedsnow(TETile[][] tiles, Random r) {


        for (int i = 0; i < numofsnowflakes; i++) {

            int x = r.nextInt(WIDTH);
            int y = r.nextInt(HEIGHT);
            while (tiles[x][y] != Tileset.FLOOR || (x ==posx && y ==posy)) {
                 x = r.nextInt(WIDTH);
                 y = r.nextInt(HEIGHT);
            }
            tiles[x][y] = Tileset.SNOWFLAKE;




        }


    }





    /** Started creating a path method that does not generate a boring pathQ*/
    public static void createPath(TETile[][] tiles, int fromX, int fromY, int destinationX, int destinationY, Random random) {
        int distanceX = abs(destinationX - fromX);
        int pathLength;
        int coinFlip;
    }
    public  void displayHUD(TETile[][] world, String HUD, int keysleft) {
        int r = 0;
        for (char i : "keys left:".toCharArray()) {
            TETile I = new TETile(i, Color.white, Color.black,
                    "i");
            world[r][HEIGHT - 1] = I;
            r+=1;
        }
        for (int i = r; i <=numofsnowflakes + r ; i++) {
            if (keysleft>0) {
                world[i][HEIGHT - 1] = Tileset.SNOWFLAKE;
                keysleft -=1;

            }  else {
                world[i][HEIGHT - 1] = Tileset.NOTHING;


            }



        }

    }






    public WorldDemo(long SEED, Random RANDOM,char[] smarray, String pressed, int avatar) {

        TETile[][] myWorldDemo = new TETile[WIDTH][HEIGHT];



        gameOver = false;
        int keysleft;

        System.out.println(pressed);
        System.out.println(pressed.equals("true"));
        System.out.println(pressed.equals("replay"));




        if (pressed.equals("true")) {
            load();

            Random r = new Random(randomseed);
            makeRooms(myWorldDemo, r);
            avatar = this.avatar;
            randomizedsnow(myWorldDemo, r);



        }
        else if (pressed.equals("replay") ) {
            replay();
            Random r = new Random(randomseed);
            makeRooms(myWorldDemo, r);
            avatar = this.avatar;
            randomizedsnow(myWorldDemo, r);

        }
        else  {
            randomseed = SEED;
            makeRooms(myWorldDemo, RANDOM);

            playersetup(RANDOM, myWorldDemo);
            this.avatar = avatar;

            randomizedsnow(myWorldDemo, RANDOM);


        }


//meep

        TETile initialtile = myWorldDemo[posx][posy];
        TETile player = Tileset.AVATAR;
        if (avatar == 1) {
            player = Tileset.curlyheart;

        }
        if (avatar == 2) {
            player = Tileset.star;


        }
        if (avatar == 3) {
            player = Tileset.smiley;


        }

        if (avatar == 4) {
            player = Tileset.crown;


        }
        if (avatar == 5) {
            player = Tileset.yinyang;


        }

        myWorldDemo[posx][posy] = player;
        keysleft = numofsnowflakes;
        String HUD = "keys left:";
        displayHUD(myWorldDemo, HUD, keysleft);



        int sm = 0;




        if (smarray !=null && smarray.length>sm) {
            TERenderer ter = new TERenderer();
            ter.initialize(WIDTH, HEIGHT);
            ter.renderFrame(myWorldDemo);
            while (!gameOver && smarray.length>sm) {

                char stringletter = smarray[sm];
                sm+=1;

                /** previous tile setting before move*/
                myWorldDemo[posx][posy] = initialtile;

                /** quits and saves*/
                if (stringletter == ':') {

                    stringletter = smarray[sm];
                    if (stringletter == 'Q' || stringletter == 'q') {
                        gameOver = true;

                    }
                    save();
                }

                /** moves avatar up if allowed*/
                if (stringletter == 'w' || stringletter == 'W') {
                    if (posy+1<=HEIGHT -1 && myWorldDemo[posx][posy + 1] != Tileset.WALL ) {
                        initialtile = myWorldDemo[posx][posy + 1];
                        posy = posy + 1;
                    }

                }

                /** moves avatar down if allowed*/
                if (stringletter == 's' || stringletter == 'S') {
                    if (posy-1>=0 && myWorldDemo[posx][posy - 1] != Tileset.WALL ) {
                        initialtile = myWorldDemo[posx][posy - 1];
                        posy = posy - 1;
                    }
                }

                /** moves avatar left if allowed*/
                if (stringletter == 'a' || stringletter == 'A') {
                    if (posx-1>=0 && myWorldDemo[posx - 1][posy] != Tileset.WALL ) {
                        initialtile = myWorldDemo[posx - 1][posy];
                        posx = posx - 1;
                    }
                }

                /** moves avatar right if allowed*/
                if (stringletter == 'd' || stringletter == 'D') {
                    if (posx+1<=WIDTH - 1 && myWorldDemo[posx + 1][posy] != Tileset.WALL ) {
                        initialtile = myWorldDemo[posx + 1][posy];
                        posx = posx + 1;
                    }

                }


                myWorldDemo[posx][posy] = player;
                if (initialtile == Tileset.SNOWFLAKE) {
                    keysleft-=1;
                    initialtile = Tileset.FLOOR;


                }


                if (numofsnowflakes <=0) {
                    gameOver = true;
                }
                displayHUD(myWorldDemo, HUD,keysleft);

                ter.renderFrame(myWorldDemo);


            }
            if (gameOver) {
                System.exit(0);
            }
        }
        else {
            TERenderer ter = new TERenderer();
            ter.initialize(WIDTH, HEIGHT);
            ter.renderFrame(myWorldDemo);
            while (!gameOver) {


                while (StdDraw.hasNextKeyTyped()) {
                    /** user input for where to move next*/
                    char letter = StdDraw.nextKeyTyped();


                    /** previous tile setting before move*/
                    myWorldDemo[posx][posy] = initialtile;

                    /** quits and saves*/
                    if (letter == ':') {
                        while (!StdDraw.hasNextKeyTyped()) {
                            StdDraw.pause(1000); }
                        char nextletter = StdDraw.nextKeyTyped();
                        if (nextletter == 'Q' || nextletter == 'q') {
                            gameOver = true;

                        }
                        save();

                    }

                    /** moves avatar up if allowed*/
                    if (letter == 'w' || letter == 'W') {
                        if (posy + 1 <= HEIGHT - 1 && myWorldDemo[posx][posy + 1] != Tileset.WALL) {
                            initialtile = myWorldDemo[posx][posy + 1];
                            posy = posy + 1;
                        }

                    }

                    /** moves avatar down if allowed*/
                    if (letter == 's' || letter == 'S') {
                        if (posy - 1 >= 0 && myWorldDemo[posx][posy - 1] != Tileset.WALL) {
                            initialtile = myWorldDemo[posx][posy - 1];
                            posy = posy - 1;
                        }
                    }

                    /** moves avatar left if allowed*/
                    if (letter == 'a' || letter == 'A') {
                        if (posx - 1 >= 0 && myWorldDemo[posx - 1][posy] != Tileset.WALL) {
                            initialtile = myWorldDemo[posx - 1][posy];
                            posx = posx - 1;
                        }
                    }

                    /** moves avatar right if allowed*/
                    if (letter == 'd' || letter == 'D') {

                        if (posx + 1 <= WIDTH - 1 && myWorldDemo[posx + 1][posy] != Tileset.WALL) {
                            initialtile = myWorldDemo[posx + 1][posy];
                            posx = posx + 1;
                        }

                    }


                    myWorldDemo[posx][posy] = player;
                    if (initialtile == Tileset.SNOWFLAKE) {
                        keysleft-=1;
                        initialtile = Tileset.FLOOR;


                    }
                    if (numofsnowflakes ==0) {
                        gameOver = true;
                    }

                }
                displayHUD(myWorldDemo, HUD, keysleft);
                ter.renderFrame(myWorldDemo);


            }
            if (gameOver) {
                System.exit(0);
            }


        }
        }








    public void playersetup(Random RANDOM, TETile[][] myWorldDemo) {
        numofsnowflakes = RANDOM.nextInt(10,20);
        posx = RANDOM.nextInt(80);
        posy = RANDOM.nextInt(29);
        while (myWorldDemo[posx][posy] != Tileset.FLOOR) {
            posx = RANDOM.nextInt(80);
            posy = RANDOM.nextInt(29);

        }
    }


    public void save() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("save.txt"));
            bw.write(""+avatar);
            bw.newLine();

            bw.write(""+numofsnowflakes);
            bw.newLine();
            bw.write(""+posx);
            bw.newLine();
            bw.write(""+posy);
            bw.newLine();
            bw.write(""+randomseed);

            bw.close();

            BufferedWriter br = new BufferedWriter(new FileWriter("replay.txt"));
            br.write(""+avatar);
            br.newLine();

            br.write(""+numofsnowflakes);
            br.newLine();
            br.write(""+posx);
            br.newLine();
            br.write(""+posy);
            br.newLine();
            br.write(""+randomseed);

            br.close();


        }
        catch (Exception e) {
            System.out.println("please play a game to save first");
        }

    }

    public void load() {
        try {
            BufferedReader games = new BufferedReader(new FileReader("save.txt"));
            avatar = Integer.parseInt(games.readLine());

            numofsnowflakes = Integer.parseInt(games.readLine());
            posx = Integer.parseInt(games.readLine());
            posy = Integer.parseInt(games.readLine());
            randomseed = Integer.parseInt(games.readLine());

        }
        catch (Exception e) {
            System.out.println("please play a game to load first");
        }

    }

    public void replay() {
        try {
            BufferedReader games = new BufferedReader(new FileReader("replay.txt"));
            avatar = Integer.parseInt(games.readLine());

            numofsnowflakes = Integer.parseInt(games.readLine());
            posx = Integer.parseInt(games.readLine());
            posy = Integer.parseInt(games.readLine());
            randomseed = Integer.parseInt(games.readLine());

        }
        catch (Exception e) {
            System.out.println("please play a game to replay first");
        }

    }





    public static void main(String[] args) {

        //TETile[][] myWorldDemo = new TETile[WIDTH][HEIGHT];

        int avatar = Integer.parseInt(args[1]);
        String seed = "";
        String input = args[0];
        long SEED = 0;
        Random RANDOM = null;
        char[] smarray = null;
        System.out.println(input);

        if(input.equals("true") || input.equals("replay")) {

            new WorldDemo(0 , null, null, input, avatar);
        }

        else {
            int i;

            for (i = 0; i < input.length(); i++) {
                if (input.charAt(i) == 'n' || input.charAt(i) == 'N') {
                    continue;
                }

                if (input.charAt(i) == 'l' || input.charAt(i) == 'L' ||
                        input.charAt(i) == 's' || input.charAt(i) == 'S' ||
                        input.charAt(i) == 'r' || input.charAt(i) == 'R'

                ) {
                    break;

                }

                if (input.charAt(i) == ':' && (input.charAt(i + 1) == 'q' || input.charAt(i + 1) == 'Q')) {
                    System.exit(0);
                }


                seed += input.charAt(i);
            }

                String stringmovement = "";
                for (int j = i + 1; j < input.length(); j++) {
                    stringmovement += input.charAt(j);
                }
                smarray = stringmovement.toCharArray();

                if (input.charAt(i) == 'l' || input.charAt(i) == 'L' ||
                        input.charAt(i) == 'r' || input.charAt(i) == 'R') {
                SEED = Integer.parseInt(seed);
                RANDOM = new Random(SEED); }
            new WorldDemo(SEED, RANDOM, smarray, args[0], avatar);


        }

    }
}


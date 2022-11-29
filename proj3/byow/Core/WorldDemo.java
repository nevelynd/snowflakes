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

public class WorldDemo {
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /** Max width/height integer for a room.*/
    public static final int ROOM_MAX = 10;

    /** Minimum width/height integer for a room.*/
    public static final int ROOM_MIN = 6;
    private static final List<Rectangle> rooms = new ArrayList<>();
    public int health;
    public int posx;
    public int posy;
    Boolean gameOver;



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

        int maxRooms = random.nextInt(7,25);

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

                                || ra.x + ra.width >= WIDTH   || ra.y  >= HEIGHT ) {
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
            Point p = new Point(ra.x + room_width / 2, ra.y - ra.height / 2);
            points.put(ra, p);
            pointsx.put(ra, p.x);
            pointsy.put(ra, p.y);
            //System.out.println(rooms);

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
        int maxx = Collections.max(pointsx.values());
        int maxy = Collections.max(pointsy.values());
        int minx = Collections.min(pointsx.values());
        int miny = Collections.min(pointsy.values());
        /** maybe keep track of connected rects and see how to prevent unnecessary hallways*/

        for (Rectangle room : rooms) {
            createLinearPath(tiles, points.get(room).x, points.get(room).y, maxx, maxy, random);

        }
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                if (tiles[x][y] == Tileset.FLOOR && ((tiles[x][y + 1] != Tileset.FLOOR && tiles[x][y + 1] != Tileset.WALL)
                        ||(tiles[x + 1][y] != Tileset.FLOOR && tiles[x + 1][y] != Tileset.WALL) ||
                        (tiles[x][y - 1] != Tileset.FLOOR && tiles[x][y - 1] != Tileset.WALL) ||
                        (tiles[x - 1][y] != Tileset.FLOOR && tiles[x - 1][y] != Tileset.WALL))) {
                    tiles[x][y] = Tileset.WALL;

                }
            }
        }


        int doorx = door.x;
        int doory = door.y;
        tiles[doorx][doory] = Tileset.FLOWER;
        TETile initialtile = tiles[doorx][doory];
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
                    if (fromY+count > HEIGHT || fromY+count > 0) {
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

    /** Started creating a path method that does not generate a boring pathQ*/
    public static void createPath(TETile[][] tiles, int fromX, int fromY, int destinationX, int destinationY, Random random) {
        int distanceX = abs(destinationX - fromX);
        int pathLength;
        int coinFlip;
    }
    public static void displayHUD(TETile[][] world, String HUD, int health) {
        int r = 0;
        for (char i : HUD.toCharArray()) {
            TETile I = new TETile(i, Color.white, Color.black,
                    "i");
            world[r][29] = I;
            r+=1;
        }
        for (int i = 0; i <=4 ; i++) {
            if (health>0) {
                world[r][29] = Tileset.FULLHEART;
                health -=1;


            } else {
            world[r][29] = Tileset.EMPTYHEART;}
            r+=1;

        }

    }




















    public WorldDemo(Random RANDOM,char[] smarray) {
        TETile[][] myWorldDemo = new TETile[WIDTH][HEIGHT];
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);



        gameOver = false;
        health = 5;

        makeRooms(myWorldDemo, RANDOM);
        String HUD = "";
        displayHUD(myWorldDemo, HUD, health);
        posx = RANDOM.nextInt(80);
        posy = RANDOM.nextInt(29);
        while (myWorldDemo[posx][posy] == Tileset.WALL) {
            posx = RANDOM.nextInt(80);
            posy = RANDOM.nextInt(29);

        }

        TETile initialtile = myWorldDemo[posx][posy];
        myWorldDemo[posx][posy] = Tileset.AVATAR;
        int sm = 0;




        if (smarray.length>sm) {
            while (!gameOver && smarray.length>sm) {
                char stringletter = smarray[sm];
                sm+=1;

                /** previous tile setting before move*/
                myWorldDemo[posx][posy] = initialtile;

                /** quits and saves*/
                if (stringletter == ':') {
                    StdDraw.pause(1000);

                    stringletter = smarray[sm];
                    if (stringletter == 'Q' || stringletter == 'q') {
                        gameOver = true;
                        LoadandSave.save();
                    }
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


                myWorldDemo[posx][posy] = Tileset.AVATAR;
                if (health <=0) {
                    gameOver = true;
                }

            }
            if (gameOver) {
                System.exit(0);
            }
        }
        else {
            while (!gameOver) {
                ter.renderFrame(myWorldDemo);
                while (StdDraw.hasNextKeyTyped()) {
                    /** user input for where to move next*/
                    char letter = StdDraw.nextKeyTyped();


                    /** previous tile setting before move*/
                    myWorldDemo[posx][posy] = initialtile;

                    /** quits and saves*/
                    if (letter == ':') {
                        StdDraw.pause(1000);
                        char nextletter = StdDraw.nextKeyTyped();
                        if (nextletter == 'Q' || nextletter == 'q') {
                            gameOver = true;
                            LoadandSave.save();
                        }
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


                    myWorldDemo[posx][posy] = Tileset.AVATAR;
                    if (health <= 0) {
                        gameOver = true;
                    }

                }
                ter.renderFrame(myWorldDemo);

            }
            if (gameOver) {
                System.exit(0);
            }


        }
        }










    public static void main(String[] args) {

        //TETile[][] myWorldDemo = new TETile[WIDTH][HEIGHT];

        String seed = "";
        String input = args[0];
        int i;

        for ( i = 0; i < input.length(); i++ ) {
            if (input.charAt(i) == 'n' || input.charAt(i) == 'N') {
                continue; }
            if (input.charAt(i) == 's' || input.charAt(i) == 'S') {
                break; }
            seed += input.charAt(i);
        }
        String stringmovement = "";
        for ( int j = i + 1 ;j < input.length(); j++ ) {
            stringmovement += input.charAt(j);
        }
        char[] smarray= stringmovement.toCharArray();




        long SEED = Integer.parseInt(seed);
        Random RANDOM = new Random(SEED);

        new WorldDemo(RANDOM , smarray);







    }
}


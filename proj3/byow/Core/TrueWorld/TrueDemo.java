package byow.Core.TrueWorld;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.List;

import static java.lang.Math.abs;

public class TrueDemo {
    public static final int WIDTH = 90;
    public static final int HEIGHT = 45;
    public static final int ROOM_MAX = 10;
    public static final int ROOM_MIN = 6;
    private static final List<Rectangle> rooms = new ArrayList<>();
    private static final List<Rectangle> validConnections = new ArrayList<>();
    private static final ArrayList<Point> validCoordinates = new ArrayList<>();
    private static List<Rectangle> lockableRooms = new ArrayList<>();
    private static Point spawnPoint;
    public int posX;
    public int posY;
    public Boolean gameOver;
    public long randomSeed;
    public int avatar;
    int snowflakesNum;
    TERenderer ter = new TERenderer();
    TETile[][] myWorld;
    Random random;
    char[] smArray;
    String stringMovement = "";

    public void TrueDemo(String[] s) {
        long seed = Long.valueOf(s[2]);
        avatar = Integer.parseInt(s[1]);
        TETile[][] myTrueDemo = new TETile[WIDTH][HEIGHT];
        myWorld = myTrueDemo;
        Random random = new Random(seed);
        myTrueDemo = declareWorldT(myTrueDemo, random);
        createRooms(myTrueDemo, random);
        createHallways(myTrueDemo, random);
        certifyOrigin(myTrueDemo, rooms.get(0));
        clearWalls(myTrueDemo, random);
        ter.initialize(WIDTH, HEIGHT, 0, -2);
        ter.renderFrame(myTrueDemo);
        playGame(myTrueDemo, random, avatar, s[0]);
    }

    private void playGame(TETile[][] tiles, Random seed, int avatar, String typedSeed) {
        int playerX = rooms.get(0).x + rooms.get(0).width / 2;
        int playerY = rooms.get(0).y + rooms.get(0).height / 2;
        TETile player = Tileset.AVATAR;
        switch(avatar) {
            case 0:
            case 1:
                player = Tileset.curlyheart;
                break;
            case 2:
                player = Tileset.star;
                break;
            case 3:
                player = Tileset.smiley;
                break;
            case 4:
                player = Tileset.crown;
            case 5:
                player = Tileset.yinyang;
        }
        TETile initialtile = tiles[playerX][playerY];
        tiles[playerX][playerY] = player;
        ter.renderFrame(tiles);
        gameOver = false;
        int keysLeft;
        snowflakesNum = seed.nextInt(5, validCoordinates.size());
        keysLeft = snowflakesNum;

        for (int i = 0; i < snowflakesNum - 1; i++) {
            int randomIndex = seed.nextInt(validCoordinates.size());
            int xPOI = validCoordinates.get(randomIndex).x;
            int yPOI = validCoordinates.get(randomIndex).y;
            Rectangle ra = new Rectangle(xPOI, yPOI, 3, 3);
            for (Rectangle rb : rooms) {
                if (ra.intersects(rb)) {
                    lockableRooms.add(rb);
                }
            }
            tiles[playerX][playerY + 1] = Tileset.SNOWFLAKE;

            tiles[xPOI][yPOI] = Tileset.SNOWFLAKE;
        }
        ter.renderFrame(tiles);

        while (!gameOver) {


            while (StdDraw.hasNextKeyTyped()) {
                /** user input for where to move next*/
                char letter = StdDraw.nextKeyTyped();


                /** previous tile setting before move*/
                tiles[playerX][playerY] = initialtile;

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






                /** moves avatar left if allowed*/
                if (letter == 'a' || letter == 'A') {
                    if (playerX - 1 >= 0 && tiles[playerX - 1][playerY] != Tileset.WALL) {
                        initialtile = tiles[playerX - 1][playerY];
                        playerX = playerX - 1;
                    }
                }

                /** moves avatar right if allowed*/
                if (letter == 'd' || letter == 'D') {

                    if (playerX + 1 <= WIDTH - 1 && tiles[playerX + 1][playerY] != Tileset.WALL) {
                        initialtile = tiles[playerX + 1][playerY];
                        playerX = playerX + 1;
                    }

                }



                /** moves avatar up if allowed*/
                if (letter == 'w' || letter == 'W') {
                    if (playerY + 1 <= HEIGHT - 1 && tiles[playerX][playerY + 1] != Tileset.WALL) {
                        initialtile = tiles[playerX][playerY + 1];
                        playerY = playerY + 1;
                    }

                }

                /** moves avatar down if allowed*/
                if (letter == 's' || letter == 'S') {
                    if (playerY - 1 >= 0 && tiles[playerX][playerY - 1] != Tileset.WALL) {
                        initialtile = tiles[playerX][playerY - 1];
                        playerY = playerY - 1;
                    }
                }
                tiles[playerX][playerY] = player;
                if (initialtile == Tileset.SNOWFLAKE) {
                    //keysleft-=1;
                    initialtile = Tileset.FLOOR;


                }

                ter.renderFrame(tiles);
            }}


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









    private void clearWalls(TETile[][] tiles, Random random) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                if (tiles[x][y] == Tileset.WALL && tiles[x-1][y] == Tileset.FLOOR && tiles[x+1][y] == Tileset.FLOOR) {
                    tiles[x][y] = Tileset.FLOOR;
                }
                if (tiles[x][y] == Tileset.WALL && tiles[x][y-1] == Tileset.FLOOR && tiles[x][y+1] == Tileset.FLOOR) {
                    tiles[x][y] = Tileset.FLOOR;
                }
            }
        }
    }

    public void testCircle(TETile[][] tiles) {
        Rectangle ra = rooms.get(0);
        Circle ca = new Circle(ra, 5, ra.x, ra.y);
        ca.intersects(ra);
    }

    public TETile[][] declareWorldT(TETile[][] tiles, Random seed) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
                int worldLayout = seed.nextInt(10);
                switch(worldLayout) {
                    case 0:
                    case 5:
                        tiles[x][y] = Tileset.WATER;
                        break;
                    case 1:
                    case 6:
                    case 8:
                    case 9:
                        tiles[x][y] = Tileset.GRASS;
                        break;
                    case 2:
                        tiles[x][y] = Tileset.MOUNTAIN;
                        break;
                    case 3:
                        tiles[x][y] = Tileset.TREE;
                        break;
                    case 4:
                    case 7:
                        tiles[x][y] = Tileset.SAND;
                        break;
                }
            }
        }
        return tiles;
    }
    public static void createRooms(TETile[][] tiles, Random seed) {
        int xs;
        int ys;
        int roomWidth;
        int roomHeight;
        Rectangle ra;
        int maxRooms = seed.nextInt(7,20);
        for(int i = 0; i < maxRooms; i++) {
            cont:
            while (true) {
                roomHeight = seed.nextInt(ROOM_MIN, ROOM_MAX + 1);
                roomWidth = seed.nextInt(ROOM_MIN, ROOM_MAX + 1);
                xs = seed.nextInt(0, WIDTH - roomWidth);
                ys = seed.nextInt(0, HEIGHT - roomHeight);
                ra = new Rectangle(xs, ys, roomHeight, roomWidth);
                if (ra.x > WIDTH || ra.y > HEIGHT || ra.x <= 0 || ra.y - 1 <= 0) {
                    continue;
                }
                int radius = ra.width / 2 + 5;
                Rectangle modify = ra;
                for (Rectangle rb : rooms) {
                    if (modify.intersects(rb) || rb.intersects(modify) || rb.contains(ra) || rb.contains(ra)) {
                        continue cont;
                    }
                }
                break;
            }
            rooms.add(ra);
            if (i == 0) {
                ra.root = rooms.get(0);
            }
            Point p = new Point(ra.x + roomWidth / 2 , ra.y + roomHeight / 2);
            validCoordinates.add(p);
            if (i == 0) {
                spawnPoint = p;
                validCoordinates.remove(p);
            }

            for (int x = xs; x < roomWidth + xs; x += 1) {
                for (int y = ys; y < roomHeight + ys; y += 1) {
                    if (x == xs || y == ys  || x == (xs + roomWidth -1) || y == (ys + roomHeight - 1)) {
                        tiles[x][y] = Tileset.WALL;
                    } else {
                        if (y == 0) {
                            tiles[x][y] = Tileset.WALL;
                        } else {
                            tiles[x][y] = Tileset.FLOOR;
                        }

                    }
                }
            }
        }
    }

    public void lockDoor(TETile[][] tiles) {
        for (Rectangle ra : lockableRooms) {
            for (int x = ra.x; x < ra.width + ra.x; x += 1) {
                for (int y = ra.y; y < ra.height + ra.y; y += 1) {
                    if (x == ra.x || y == ra.y  || x == (ra.x + ra.width -1) || y == (ra.y + ra.height - 1)) {
                        if (tiles[x][y] == Tileset.FLOOR) {
                            tiles[x][y] = Tileset.LOCKED_DOOR;
                        } else {
                            tiles[x][y] = Tileset.WALL;
                        }
                    } else {
                        tiles[x][y] = Tileset.FLOOR;
                    }
                }
            }
        }
    }


    public void certifyOrigin(TETile[][] tiles, Rectangle ra) {
        for (int x = ra.x; x < ra.width + ra.x; x += 1) {
            for (int y = ra.y; y < ra.height + ra.y; y += 1) {
                if (x == ra.x || y == ra.y  || x == (ra.x + ra.width -1) || y == (ra.y + ra.height - 1)) {
                    if (tiles[x][y] == Tileset.FLOOR) {
                        tiles[x][y] = Tileset.FLOOR;
                    } else {
                        tiles[x][y] = Tileset.WALL;
                    }
                } else {
                        tiles[x][y] = Tileset.FLOOR;
                }
            }
        }
    }


    public void createHallways(TETile[][] tiles, Random seed) {
        List<Rectangle> rootRect = new ArrayList<>();
        for (int i = 0; i < rooms.size(); i++) {
            Rectangle ra = rooms.get(i);
            int radius = ra.width / 2;
            Circle ca;
            while (!ra.isConnected){
                radius += 1;
                boolean intersection = false;
                ca = new Circle(ra, radius, ra.x, ra.y);
                for (Rectangle rb : rooms) {
                    if (!intersection) {
                        if (ca.intersects(rb)) {
                            Point p = new Point(rb.x + rb.width / 2, rb.y + rb.height / 2);
                            ra.destination = p;
                            ra.isConnected = true;
                            if (rb.root == null) {
                                if (ra.root == null) {
                                    ra.root = ra;
                                }
                                rb.root = ra.root;
                                ra.isRoot = true;
                            } else {
                                ra.root = rb.root;
                            }
                            if (!rootRect.contains(ra.root.root)) {
                                rootRect.add(ra.root.root);
                            }
                            intersection = true;
                        }
                    }
                }
            }
        }
        hallwayDirection(tiles, seed);
        Rectangle origin = rooms.get(0);
        Point p = new Point(origin.x + origin.width / 2, origin.y + origin.height / 2);
        for (Rectangle ra : rootRect) {
            ra.destination = p;
            connectToOrigin(tiles, seed, ra);
        }

    }

    public void hallwayDirection(TETile[][] tiles, Random seed) {
        for (int i = 0; i < rooms.size(); i++) {
            Rectangle ra = rooms.get(i);
            ra.entrance = determineDirection(ra.x - ra.destination.x, ra.y - ra.destination.y, seed);
            switch(ra.entrance) {
                case North:
                    drawHallwayHelper(tiles, Direction.North, seed, ra);
                    break;
                case East:
                    drawHallwayHelper(tiles, Direction.East, seed, ra);
                    break;
                case South:
                    drawHallwayHelper(tiles, Direction.South, seed, ra);
                    break;
                case West:
                    drawHallwayHelper(tiles, Direction.West, seed, ra);
                    break;
            }
        }
    }

    public void connectToOrigin(TETile[][] tiles, Random seed, Rectangle ra) {
        ra.entrance = determineDirection(ra.x - ra.destination.x, ra.y - ra.destination.y, seed);
        switch(ra.entrance) {
            case North:
                drawHallwayHelper(tiles, Direction.North, seed, ra);
                break;
            case East:
                drawHallwayHelper(tiles, Direction.East, seed, ra);
                break;
            case South:
                drawHallwayHelper(tiles, Direction.South, seed, ra);
                break;
            case West:
                drawHallwayHelper(tiles, Direction.West, seed, ra);
                break;
        }
    }

    public void updateValidConnect() {
        for (Rectangle ra : rooms) {
            if (ra.root == rooms.get(0) || ra.root.root == rooms.get(0)) {
                validConnections.add(ra);
            }
        }
    }

    public void drawHallwayHelper(TETile[][] tiles, Direction direction, Random seed, Rectangle ra) {
        int tetrahedron = seed.nextInt(3);
        switch (tetrahedron) {
            case 0:
            case 1:
            case 2:
            case 3:
                drawLinearPath(tiles, direction, seed, ra);
        }

    }

    public void drawLinearPath(TETile[][] tiles, Direction direction, Random seed, Rectangle ra) {
        int x = 0;
        int y = 0;
        switch (direction) {
            case North:
                x = seed.nextInt(ra.x + 1, ra.x + ra.width - 1);
                y = ra.y + ra.height - 1;
                break;
            case East:
                x = ra.x + ra.width - 1;
                y = seed.nextInt(ra.y + 1, ra.y + ra.height - 1);
                for (int i = x; i < ra.destination.x; i++) {
                    tiles[i][y] = Tileset.FLOOR;
                    if (tiles[i][y+1] != Tileset.FLOOR) {
                        tiles[i][y+1] = Tileset.WALL;
                    } if (tiles[i][y-1] != Tileset.FLOOR) {
                        tiles[i][y-1] = Tileset.WALL;
                    }
                }
                    drawLinearPathHelper(tiles, y, x, ra, (ra.y + ra.width / 2) - ra.destination.y);
                break;
            case South:
                break;
            case West:
                x = ra.x;
                y = seed.nextInt(ra.y + 1, ra.y + ra.height - 1);
                for (int i = x; i > ra.destination.x; i--) {
                    if (y == 0) {
                        y += 1;
                    }
                    tiles[i][y] = Tileset.FLOOR;
                    if (tiles[i][y+1] != Tileset.FLOOR) {
                        tiles[i][y+1] = Tileset.WALL;
                    } if (tiles[i][y-1] != Tileset.FLOOR) {
                        tiles[i][y-1] = Tileset.WALL;
                    } if (tiles[i-1][y+1] != Tileset.FLOOR) {
                        tiles[i-1][y+1] = Tileset.WALL;
                    }
                }
                drawLinearPathHelper(tiles, y, x, ra, ra.y - ra.destination.y);
                break;
        }
    }

    public void drawLinearPathHelper(TETile[][] tiles, int y, int x, Rectangle ra, int above) {
        Direction next;
        if (above < 0) {
            next = Direction.North;
        } else {
            next = Direction.South;
        }
        switch(next) {
            case North:
                if (tiles[ra.destination.x][y-1] != Tileset.FLOOR) {
                    tiles[ra.destination.x][y-1] = Tileset.WALL;
                }
                if (tiles[ra.destination.x-1][y-1] != Tileset.FLOOR) {
                    tiles[ra.destination.x-1][y-1] = Tileset.WALL;
                }
                if (tiles[ra.destination.x+1][y-1] != Tileset.FLOOR) {
                    tiles[ra.destination.x+1][y-1] = Tileset.WALL;
                }
                for (int i = y; i < ra.destination.y; i++) {
                    tiles[ra.destination.x][i] = Tileset.FLOOR;
                    if (tiles [ra.destination.x-1][i] != Tileset.FLOOR) {
                        tiles[ra.destination.x-1][i] = Tileset.WALL;
                    } if (tiles[ra.destination.x+1][i] != Tileset.FLOOR) {
                        tiles[ra.destination.x+1][i] = Tileset.WALL;
                    }
                }

                break;
            case South:
                if (tiles[ra.destination.x][y+1] != Tileset.FLOOR) {
                    tiles[ra.destination.x][y+1] = Tileset.WALL;
                }
                if (tiles[ra.destination.x+1][y+1] != Tileset.FLOOR) {
                    tiles[ra.destination.x+1][y+1] = Tileset.WALL;
                }
                if (tiles[ra.destination.x-1][y+1] != Tileset.FLOOR) {
                    tiles[ra.destination.x-1][y+1] = Tileset.WALL;
                }
                for (int i = y; i > ra.destination.y; i--) {
                    tiles[ra.destination.x][i] = Tileset.FLOOR;
                    if (tiles [ra.destination.x-1][i] != Tileset.FLOOR) {
                        tiles[ra.destination.x-1][i] = Tileset.WALL;
                    } if (tiles[ra.destination.x+1][i] != Tileset.FLOOR) {
                        tiles[ra.destination.x+1][i] = Tileset.WALL;
                    }
                }
                break;
        }
    }


    public Direction determineDirection(int xDistance, int yDistance, Random seed) {
        int coinFlip = seed.nextInt(1);
        switch(coinFlip) {
            case 0:
                if (xDistance < 0) {
                    return Direction.East;
                } else {
                    return Direction.West;
                }
            case 1:
                if (yDistance < 0) {
                    return Direction.North;
                } else {
                    return Direction.South;
                }
        }
        return null;
    }



}
package byow.Core.TrueWorld;

import byow.Core.Engine;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.awt.event.MouseEvent;
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
    private static ArrayList<Point> snowflakeLocation = new ArrayList<>();
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
        ter.initialize(WIDTH, HEIGHT, 0 , 0);
        ter.renderFrame(myTrueDemo);
        playGame(myTrueDemo, random, avatar, s[0]);
    }

    public TETile[][] continueTrueDemo(String pastSeed, String remaining, String total, int avatar, TETile[][] tiles, Boolean replay) {
        long seed = Long.valueOf(pastSeed);
        myWorld = tiles;
        Random random = new Random(seed);
        tiles = declareWorldT(tiles, random);
        createRooms(tiles, random);
        createHallways(tiles, random);
        certifyOrigin(tiles, rooms.get(0));
        clearWalls(tiles, random);
        ter.initialize(WIDTH, HEIGHT, 0, 0);
        ter.renderFrame(tiles);
        playGameState(tiles, random, avatar, remaining, total, replay);
        return tiles;
    }

    private void playGameState(TETile[][] tiles, Random seed, int avatar, String remaining, String total, Boolean replay) {
        int playerX = rooms.get(0).x + rooms.get(0).width / 2;
        int playerY = rooms.get(0).y + rooms.get(0).height / 2;
        TETile player = Tileset.AVATAR;
        switch (avatar) {
            case 0:
                player = Tileset.AVATAR;
                break;
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
                break;
            case 5:
                player = Tileset.yinyang;
                break;
        }
        TETile initialTile = tiles[playerX][playerY];
        tiles[playerX][playerY] = player;
        gameOver = false;
        int keysLeft;
        snowflakesNum = seed.nextInt(5, validCoordinates.size());
        keysLeft = snowflakesNum;
        for (int i = 0; i <= snowflakesNum; i++) {
            int randomIndex = seed.nextInt(validCoordinates.size());
            int xPOI = validCoordinates.get(randomIndex).x;
            int yPOI = validCoordinates.get(randomIndex).y;
            Rectangle ra = new Rectangle(xPOI, yPOI, 3, 3);
            for (Rectangle rb : rooms) {
                if (ra.intersects(rb)) {
                    lockableRooms.add(rb);
                }
            }
            tiles[xPOI][yPOI] = Tileset.SNOWFLAKE;
        }

        for(int i = 1; i < remaining.length(); i++) {
            char currentLetter = remaining.charAt(i);
            tiles[playerX][playerY] = initialTile;
            if (currentLetter == 'a' || currentLetter == 'A') {
                if (playerX - 1 >= 0 && tiles[playerX - 1][playerY] != Tileset.WALL) {
                    initialTile = tiles[playerX - 1][playerY];
                    playerX = playerX - 1;
                }
            }
            if (currentLetter == 'd' || currentLetter == 'D') {
                if (playerX + 1 <= WIDTH - 1 && tiles[playerX + 1][playerY] != Tileset.WALL) {
                    initialTile = tiles[playerX + 1][playerY];
                    playerX = playerX + 1;
                }
            }
            if (currentLetter == 'w' || currentLetter == 'W') {
                if (playerY + 1 <= HEIGHT - 1 && tiles[playerX][playerY + 1] != Tileset.WALL) {
                    initialTile = tiles[playerX][playerY + 1];
                    playerY = playerY + 1;
                }
            }
            if (currentLetter == 's' || currentLetter == 'S') {
                if (playerY - 1 >= 0 && tiles[playerX][playerY - 1] != Tileset.WALL) {
                    initialTile = tiles[playerX][playerY - 1];
                    playerY = playerY - 1;
                }
            }
            tiles[playerX][playerY] = player;
            if (initialTile == Tileset.SNOWFLAKE) {
                keysLeft -= 1;
                initialTile = Tileset.FLOOR;
            }


            if (replay) {
                displayHUD(tiles, null, keysLeft, null);
                ter.renderFrame(tiles, true);
            }




        }

        if (replay) {
            System.exit(0);
        }

        if (!replay) {
            displayHUD(tiles, null, keysLeft, null);
            ter.renderFrame(tiles);
            while (!gameOver) {

                hoverHUD(tiles);

                while (StdDraw.hasNextKeyTyped()) {
                    char letter = StdDraw.nextKeyTyped();
                    tiles[playerX][playerY] = initialTile;

                    if (letter == ':') {
                        total += letter;
                        while (!StdDraw.hasNextKeyTyped()) {
                            StdDraw.pause(1000);
                        }
                        char nextLetter = StdDraw.nextKeyTyped();
                        if (nextLetter == 'Q' || nextLetter == 'q') {
                            total += nextLetter;
                            gameOver = true;
                        }
                        save(total);
                    }
                    if (letter == 'a' || letter == 'A') {
                        if (playerX - 1 >= 0 && tiles[playerX - 1][playerY] != Tileset.WALL) {
                            initialTile = tiles[playerX - 1][playerY];
                            playerX = playerX - 1;
                            total += letter;
                        }
                    }
                    if (letter == 'd' || letter == 'D') {
                        if (playerX + 1 <= WIDTH - 1 && tiles[playerX + 1][playerY] != Tileset.WALL) {
                            initialTile = tiles[playerX + 1][playerY];
                            playerX = playerX + 1;
                            total += letter;
                        }

                    }
                    if (letter == 'w' || letter == 'W') {
                        if (playerY + 1 <= HEIGHT - 1 && tiles[playerX][playerY + 1] != Tileset.WALL) {
                            initialTile = tiles[playerX][playerY + 1];
                            playerY = playerY + 1;
                            total += letter;
                        }

                    }
                    if (letter == 's' || letter == 'S') {
                        if (playerY - 1 >= 0 && tiles[playerX][playerY - 1] != Tileset.WALL) {
                            initialTile = tiles[playerX][playerY - 1];
                            playerY = playerY - 1;
                            total += letter;
                        }
                    }
                    tiles[playerX][playerY] = player;
                    if (initialTile == Tileset.SNOWFLAKE) {
                        keysLeft -= 1;
                        initialTile = Tileset.FLOOR;
                    }
                    if (keysLeft ==0  ){
                        gameOver = true;
                    }

                    String ct = hoverHUD(tiles);
                    displayHUD(tiles, null, keysLeft, ct);
                    ter.renderFrame(tiles);
                }
            }
        }
        if (gameOver) {
            System.exit(0);
        }
    }

    private void playGame(TETile[][] tiles, Random seed, int avatar, String typedSeed) {
        int playerX = rooms.get(0).x + rooms.get(0).width / 2;
        int playerY = rooms.get(0).y + rooms.get(0).height / 2;
        TETile player = Tileset.AVATAR;
        switch(avatar) {
            case 0:
                player = Tileset.AVATAR;
                break;
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
                break;
            case 5:
                player = Tileset.yinyang;
                break;
        }
        TETile initialTile = tiles[playerX][playerY];
        tiles[playerX][playerY] = player;
        gameOver = false;
        int keysLeft;
        snowflakesNum = seed.nextInt(5, validCoordinates.size());
        keysLeft = snowflakesNum;



        for (int i = 0; i < snowflakesNum; i++) {
            int randomIndex = seed.nextInt(validCoordinates.size());
            int xPOI = validCoordinates.get(randomIndex).x;
            int yPOI = validCoordinates.get(randomIndex).y;
            if (!snowflakeLocation.contains(validCoordinates.get(randomIndex))) {
                snowflakeLocation.add(validCoordinates.get(randomIndex));
            } else {
                continue;
            }
            tiles[xPOI][yPOI] = Tileset.SNOWFLAKE;
        }

        String HUD = "Keys left:";
        String ct = hoverHUD(tiles);
        displayHUD(tiles, HUD, keysLeft, ct);
        ter.renderFrame(tiles);

        while (!gameOver) {

            while (StdDraw.hasNextKeyTyped()) {
                char letter = StdDraw.nextKeyTyped();
                tiles[playerX][playerY] = initialTile;

                if (letter == ':') {
                    typedSeed += letter;
                    while (!StdDraw.hasNextKeyTyped()) {
                        StdDraw.pause(1000);
                    }
                    char nextLetter = StdDraw.nextKeyTyped();
                    if (nextLetter == 'Q' || nextLetter == 'q') {
                        typedSeed += nextLetter;
                        gameOver = true;
                    }
                    save(typedSeed);
                }
                if (letter == 'a' || letter == 'A') {
                    if (playerX - 1 >= 0 && tiles[playerX - 1][playerY] != Tileset.WALL) {
                        initialTile = tiles[playerX - 1][playerY];
                        playerX = playerX - 1;
                        typedSeed += letter;
                    }
                }
                if (letter == 'd' || letter == 'D') {
                    if (playerX + 1 <= WIDTH - 1 && tiles[playerX + 1][playerY] != Tileset.WALL) {
                        initialTile = tiles[playerX + 1][playerY];
                        playerX = playerX + 1;
                        typedSeed += letter;
                    }

                }
                if (letter == 'w' || letter == 'W') {
                    if (playerY + 1 <= HEIGHT - 1 && tiles[playerX][playerY + 1] != Tileset.WALL) {
                        initialTile = tiles[playerX][playerY + 1];
                        playerY = playerY + 1;
                        typedSeed += letter;
                    }

                }
                if (letter == 's' || letter == 'S') {
                    if (playerY - 1 >= 0 && tiles[playerX][playerY - 1] != Tileset.WALL) {
                        initialTile = tiles[playerX][playerY - 1];
                        playerY = playerY - 1;
                        typedSeed += letter;
                    }
                }
                tiles[playerX][playerY] = player;
                if (initialTile == Tileset.SNOWFLAKE) {
                    keysLeft -= 1;
                    initialTile = Tileset.FLOOR;

                }
                ct = hoverHUD(tiles);
                displayHUD(tiles, HUD, keysLeft, ct);
                ter.renderFrame(tiles);
            }
        }
        if (gameOver) {
            System.exit(0);
        }
    }

    public  void displayHUD(TETile[][] world, String HUD, int keysLeft, String currentTile) {
        int r = 0;
        for (char i : "keys left:".toCharArray()) {
            TETile I = new TETile(i, Color.white, Color.black,
                    "i");
            world[r][HEIGHT -1] = I;
            r+=1;
        }
        for (int i = r; i < WIDTH ; i++) {
            if (keysLeft>1) {
                world[i][HEIGHT - 1] = Tileset.SNOWFLAKE;
                keysLeft -=1;

            }  else {
                world[i][HEIGHT - 1] = Tileset.NOTHING;
            }
        }

        char[] ctarray =  currentTile.toCharArray();
        int count = 0;
        for (int i = WIDTH - currentTile.length() ; i < WIDTH; i++) {
            TETile I = new TETile(ctarray[count], Color.white, Color.black,
                    "i");
            world[i][HEIGHT -1] = I;
            count +=1;

        }
    }

    public String hoverHUD(TETile[][] world) {
        String currentTile = "false";
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        if (world[x][y] == Tileset.SNOWFLAKE) {
            currentTile = "Snowflake";
        } if (world[x][y] == Tileset.WALL) {
            currentTile = "Wall";
        }
        if (world[x][y] == Tileset.FLOOR) {
            currentTile = "Floor";
        }
        if (world[x][y] == Tileset.GRASS) {
            currentTile = "Grass";
        } if (world[x][y] == Tileset.SAND) {
            currentTile = "Sand";
        }
        if (world[x][y] == Tileset.TREE) {
            currentTile = "Tree";
        }
        if (world[x][y] == Tileset.MOUNTAIN) {
            currentTile = "Mountain";
        } if (world[x][y] == Tileset.WATER) {
            currentTile = "Water";
        }
        return currentTile;


        //System.out.println(currentTile);
    }



    public void save(String typedSeed) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("save.txt"));
            bw.write("" + avatar);
            bw.newLine();
            bw.write("" + typedSeed);
            bw.close();

            BufferedWriter br = new BufferedWriter(new FileWriter("replay.txt"));
            br.write("" + avatar);
            br.newLine();
            br.write("" + typedSeed);
            br.close();

        }
        catch (Exception e) {
            System.out.println("Please play a game to save first!");
        }

    }

    public void load() {
        try {
            BufferedReader games = new BufferedReader(new FileReader("save.txt"));
            int avatar = Integer.parseInt(games.readLine());
            String s = games.readLine();
            TrueEngine engine = new TrueEngine();
            engine.interactWithInputString(s, avatar);
        }
        catch (Exception e) {
            System.out.println("Please play a game to load first!");
        }
    }

    public void replay() {
        try {
            BufferedReader games = new BufferedReader(new FileReader("replay.txt"));
            int avatar = Integer.parseInt(games.readLine());
            String s = games.readLine();
            TrueEngine engine = new TrueEngine();
            engine.interactWithInputString(s, avatar, true);

        }
        catch (Exception e) {
            System.out.println("Please play a game to replay first!");
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
            for (int y = 0; y < HEIGHT ; y += 1) {
                if (y == HEIGHT - 1 ) {
                    tiles[x][y] = Tileset.NOTHING;

                }
                  else {
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
            }}
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
                if (ra.x > WIDTH || ra.y > HEIGHT - 1 || ra.x <= 0 || ra.y - 1 <= 0) {
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
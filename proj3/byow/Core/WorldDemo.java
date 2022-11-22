package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;


import java.util.*;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.floor;

public class WorldDemo {
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /** Max width/height integer for a room.*/
    public static final int ROOM_MAX = 10;

    /** Minimum width/height integer for a room.*/
    public static final int ROOM_MIN = 5;
    private static final List<Rectangle> rooms = new ArrayList<>();
    public static TETile[][] myWorldDemo;

    /** Not entirely sure why I created these.*/
    public static Set<Integer> xs = new HashSet<>();
    public static Set<Integer> ys = new HashSet<>();

    public static void makeRooms(TETile[][] tiles, Random random) {
        /** Set the background as a series of empty tiles.*/
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }

        /** Initialize variables.*/
        int xs;;
        int ys;
        int room_width;
        int room_height;
        Rectangle ra = null;
        int maxRooms = random.nextInt(5,25);

        for(int i = 0; i < maxRooms; i++) {
            cont:
            while (true) {
                /** Set a random origin for the rectangle, these are the variables xs and ys.*/
                xs = random.nextInt(0, WIDTH + 1);
                ys = random.nextInt(0, HEIGHT + 1);
                room_height = random.nextInt(ROOM_MIN, ROOM_MAX + 1);
                room_width = random.nextInt(ROOM_MIN, ROOM_MAX + 1);

                /** If the room that is about to be created does not meet the specified requirements,
                 * then we cycle through the while statement again. Thus, generating a new room.*/
                if (room_width + xs > WIDTH || room_height + ys > HEIGHT) {
                    continue;
                }
                Point p = new Point(xs, ys);
                ra = new Rectangle(p.x - room_width / 2, p.y - room_height / 2, room_width, room_height);
                /** If the rectangle intersects with another rectangle within our known list, then
                 * we must generate a new rectangle.*/
                for (Rectangle rb : rooms) {
                    if (ra.intersects(rb)) {
                        continue cont;
                    }
                }

                break;
            }

            /** Add valid rectangle to array.*/
            rooms.add(ra);

            /** Generate tiles*/
            for (int x = xs; x < room_width + xs; x += 1) {
                for (int y = ys; y < room_height + ys; y += 1) {
                    if (x == xs || y == ys || x == (xs + room_width - 1) || y == (ys + room_height - 1)) {
                        tiles[x][y] = Tileset.WALL;
                    } else {
                        tiles[x][y] = Tileset.FLOOR;
                    }
                }
            }

        }

        int index = random.nextInt(rooms.size() + 1);
        for (Rectangle rec : rooms) {

        }
    }


    /** Generation of a linear path, that takes in start and end values. Very predictable. I've looked through several algorithms
     * and most path generations are disgustingly similar with regard to the use of brute force. It's really nasty, and I'm working
     * diligently to create something more elegant.*/
    public static void createLinearPath(TETile[][] tiles, int fromX, int fromY, int destinationX, int destinationY, Random random) {
        if (fromX > destinationX) {
            generateHallwayTiles(tiles, fromX, fromY, destinationX, destinationY, true);
        } else {
            generateHallwayTiles(tiles, destinationX, fromY, fromX, destinationY, true);
        }
        if (fromY > destinationY) {
            generateHallwayTiles(tiles, fromX, fromY, destinationX, destinationY, false);
        } else {
            generateHallwayTiles(tiles, fromX, destinationY, destinationX, fromY, false);
        }
    }

    public static void createHallwayEdgesX(TETile[][] tiles, int x, int destinationX, int fromX, int fromY, int adjust) {
        if (tiles[x - adjust][fromY] != Tileset.FLOOR) {
            tiles[x - adjust][fromY] = Tileset.WALL;
            for (int count = -1; count != 2; count++) {
                if (tiles[x - adjust][fromY + count] != Tileset.FLOOR) {
                    tiles[x - adjust][fromY + count] = Tileset.WALL;
                }
            }
        }
    }

    public static void createHallwayEdgesY(TETile[][] tiles, int y, int destinationY, int fromX, int adjust) {
        if (tiles[fromX][y - adjust] != Tileset.FLOOR) {
            tiles[fromX][y - adjust] = Tileset.WALL;
            for (int count = -1; count != 2; count++) {
                if (tiles[fromX + count][y - adjust] != Tileset.FLOOR) {
                    tiles[fromX + count][y - adjust] = Tileset.WALL;
                }
            }
        }
    }

    public static void generateHallwayTiles(TETile[][] tiles, int fromX, int fromY, int destinationX, int destinationY, boolean axis) {
        if (axis) {
            for (int x = fromX; x != destinationX; x--) {
                for (int count = -1; count != 2; count++) {
                    if (tiles[x][fromY + count] != Tileset.FLOOR) {
                        tiles[x][fromY + count] = Tileset.WALL;
                    }
                }
                tiles[x][fromY] = Tileset.FLOOR;
                if (x == destinationX + 1) {
                    createHallwayEdgesX(tiles, x, destinationX, fromX, fromY, 1);
                } else if (x == fromX) {
                    createHallwayEdgesX(tiles, x, destinationX, fromX, fromY, -1);
                }

            }
        } else {
            destinationY -= 1;
            for (int y = fromY; y != destinationY; y--) {
                for (int count = -1; count != 2; count++) {
                    if (fromX + count < WIDTH || fromX + count > 0) {
                        if (tiles[fromX + count][y] != Tileset.FLOOR) {
                            tiles[fromX + count][y] = Tileset.WALL;
                        }
                    }
                }
                if (y == destinationY + 1) {
                    createHallwayEdgesY(tiles, y, destinationY, fromX, 1);
                } else if (y == fromY) {
                    createHallwayEdgesY(tiles, y, destinationY, fromX, -1);
                }
                tiles[fromX][y] = Tileset.FLOOR;
            }
        }
    }

    public static void main(String[] args) {

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        Engine engine = new Engine();
        /** The number below is the seed. The method can only take in numbers for now. Try changing the seed!
         * The world should generate a random number of rooms between 5 and 25 of many. The rooms should
         * be of a random size within given parameters. Note that if you put the same seed in, you get
         * the same world generation! That will be useful later.*/
        myWorldDemo = engine.interactWithInputCheese("123");
        ter.renderFrame(myWorldDemo);
    }


}



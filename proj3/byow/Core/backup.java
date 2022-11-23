package byow.Core;

        import byow.TileEngine.TERenderer;
        import byow.TileEngine.TETile;
        import byow.TileEngine.Tileset;

        import java.awt.*;


        import java.util.*;
        import java.util.List;

        import java.util.HashSet;
        import java.util.HashMap;


        import static java.lang.Math.abs;

public class backup {
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
    public static HashMap<Rectangle, Point> points = new HashMap<>();

    public static HashMap<Rectangle, Integer> pointsx = new HashMap<>();
    public static HashMap<Rectangle, Integer> pointsy = new HashMap<>();

    public static Set<Rectangle> connectedrectangles = new HashSet<>();

    public static void makeRooms(TETile[][] tiles, Random random) {
        /** Set the background as a series of empty tiles.*/
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }

        /** Initialize variables.*/
        int xs;
        int ys;
        int room_width;
        int room_height;
        Rectangle ra = null;
        int maxRooms = random.nextInt(5,25);

        for(int i = 0; i < maxRooms; i++) {
            cont:
            while (true) {
                /** Set a random origin for the rectangle, these are the variables xs and ys.*/


                room_height = random.nextInt(ROOM_MIN, ROOM_MAX + 1);
                room_width = random.nextInt(ROOM_MIN, ROOM_MAX + 1);
                xs = random.nextInt(0, WIDTH + 1);
                ys = random.nextInt(0, HEIGHT + 1 - room_height);

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
        int minx = Collections.min(pointsx.values());
        int maxx = Collections.max(pointsx.values());
        int miny = Collections.min(pointsy.values());
        int maxy = Collections.max(pointsy.values());



        for (Rectangle room : rooms) {
            createLinearPath(tiles, points.get(room).x, points.get(room).y, maxx, maxy, random);

        }

        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                if (tiles[x][y] == Tileset.FLOOR && (tiles[x][y + 1] == Tileset.NOTHING ||tiles[x + 1][y] == Tileset.NOTHING||
                        tiles[x][y - 1] == Tileset.NOTHING||tiles[x - 1][y] == Tileset.NOTHING)) {
                    tiles[x][y] = Tileset.WALL;

                }
            }
        }

        /** using points hashmap with location to possibly find closest rectangle and then create hallway
         List<Rectangle> hallways = new ArrayList<>();
         int compare_rect = 0;
         //while (connectedrectangles.size() != rooms.size()) {
         List<Integer> roomdistancex = new ArrayList<>();
         List<Integer> roomdistancey = new ArrayList<>();
         int room_index = random.nextInt(1, rooms.size());
         Rectangle current_rect = rooms.get(3);
         Rectangle other_rect = rooms.get(0);
         int distancex = points.get(current_rect).x - points.get(other_rect).x;
         int distancey = points.get(current_rect).y - points.get(other_rect).y;
         Rectangle hallwayx = new Rectangle(points.get(current_rect).x, points.get(current_rect).y - 2, abs(distancex), 3);
         Rectangle hallwayy = new Rectangle(points.get(current_rect).x, points.get(current_rect).y, 3, abs(distancey));


         if (distancex < 0) {
         for (int x = hallwayx.x; x < hallwayx.width + hallwayx.x; x += 1) {
         for (int y = hallwayx.y; y < hallwayx.height + hallwayx.y; y += 1) {
         if (y == hallwayx.y + 1 || tiles[x][y] == Tileset.FLOOR) {
         tiles[x][y] = Tileset.FLOOR;
         } else {
         tiles[x][y] = Tileset.WALL;
         }
         }
         }

         if (distancey > 0) {
         for (int x = hallwayx.width + hallwayx.x ; x < hallwayx.width + hallwayx.x  + hallwayy.width; x += 1) {
         for (int y = hallwayy.y; y > hallwayy.y - hallwayy.height; y -= 1) {
         if ((x == hallwayx.width + hallwayx.x + 1 || tiles[x][y] == Tileset.FLOOR ||
         (y == hallwayy.y - 1 && x != hallwayx.width + hallwayx.x + 2)) && y != hallwayy.y) {
         tiles[x][y] = Tileset.FLOOR;
         } else {
         tiles[x][y] = Tileset.WALL;
         }
         }
         }
         System.out.println("r");
         } else {
         for (int x = hallwayx.width + hallwayx.x - 1; x < hallwayx.width + hallwayx.x  + hallwayy.width - 1; x += 1) {
         for (int y = hallwayy.y - 2; y < hallwayy.height + hallwayy.y; y += 1) {
         if ((x == hallwayx.width + hallwayx.x + 1 || tiles[x][y] == Tileset.FLOOR ||
         (y == hallwayy.y + 1 && x != hallwayx.width + hallwayx.x + 2)) && y != hallwayy.y) {
         tiles[x][y] = Tileset.FLOOR;
         } else {
         tiles[x][y] = Tileset.FLOWER;
         }
         }
         }
         System.out.println("l");


         }

         } else {
         for (int x = hallwayx.x; x > hallwayx.x - hallwayx.width ; x -= 1) {
         for (int y = hallwayx.y; y < hallwayx.height + hallwayx.y; y += 1) {
         if (y == hallwayx.y + 1 || tiles[x][y] == Tileset.FLOOR) {
         tiles[x][y] = Tileset.FLOOR;
         } else {
         tiles[x][y] = Tileset.WALL;
         }
         }
         }

         if (distancey > 0) {
         for (int x = hallwayx.x + 1 - hallwayx.width; x > hallwayx.x - hallwayx.width + 1 - hallwayy.width; x -= 1) {
         for (int y = hallwayy.y; y > hallwayy.y - hallwayy.height; y -= 1) {
         if ((x == hallwayx.x - hallwayx.width  || tiles[x][y] == Tileset.FLOOR ||
         (y == hallwayy.y + 1 && x != hallwayx.x - hallwayx.width -1)) && y != hallwayy.y) {
         tiles[x][y] = Tileset.FLOOR;
         } else {
         tiles[x][y] = Tileset.WALL;
         }
         }
         }
         System.out.println("r");
         } else {
         for (int x = hallwayx.x + 1 - hallwayx.width; x > hallwayx.x - hallwayx.width + 1 - hallwayy.width; x -= 1) {
         for (int y = hallwayy.y; y < hallwayy.height + hallwayy.y; y += 1) {
         if ((x == hallwayx.width + hallwayx.x + 1 || tiles[x][y] == Tileset.FLOOR ||
         (y == hallwayy.y + 1 && x != hallwayx.width + hallwayx.x + 2)) && y != hallwayy.y) {
         tiles[x][y] = Tileset.FLOWER;
         } else {
         tiles[x][y] = Tileset.FLOWER;
         }
         }
         }
         System.out.println("l");


         }
         }


         connectedrectangles.add(current_rect);
         connectedrectangles.add(other_rect);
         System.out.println(connectedrectangles);
         //}






         /**
         int min_x = Collections.min(roomdistancex);
         int min_y = Collections.min(roomdistancey);
         int min;
         //createLinearPath(tiles, 20, 5, 50, 20, random);
         Rectangle hallway;
         if (min_x <= min_y) {
         min = min_x;
         Rectangle desiredconnectrect = rooms.get(roomdistancex.indexOf(min));
         if (rooms.get(compare_rect).x < desiredconnectrect.x) {
         hallway = new Rectangle(points.get(rooms.get(compare_rect)).x + rooms.get(compare_rect).width / 2,
         points.get(rooms.get(compare_rect)).y, min, 3);

         } else {
         hallway = new Rectangle(desiredconnectrect.x + desiredconnectrect.width,
         desiredconnectrect.y - desiredconnectrect.height / 2, min, 3);
         }

         connectedrectangles.add(desiredconnectrect);
         connectedrectangles.add(rooms.get(compare_rect));
         connectedrectangles.add(hallway);
         } else {
         min = min_y;
         Rectangle desiredconnectrect = rooms.get(roomdistancey.indexOf(min));

         if (rooms.get(compare_rect).y > desiredconnectrect.y) {
         hallway = new Rectangle(rooms.get(compare_rect).x,
         rooms.get(compare_rect).y - rooms.get(compare_rect).height, 3, min);
         } else {
         hallway = new Rectangle(desiredconnectrect.x,
         desiredconnectrect.y - desiredconnectrect.height, 3, min);

         }
         connectedrectangles.add(desiredconnectrect);
         connectedrectangles.add(rooms.get(compare_rect));
         connectedrectangles.add(hallway);


         }
         hallways.add(hallway); */



        //compare_rect += 1;


        //}




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







    public static void main(String[] args) {

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);


        Engine engine = new Engine();
        /** The number below is the seed. The method can only take in numbers for now. Try changing the seed!
         * The world should generate a random number of rooms between 5 and 25 of many. The rooms should
         * be of a random size within given parameters. Note that if you put the same seed in, you get
         * the same world generation! That will be useful later.*/


        myWorldDemo = engine.interactWithInputString("831");
        ter.renderFrame(myWorldDemo);
    }
}

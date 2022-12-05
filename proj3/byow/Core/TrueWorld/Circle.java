package byow.Core.TrueWorld;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.List;

public class Circle {
    public int radius;
    public int diameter;
    public int xs;
    public int ys;
    public int sx;
    public int sy;
    public int originX;
    public int originY;

    public Circle(Rectangle ra, int radius, int originX, int originY) {
        this.xs = ra.x + ra.width / 2 - radius;
        this.ys = ra.y + ra.height / 2 - radius;
        this.radius = radius;
        this.diameter = radius * 2;
        this.sx = ra.x + radius * 2 - 1;
        this.sy = ra.y + radius * 2 - 1;
        this.originX = originX;
        this.originY = originY;
    }

    public boolean intersects(Rectangle ra) {
        if (ra.x != originX && ra.y != originY) {
            int xPOI = ra.x + ra.width / 2;
            int yPOI = ra.y + ra.width / 2;
            if (xPOI > xs && yPOI > ys && xPOI < xs + radius * 2 && yPOI < ys + radius * 2) {
                return true;
            }
        }
        return false;
    }

    public void drawCircle(TETile[][] tiles, Circle ca) {
        for (int x = xs; x < xs + radius * 2; x++) {
            for (int y = ys; y < ys + radius * 2; y++) {
                if (x > 0 && x < 90 && y > 0 && y < 45) {
                    tiles[x][y] = Tileset.FLOOR;
                }
            }
        }
        tiles[xs][ys] = Tileset.NOTHING;
    }
}
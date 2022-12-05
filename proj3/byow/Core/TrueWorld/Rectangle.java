package byow.Core.TrueWorld;

import java.awt.*;
import java.util.List;

public class Rectangle extends java.awt.Rectangle {
    public boolean hasDoor = false;
    public boolean isConnected = false;
    public Direction entrance = null;
    public Point destination = null;
    public boolean locked = false;
    public Rectangle root = null;
    public boolean isRoot = false;

    public Rectangle(int x, int y, int height, int width) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.hasDoor = false;
        this.entrance = null;
    }

    public void hasEntrance(Direction dir) {
        switch(dir) {
            case North:
                entrance = Direction.North;
                break;
            case East:
                entrance = Direction.East;
                break;
            case South:
                entrance = Direction.South;
                break;
            case West:
                entrance = Direction.West;
                break;
        }
    }

    public void createDoor() {
        this.hasDoor = true;
    }

    public void createConnection() {
        isConnected = true;
    }

    public Rectangle rightMost(Rectangle ra, List<Rectangle> rooms) {
        Circle ca;
        int radius = 100;
        ca = new Circle(ra, radius, ra.x, ra.y);
        Rectangle rightMost = new Rectangle(0, 0, 5, 5);
        for (Rectangle rb : rooms) {
            if (ca.intersects(rb) && rb.root == ra) {
                if (rb.x > rightMost.x) {
                    rightMost = rb;
                }
            }
        }
        return rightMost;
    }

    public Rectangle leftMost(Rectangle ra, List<Rectangle> rooms) {
        Circle ca;
        int radius = 100;
        ca = new Circle(ra, radius, ra.x, ra.y);
        Rectangle leftMost = new Rectangle(90, 0, 5, 5);
        for (Rectangle rb : rooms) {
            if (ca.intersects(rb) && rb.root == ra) {
                if (rb.x < leftMost.x) {
                    leftMost = rb;
                }
            }
        }
        return leftMost;
    }
}
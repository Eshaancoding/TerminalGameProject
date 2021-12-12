package io.github.Eshaancoding.JavaTextBaseGame;

public class Box {
    int height;
    int width;
    int leftX;
    int downY;
    String representation;
    public Box (int leftX, int downY, int width, int height, String representation) {
        this.height = height;
        this.width = width;
        this.leftX = leftX;
        this.downY = downY;
        this.representation = representation;
    }
    public boolean point_in_box (float x, float y) {
        return (x >= leftX && x <= leftX + width) && (y >= downY && y <= downY + height);
    }
}


package io.rizvan.beans;

public class Zone {
    private int width;
    private int height;

    public Zone() {
    }

    public Zone(Zone zone) {
        this.width = zone.width;
        this.height = zone.height;
    }

    public Zone(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

package io.rizvan.utils;

public class Coord<T> {
    private final T x;
    private final T y;

    public Coord(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public T getX() { return x; }
    public T getY() { return y; }

    public static <T> Coord<T> of(T x, T y) {
        return new Coord<>(x, y);
    }
}
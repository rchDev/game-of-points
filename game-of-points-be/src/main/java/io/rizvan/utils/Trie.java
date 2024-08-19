package io.rizvan.utils;

public class Trie<T, U, V> {
    private T first;
    private U second;
    private V third;

    public Trie(T first, U second, V third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public U getSecond() {
        return second;
    }

    public void setSecond(U second) {
        this.second = second;
    }

    public V getThird() {
        return third;
    }

    public void setThird(V third) {
        this.third = third;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                ", third=" + third +
                '}';
    }
}

package controller;

/**
 * Created by nitix on 02/10/2016.
 */
public class Tuple<T, Y> {

    public final T first;

    public final Y second;

    public Tuple(T first, Y second) {
        this.first = first;
        this.second = second;
    }
}

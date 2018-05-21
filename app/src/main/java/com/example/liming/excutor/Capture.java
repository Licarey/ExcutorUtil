package com.example.liming.excutor;

/**
 * Created by liming on 2017/12/6.
 * email liming@finupgroup.com
 */

public class Capture<T> {
    private T value;

    public Capture() {
    }

    public Capture(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}

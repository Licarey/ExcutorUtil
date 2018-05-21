package com.example.liming.validateproject.bean;

/**
 * Created by liming on 2018/1/12.
 * email liming@finupgroup.com
 */

public class Test {
    private String name;

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

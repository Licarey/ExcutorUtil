package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liming on 2017/12/19.
 * email liming@finupgroup.com
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface LBindClick {
    int value();
}

package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liming on 2017/12/19.
 * email liming@finupgroup.com
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface LCheckView {
    int value() default 0;
}

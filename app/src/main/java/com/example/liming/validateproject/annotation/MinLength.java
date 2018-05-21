package com.example.liming.validateproject.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**验证最小位数
 * Created by liming on 2017/11/17.
 * email liming@finupgroup.com
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MinLength {
    int length();
    String msg()  ;
}

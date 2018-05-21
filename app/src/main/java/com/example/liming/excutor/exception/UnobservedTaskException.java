package com.example.liming.excutor.exception;

/**Used to signify that a Task's error went unobserved.
 * Created by liming on 2017/12/6.
 * email liming@finupgroup.com
 */

public class UnobservedTaskException extends RuntimeException {
    public UnobservedTaskException(Throwable cause){
        super(cause);
    }
}

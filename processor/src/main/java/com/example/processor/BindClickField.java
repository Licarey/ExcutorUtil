package com.example.processor;


import com.example.annotation.LBindClick;
import com.example.annotation.LBindView;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;

/**
 * Created by liming on 2017/12/22.
 * email liming@finupgroup.com
 */

public class BindClickField {
    /**
     * 方法元素
     */
    private ExecutableElement executableElement;
    /**
     * 控件id
     */
    private int resId;
    /**
     * 绑定方法名
     */
    private String methodName;

    public BindClickField(Element element) {
        //只支持方法注解
        if (element.getKind() != ElementKind.METHOD) {
            throw new IllegalArgumentException(String.format("Only method can be annotated with @%s",
                    LBindClick.class.getSimpleName()));
        }
        executableElement = (ExecutableElement) element;//转化成方法元素
        LBindClick gzClickView = executableElement.getAnnotation(LBindClick.class);//注解对象
        resId = gzClickView.value();//获取id
        if (resId < 0) {
            throw new IllegalArgumentException(String.format("value() in %s for field %s is not valid !", LBindView.class.getSimpleName(),
                            executableElement.getSimpleName()));
        }
        methodName = executableElement.getSimpleName().toString();
    }

    public ExecutableElement getExecutableElement() {
        return executableElement;
    }

    public int getResId() {
        return resId;
    }

    public String getMethodName() {
        return methodName;
    }
}

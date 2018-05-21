package com.example.api;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by liming on 2017/12/22.
 * email liming@finupgroup.com
 */

public class LViewFinder {
    private static final ObjectIViewFinder activityFinder = new ObjectIViewFinder();//默认声明一个Activity View查找器
    private static final Map<String, IViewBinder> binderMap = new LinkedHashMap<>();//管理保持管理者Map集合

    /**
     * target ObjectIViewFinder
     *
     * @param target
     */
    public static void bind(Object target) {
        bind(target, target, activityFinder);
    }


    /**
     * 注解绑定
     * @param host   表示注解 View 变量所在的类，也就是注解类
     * @param object 表示查找 View 的地方，Activity & View 自身就可以查找，Fragment
     * @param finder ui绑定提供者接口
     */
    private static void bind(Object host, Object object, IViewFinder finder) {
        String className = host.getClass().getName();
        try {
            IViewBinder binder = binderMap.get(className);
            if (binder == null) {
                Class<?> aClass = Class.forName(className + "$$IViewBinder");
                binder = (IViewBinder) aClass.newInstance();
                binderMap.put(className, binder);
            }
            if (binder != null) {
                binder.bindView(host, object, finder);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解除注解绑定 ObjectIViewFinder
     *
     * @param host
     */
    public static void unBind(Object host) {
        String className = host.getClass().getName();
        IViewBinder binder = binderMap.get(className);
        if (binder != null) {
            binder.unBindView(host);
        }
        binderMap.remove(className);
    }
}

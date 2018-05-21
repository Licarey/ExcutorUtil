package com.example.api;

/**
 * Created by liming on 2017/12/22.
 * email liming@finupgroup.com
 */

public interface IViewBinder<T> {
    void bindView(T host, Object object, IViewFinder finder);
    void unBindView(T host);
}

package com.example.api;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by liming on 2017/12/22.
 * email liming@finupgroup.com
 */

public class ObjectIViewFinder implements IViewFinder {
    @Override
    public View findView(Object object, int id) {
        if(object instanceof Activity){
            return ((Activity) object).findViewById(id);
        }else if(object instanceof Fragment){
            return ((Fragment) object).getView().findViewById(id);
        }
        return ((View) object).findViewById(id);
    }
}


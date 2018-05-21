package com.example.liming.processor;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.annotation.LBindClick;
import com.example.annotation.LBindView;
import com.example.api.LViewFinder;
import com.example.liming.validateproject.R;

/**
 * Created by liming on 2017/12/22.
 * email liming@finupgroup.com
 */

public class ProcessorLayout extends LinearLayout {
    @LBindView(R.id.text)
    TextView view;

    public ProcessorLayout(Context context) {
        super(context);
        init(context);
    }

    public ProcessorLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProcessorLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View myView = mInflater.inflate(R.layout.activity_view, null);
        addView(myView);
        LViewFinder.bind(this);
    }

    @LBindClick(R.id.text)
    public void tab(){
        view.setText("你点击了自定义view");
        view.setTextColor(Color.RED);
    }
}

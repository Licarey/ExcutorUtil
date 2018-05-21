package com.example.liming.processor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.annotation.LBindClick;
import com.example.annotation.LBindView;
import com.example.api.LViewFinder;
import com.example.liming.validateproject.R;

/**
 * Created by liming on 2017/12/22.
 * email liming@finupgroup.com
 */

public class ProcessorFragment extends Fragment {
    @LBindView(R.id.btn)
    ProcessorLayout btn;
    @LBindView(R.id.btns)
    Button btns;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LViewFinder.bind(this);
    }

    @LBindClick(R.id.btns)
    public void clickBtn(){
        Toast.makeText(getActivity() , "你点击了fragment" , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LViewFinder.unBind(this);
    }
}

package com.example.liming.processor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.annotation.LBindClick;
import com.example.annotation.LBindView;
import com.example.api.LViewFinder;
import com.example.liming.validateproject.R;

/**
 * Created by liming on 2017/12/19.
 * email liming@finupgroup.com
 */
public class TestProcessorActivity extends AppCompatActivity {
    @LBindView(R.id.et_input)
    EditText inputView;
    @LBindView(R.id.button)
    Button buttonView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_processor);
        LViewFinder.bind(this);
    }

    @LBindClick(R.id.button)
    public void clickBtn(){
        Toast.makeText(TestProcessorActivity.this  , inputView.getText().toString() , Toast.LENGTH_SHORT).show();
        getSupportFragmentManager().beginTransaction().add(R.id.container , new ProcessorFragment()).commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        LViewFinder.unBind(this);
        super.onDestroy();
    }
}

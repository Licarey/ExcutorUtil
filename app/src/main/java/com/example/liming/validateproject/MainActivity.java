package com.example.liming.validateproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.liming.validateproject.annotation.Index;
import com.example.liming.validateproject.annotation.MaxLength;
import com.example.liming.validateproject.annotation.MinLength;
import com.example.liming.validateproject.annotation.NotNull;
import com.example.liming.validateproject.annotation.PasswordFirst;
import com.example.liming.validateproject.annotation.PasswordSecond;
import com.example.liming.validateproject.annotation.Pattern;
import com.example.liming.validateproject.annotation.Skip;
import com.example.liming.validateproject.result.IValidateResult;
import com.example.liming.validateproject.validate.ValidateManager;

/**
 * Created by liming on 2017/11/19.
 * email liming@finupgroup.com
 */
public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, IValidateResult {

    @Index(1)
    @NotNull(msg = "请输入手机号")
    @Pattern(pattern = Pattern.phone_number , msg = "请输入正确格式的手机号")
    @MinLength(length = 11, msg = "请输入11位的手机号")
    EditText et_phone;

    @Index(2)
    @NotNull(msg = "两次密码验证 密码一不为能空")
    @MinLength(length = 6, msg = "请输入6位以上的密码")
    @MaxLength(length = 12, msg = "请输入12位以下的密码")
    @PasswordFirst()
    EditText et_pwd;

    @Index(3)
    @NotNull(msg = "两次密码验证 密码二不为能空")
    @PasswordSecond(msg = "两次密码不一致")
    EditText et_repwd;

    @Skip
    @Index(4)
    @NotNull(msg = "请输入性别")
    EditText et_sex;

    @Index(5)
    @NotNull(msg = "请输入邮箱")
    @Pattern(pattern = Pattern.email , msg = "请输入正确格式的邮箱")
    EditText et_email;

    private boolean isSkip = true;
    private RadioGroup skipGroup;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ValidateManager.regist(this);
        skipGroup = findViewById(R.id.radio_group);
        et_phone = findViewById(R.id.et_phone);
        et_pwd = findViewById(R.id.et_pwd);
        et_repwd = findViewById(R.id.et_repwd);
        et_sex = findViewById(R.id.et_sex);
        et_email = findViewById(R.id.et_email);
        button = findViewById(R.id.button);
        button.setOnClickListener(this);
        skipGroup.setOnCheckedChangeListener(this);

        et_phone.setText("18500020848");
        et_email.setText("liming@finupgroup.com");
        et_pwd.setText("123456");
        et_repwd.setText("123456");

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rbtn_no_skip:
                isSkip = false;
                break;
            case R.id.rbtn_skip:
                isSkip = true;
                break;
        }
    }

    @Override
    public void onClick(View v) {
        //ValidateManager.check(this , isSkip , this);
        //startActivity(new Intent(MainActivity.this , Test.class));
        startActivity(new Intent(MainActivity.this , InputEndValideActivity.class));
        //startActivity(new Intent(MainActivity.this , TestProcessorActivity.class));
    }

    @Override
    public void onValidateSuccess() {
        Toast.makeText(this, "验证通过", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValidateError(String errorMsg, EditText editText) {
        Toast.makeText(this, "" + errorMsg, Toast.LENGTH_SHORT).show();
        editText.setBackgroundColor(Color.RED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ValidateManager.unregist(this);
    }
}

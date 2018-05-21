package com.example.liming.validateproject.result;

import android.widget.EditText;
import android.widget.Toast;

/**simple回调
 * Created by liming on 2017/11/17.
 * email liming@finupgroup.com
 */

public abstract class SimpleValidateResult implements IValidateResult{
    @Override
    public void onValidateError(String s, EditText editText) {
        Toast.makeText(editText.getContext(),s,Toast.LENGTH_SHORT).show();
    }
}

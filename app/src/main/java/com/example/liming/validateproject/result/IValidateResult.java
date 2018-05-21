package com.example.liming.validateproject.result;

import android.widget.EditText;

/**验证结果回调
 * Created by liming on 2017/11/17.
 * email liming@finupgroup.com
 */

public interface IValidateResult {
    void onValidateSuccess();
    void onValidateError(String errorMsg , EditText editText);
}

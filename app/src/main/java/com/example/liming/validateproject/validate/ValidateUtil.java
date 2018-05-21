package com.example.liming.validateproject.validate;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.example.liming.validateproject.bean.AttrBean;
import com.example.liming.validateproject.bean.LengthBean;
import com.example.liming.validateproject.bean.PasswordBean;
import com.example.liming.validateproject.bean.PatternBean;
import com.example.liming.validateproject.result.IValidateResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**校验工具类
 * Created by liming on 2017/11/18.
 * email liming@finupgroup.com
 */

public class ValidateUtil {

    /**
     * 设置校验view的信息
     * @param view
     * @param isEditText
     * @param msg
     * @param validateResult
     */
    private static void setViewInfo(Object view, boolean isEditText, String msg, IValidateResult validateResult) {
        if (isEditText) {
            validateResult.onValidateError(msg, ((EditText) view));
        } else {
            validateResult.onValidateError(msg, null);
        }
    }

    /**
     * 不可为空
     * @param view
     * @param isEt
     * @param msg
     * @param validateResult
     * @return
     */
    public static boolean notNull(Object view, boolean isEt, String msg, IValidateResult validateResult) {
        if (TextUtils.isEmpty(((TextView) view).getText().toString())) {
            setViewInfo(view, isEt, msg, validateResult);
            return true;
        }
        return false;
    }

    /**
     * 判断是否为空
     * @param view
     * @return
     */
    public static boolean isNull(Object view) {
        if (view == null)
            throw new NullPointerException("view can not be null");
        if (TextUtils.isEmpty(((TextView) view).getText().toString())) {
            return true;
        }
        return false;
    }

    /**
     * 根据正则表达式校验
     * @param view
     * @param isEt
     * @param bean
     * @param validateResult
     * @return
     */
    public static boolean checkPattern(Object view, boolean isEt, PatternBean bean, IValidateResult validateResult) {
        if (isNull(view)) return true;

        Pattern r = Pattern.compile(bean.pattern);
        Matcher m = r.matcher(((TextView) view).getText().toString());
        if (!m.matches()) {
            setViewInfo(view, isEt, bean.msg, validateResult);
            return true;
        }
        return false;
    }

    /**
     * 最大长度校验
     * @param view
     * @param isEt
     * @param bean
     * @param validateResult
     * @return
     */
    public static boolean maxLenght(Object view, boolean isEt, LengthBean bean, IValidateResult validateResult) {
        if (isNull(view)) return true;

        if (((TextView) view).getText().toString().length() > bean.length) {
            setViewInfo(view, isEt, bean.msg, validateResult);
            return true;
        }
        return false;
    }

    /**
     * 最小长度校验
     * @param view
     * @param isEt
     * @param bean
     * @param validateResult
     * @return
     */
    public static boolean minLenght(Object view, boolean isEt, LengthBean bean, IValidateResult validateResult) {
        if (isNull(view)) return true;

        if (((TextView) view).getText().toString().length() < bean.length) {
            setViewInfo(view, isEt, bean.msg, validateResult);
            return true;
        }
        return false;
    }

    /**
     * 密码校验
     * @param view
     * @return
     */
    public static boolean password(AttrBean view, AttrBean pwd1Attr, PasswordBean bean, IValidateResult validateResult) {
        if (isNull(view.view)) {
            return true;
        }
        if (!((TextView) view.view).getText().toString().equals(((EditText) pwd1Attr.view).getText().toString())) {
            setViewInfo(view.view, view.isEditText, bean.msg, validateResult);
            return true;
        }
        return false;
    }
}

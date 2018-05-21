package com.example.liming.validateproject.validate;

import android.text.InputFilter;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.TextView;

import com.example.liming.validateproject.annotation.Index;
import com.example.liming.validateproject.annotation.MaxLength;
import com.example.liming.validateproject.annotation.MinLength;
import com.example.liming.validateproject.annotation.NotNull;
import com.example.liming.validateproject.annotation.PasswordFirst;
import com.example.liming.validateproject.annotation.PasswordSecond;
import com.example.liming.validateproject.annotation.Pattern;
import com.example.liming.validateproject.annotation.Skip;
import com.example.liming.validateproject.bean.AttrBean;
import com.example.liming.validateproject.bean.Basebean;
import com.example.liming.validateproject.bean.LengthBean;
import com.example.liming.validateproject.bean.NotNullBean;
import com.example.liming.validateproject.bean.PasswordBean;
import com.example.liming.validateproject.bean.PatternBean;
import com.example.liming.validateproject.result.IValidateResult;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 校验器
 * Created by liming on 2017/11/18.
 * email liming@finupgroup.com
 */

public class ValidateManager {
    /**
     * 注册的页面
     */
    private static Map<Object, List<AttrBean>> registList = new HashMap<>();
    /**
     * 类型
     */
    private static final String TYPE_NOTNULL = "NotNull";
    private static final String TYPE_PATTERN = "PATTERN";
    private static final String TYPE_MAXLENGTH = "MAXLENGTH";
    private static final String TYPE_MINLENGTH = "MINLENGTH";
    private static final String TYPE_PASSWORDFIRST = "PASSWORD_FIRST";
    private static final String TYPE_PASSWORDSECOND = "PASSWORD_SECOND";
    private static final String TYPE_SKIP = "SKIP";

    /**
     * 开始校验
     * @param activity
     * @param isSkip
     * @param validateResult
     */
    public static void check(Object activity, boolean isSkip, IValidateResult validateResult) {
        if (activity == null || validateResult == null) return;
        List<AttrBean> list = registList.get(activity);
        if (list == null) return;
        for (AttrBean attrBean : list) {
            if (attrBean.index == null) {
                return;
            }
        }
        /**
         * 根据index进行排序
         */
        Collections.sort(list, new Comparator<AttrBean>() {
            public int compare(AttrBean arg0, AttrBean arg1) {
                return arg0.index.compareTo(arg1.index);
            }
        });
        for (AttrBean attrBean : list) {
            for (Basebean bean : attrBean.annos) {
                if (isSkip) {
                    if (TYPE_SKIP.equals(attrBean.annos.getLast().type)) {
                        break;
                    }
                }
                if (TYPE_NOTNULL.equals(bean.type)) {
                    if (ValidateUtil.notNull(attrBean.view, attrBean.isEditText, bean.msg, validateResult)) {
                        return;
                    }
                } else if (TYPE_PATTERN.equals(bean.type)) {
                    if (ValidateUtil.checkPattern(attrBean.view, attrBean.isEditText, (PatternBean) bean, validateResult)) {
                        return;
                    }
                } else if (TYPE_MAXLENGTH.equals(bean.type)) {
                    if (ValidateUtil.maxLenght(attrBean.view, attrBean.isEditText, (LengthBean) bean, validateResult)) {
                        return;
                    }
                } else if (TYPE_MINLENGTH.equals(bean.type)) {
                    if (ValidateUtil.minLenght(attrBean.view, attrBean.isEditText, (LengthBean) bean, validateResult)) {
                        return;
                    }
                } else if (TYPE_PASSWORDFIRST.equals(bean.type)) {
                    if (ValidateUtil.isNull(attrBean.view)) {
                        return;
                    }
                    pwd1Attr = attrBean;
                } else if (TYPE_PASSWORDSECOND.equals(bean.type)) {
                    if (ValidateUtil.password(attrBean, pwd1Attr, (PasswordBean) bean, validateResult)) {
                        return;
                    }
                    pwd1Attr = null;
                }
            }
        }
        validateResult.onValidateSuccess();
        pwd1Attr = null;
    }

    private static AttrBean pwd1Attr = null;

    /**
     * 注册
     *
     * @param target
     */
    public static void regist(final Object target) {
        try {
            Class clazz = target.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(NotNull.class) ||
                        field.isAnnotationPresent(Pattern.class) ||
                        field.isAnnotationPresent(MaxLength.class) ||
                        field.isAnnotationPresent(MinLength.class) ||
                        field.isAnnotationPresent(Index.class) ||
                        field.isAnnotationPresent(PasswordFirst.class) ||
                        field.isAnnotationPresent(PasswordSecond.class)) {

                    field.setAccessible(true);
                    List<AttrBean> editTextMap = registList.get(target);
                    if (editTextMap == null) {
                        editTextMap = new LinkedList<>();
                        registList.put(target, editTextMap);
                    }
                    AttrBean attr = new AttrBean();
                    attr.name = field.getName();
                    attr.view = field.get(target);

                    if (field.getType() == EditText.class) {
                        attr.isEditText = true;
                    } else if (field.getType() == TextView.class) {
                        attr.isEditText = false;
                    }
                    if (attr.annos == null) {
                        attr.annos = new LinkedList<>();
                    }
                    editTextMap.add(attr);

                    if (field.isAnnotationPresent(NotNull.class))
                        attr.annos.add(validateType(field, TYPE_NOTNULL));
                    if (field.isAnnotationPresent(Pattern.class))
                        attr.annos.add(validateType(field, TYPE_PATTERN));
                    if (field.isAnnotationPresent(MaxLength.class)) {
                        attr.annos.add(validateType(field, TYPE_MAXLENGTH));
                        if (attr.view != null && attr.isEditText) {
                            int length = field.getAnnotation(MaxLength.class).length();
                            ((EditText) attr.view).setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
                        }
                    }
                    if (field.isAnnotationPresent(MinLength.class))
                        attr.annos.add(validateType(field, TYPE_MINLENGTH));
                    if (field.isAnnotationPresent(PasswordFirst.class)) {
                        attr.annos.add(validateType(field, TYPE_PASSWORDFIRST));
                        if (attr.view != null && attr.isEditText) {
                            ((EditText) attr.view).setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }
                    }
                    if (field.isAnnotationPresent(PasswordSecond.class)) {
                        attr.annos.add(validateType(field, TYPE_PASSWORDSECOND));
                        if (attr.view != null && attr.isEditText) {
                            ((EditText) attr.view).setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }
                    }
                    if (field.isAnnotationPresent(Index.class))
                        attr.index = field.getAnnotation(Index.class).value();
                    if (field.isAnnotationPresent(Skip.class))
                        attr.annos.add(validateType(field, TYPE_SKIP));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据不同类型校验
     *
     * @param field
     * @param type
     * @return
     * @throws IllegalAccessException
     */
    private static Basebean validateType(Field field, String type) throws IllegalAccessException {
        if (type.equals(TYPE_NOTNULL)) {
            NotNull notnull = field.getAnnotation(NotNull.class);
            NotNullBean bean = new NotNullBean();
            bean.msg = notnull.msg();
            bean.type = type;
            return bean;
        } else if (type.equals(TYPE_PATTERN)) {
            Pattern re = field.getAnnotation(Pattern.class);
            PatternBean reBean = new PatternBean();
            reBean.msg = re.msg();
            reBean.type = TYPE_PATTERN;
            reBean.pattern = re.pattern();
            return reBean;
        } else if (type.equals(TYPE_MAXLENGTH)) {
            MaxLength anno = field.getAnnotation(MaxLength.class);
            LengthBean bean = new LengthBean();
            bean.msg = anno.msg();
            bean.type = TYPE_MAXLENGTH;
            bean.length = anno.length();
            return bean;
        } else if (type.equals(TYPE_MINLENGTH)) {
            MinLength anno = field.getAnnotation(MinLength.class);
            LengthBean bean = new LengthBean();
            bean.msg = anno.msg();
            bean.type = TYPE_MINLENGTH;
            bean.length = anno.length();
            return bean;
        } else if (type.equals(TYPE_PASSWORDFIRST)) {
            PasswordBean bean = new PasswordBean();
            bean.type = TYPE_PASSWORDFIRST;
            return bean;
        } else if (type.equals(TYPE_PASSWORDSECOND)) {
            PasswordSecond anno = field.getAnnotation(PasswordSecond.class);
            PasswordBean bean = new PasswordBean();
            bean.msg = anno.msg();
            bean.type = TYPE_PASSWORDSECOND;
            return bean;
        } else if (type.equals(TYPE_SKIP)) {
            Basebean bean = new Basebean();
            bean.type = TYPE_SKIP;
            return bean;
        }

        return null;
    }

    /**
     * 解注册
     *
     * @param target
     */
    public static void unregist(Object target) {
        registList.remove(target);
    }
}

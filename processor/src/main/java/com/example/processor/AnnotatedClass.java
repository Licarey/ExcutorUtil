package com.example.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by liming on 2017/12/22.
 * email liming@finupgroup.com
 */

public class AnnotatedClass {
    private static class TypeUtil {
        static final ClassName BINDER = ClassName.get("com.example.api", "IViewBinder");
        static final ClassName PROVIDER = ClassName.get("com.example.api", "IViewFinder");
    }

    /**类或者接口元素*/
    private TypeElement mTypeElement;

    /**绑定的view对象*/
    private ArrayList<BindViewField> mFields;

    /**辅助类，用于后文的文件输出*/
    private Elements mElements;

    /**绑定方法域*/
    private ArrayList<BindClickField> mClickFiled;

    /**
     * @param typeElement 注解所在的类或者接口
     * @param elements 辅助类
     **/
    AnnotatedClass(TypeElement typeElement, Elements elements) {
        mTypeElement = typeElement;
        mElements = elements;
        mFields = new ArrayList<>();
        mClickFiled = new ArrayList<>();
    }

    /**增加绑定方法域*/
    void addClickField(BindClickField field) {
        mClickFiled.add(field);
    }

    /**增加绑定view域*/
    void addField(BindViewField field) {
        mFields.add(field);
    }

    /**生成java代码**/
    JavaFile generateFile() {
        /**
         * 定义方法 bindbindView(final T host, Object object, ViewFinder finder);
         */
        MethodSpec.Builder bindViewMethod = MethodSpec.methodBuilder("bindView")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mTypeElement.asType()), "host")
                .addParameter(TypeName.OBJECT, "source",Modifier.FINAL)//后面我们需要使用源文件注册方法到控件中，因此这里需要final
                .addParameter(TypeUtil.PROVIDER, "finder");


        for (BindViewField field : mFields) {
            bindViewMethod.addStatement("host.$N = ($T)(finder.findView(source, $L))", field.getFieldName(), ClassName.get(field.getFieldType()), field.getResId());
        }

        ClassName androidView = ClassName.get("android.view","View");

        if(mClickFiled!=null) {
            for (BindClickField filed : mClickFiled) {
                bindViewMethod.addStatement("finder.findView(source, $L).setOnClickListener(new $T.OnClickListener()" +
                        " {" +
                        "@Override " +
                        "public void onClick($T view) " +
                        "{ " +
                        " (($T)source).$N " +
                        "}" +
                        "}" +
                        ");", filed.getResId(),androidView,androidView, TypeName.get(mTypeElement.asType()),filed.getMethodName() + "();");
            }
        }
        MethodSpec.Builder unBindViewMethod = MethodSpec.methodBuilder("unBindView")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(mTypeElement.asType()), "host")
                .addAnnotation(Override.class);
        for (BindViewField field : mFields)
            unBindViewMethod.addStatement("host.$N = null", field.getFieldName());

        TypeSpec injectClass = TypeSpec.classBuilder(mTypeElement.getSimpleName() + "$$ViewBinder")//类名字
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(TypeUtil.BINDER, TypeName.get(mTypeElement.asType())))//接口，首先是接口然后是范型
                .addMethod(bindViewMethod.build())//再加入我们的目标方法
                .addMethod(unBindViewMethod.build())
                .build();

        String packageName = mElements.getPackageOf(mTypeElement).getQualifiedName().toString();
        return JavaFile.builder(packageName, injectClass)
                .build();
    }
}

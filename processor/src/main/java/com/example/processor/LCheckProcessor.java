package com.example.processor;

import com.example.annotation.LCheckActivity;
import com.example.annotation.LCheckView;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by liming on 2018/1/10.
 * email liming@finupgroup.com
 *
 * debug processor主要几步：
 * 1.gradle.properties配置org.gradle.jvmargs=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8102
                         org.gradle.daemon=true
 *
 * 2.Edit Configurations
 *
 * 3.gradlew --daemon 启动守护进程
 *
 * 4.运行debug
 * 5.gradlew clean assembleDebug
 *
 */
@AutoService(Processor.class)
public class LCheckProcessor extends AbstractProcessor {

    private Elements elementUtils;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(LCheckActivity.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(LCheckActivity.class);
        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element;
            List<? extends Element> members = elementUtils.getAllMembers(typeElement);
            MethodSpec.Builder bindViewMethodSpecBuilder = MethodSpec.methodBuilder("bindViewAndCheck")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(TypeName.VOID)
                    .addParameter(ClassName.get(typeElement.asType()), "activity" , Modifier.FINAL);
            MethodSpec.Builder unBindViewMethodSpecBuilder = MethodSpec.methodBuilder("unBindViewAndCheck")
                    .addModifiers(Modifier.PUBLIC , Modifier.STATIC)
                    .addStatement("if(viewList != null){\n" +
                            "viewList.clear();\n" +
                            "viewList = null;\n" +
                            "}")
                    .returns(TypeName.VOID);
            MethodSpec.Builder addView = MethodSpec.methodBuilder("checkInput")
                    .addModifiers(Modifier.PRIVATE , Modifier.STATIC)
                    .addStatement("boolean enableClick = true")
                    .beginControlFlow("for (int i = " + 0 + "; i < viewList.size()" + "; i++)")
                    .addStatement("if(android.text.TextUtils.isEmpty(viewList.get(i).getText().toString())){\n" +
                            "enableClick = false;\n" +
                            "}\n")
                    .endControlFlow()
                    .addStatement("if(mActivity != null) {" +
                            "((" + ClassName.get(typeElement.asType()) +")mActivity).checkInput(enableClick);\n" +
                            "}")
                    .returns(TypeName.VOID);

            bindViewMethodSpecBuilder.addStatement("mActivity = activity");
            for (Element item : members) {
                LCheckView diView = item.getAnnotation(LCheckView.class);
                if (diView == null) {
                    continue;
                }
                bindViewMethodSpecBuilder.addStatement(String.format("activity.%s = (%s) activity.findViewById(%s)", item.getSimpleName(), ClassName.get(item.asType()).toString(), diView.value()));
                bindViewMethodSpecBuilder.addStatement(String.format("activity.%s.addTextChangedListener(new android.text.TextWatcher(){\n" +
                        "@Override\n" +
                        "        public void beforeTextChanged(CharSequence s, int start, int count, int after) {\n" +
                        "\n" +
                        "        }\n" +
                        "\n" +
                        "        @Override\n" +
                        "        public void onTextChanged(CharSequence s, int start, int before, int count) {\n" +
                        "\n" +
                        "        }\n" +
                        "\n" +
                        "        @Override\n" +
                        "        public void afterTextChanged(android.text.Editable s) {\n" +
                        "\n" +   "             checkInput();" +
                        "        }\n" +
                        "})", item.getSimpleName()));
                bindViewMethodSpecBuilder.addStatement(String.format("             viewList.add(activity.%s);" , item.getSimpleName()));
            }


            TypeSpec typeSpec;
            try {
                ClassName view = ClassName.get("android.widget", "EditText");
                ClassName list = ClassName.get("java.util", "List");
                ClassName arrayList = ClassName.get("java.util", "ArrayList");
                TypeName listOfHoverboards = ParameterizedTypeName.get(list, view);

                typeSpec = TypeSpec.classBuilder("L" + element.getSimpleName())
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addField(ClassName.get(typeElement.asType()) , "mActivity" , Modifier.PRIVATE , Modifier.STATIC)
                        .addField(listOfHoverboards , "viewList" , Modifier.PRIVATE , Modifier.STATIC)
                        .addStaticBlock(CodeBlock.builder().addStatement("viewList = new $T<>()", arrayList).build())
                        .addMethod(bindViewMethodSpecBuilder.build())
                        .addMethod(addView.build())
                        .addMethod(unBindViewMethodSpecBuilder.build())
                        .build();
                JavaFile javaFile = JavaFile.builder(getPackageName(typeElement), typeSpec).build();
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }
}

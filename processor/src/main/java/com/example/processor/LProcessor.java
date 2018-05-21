package com.example.processor;

import com.example.annotation.LBindClick;
import com.example.annotation.LBindView;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by liming on 2017/12/19.
 * email liming@finupgroup.com
 *
 * AutoService会自动在META-INF文件夹下生成Processor配置信息文件，该文件里就是实现该服务接口的具体实现类。而当外部程序装配这个模块的时候，
 * 就能通过该jar包META-INF/services/里的配置文件找到具体的实现类名，并装载实例化，完成模块的注入。
 * 基于这样一个约定就能很好的找到服务接口的实现类，而不需要再代码里制定。
 */
@AutoService(Processor.class)
public class LProcessor extends AbstractProcessor {
    private Filer mFiler; //文件相关的辅助类
    private Elements mElementUtils; //元素相关的辅助类
    private Messager mMessager; //日志相关的辅助类
    private Map<String, AnnotatedClass> mAnnotatedClassMap;

    /**
     * 初始化
     * @param processingEnv
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();//filter用来创建新的源文件、class文件以及辅助文件
        mElementUtils = processingEnv.getElementUtils();//elements中包含着操作element的工具方法
        mMessager = processingEnv.getMessager();//用来报告错误、警告以及其他提示信息
        mAnnotatedClassMap = new TreeMap<>();
    }

    /**
     * 支持的注解类型
     * @return 支持的类型集合
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(LBindView.class.getCanonicalName());
        types.add(LBindClick.class.getCanonicalName());
        return types;
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mAnnotatedClassMap.clear();
        try {
            processBindView(roundEnv);
            processBindClick(roundEnv);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            error(e.getMessage());
        }
        for (AnnotatedClass annotatedClass : mAnnotatedClassMap.values()) {
            try {
                annotatedClass.generateFile().writeTo(mFiler);
            } catch (IOException e) {
                error("Generate file failed, reason: %s", e.getMessage());
            }
        }
        return true;
    }

    /**处理点击事件绑定*/
    private void processBindClick(RoundEnvironment roundEnv) {
        for(Element element : roundEnv.getElementsAnnotatedWith(LBindClick.class)){
            AnnotatedClass annotatedClass = getAnnotatedClass(element);//获取对应的生成类
            BindClickField clickFile = new BindClickField(element);//生成我们的目标注解模型，方便后期文件输出
            annotatedClass.addClickField(clickFile);
        }

    }

    private void processBindView(RoundEnvironment roundEnv) throws IllegalArgumentException {
        for (Element element : roundEnv.getElementsAnnotatedWith(LBindView.class)) {
            AnnotatedClass annotatedClass = getAnnotatedClass(element);
            BindViewField bindViewField = new BindViewField(element);
            annotatedClass.addField(bindViewField);
        }
    }

    /**获取注解所在文件对应的生成类*/
    private AnnotatedClass getAnnotatedClass(Element element) {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();//typeElement表示类或者接口元素
        String fullName = typeElement.getQualifiedName().toString();
        AnnotatedClass annotatedClass = mAnnotatedClassMap.get(fullName);//获得了注解的类名
        if (annotatedClass == null) {
            annotatedClass = new AnnotatedClass(typeElement, mElementUtils);
            mAnnotatedClassMap.put(fullName, annotatedClass);
        }
        return annotatedClass;
    }

    private void error(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}

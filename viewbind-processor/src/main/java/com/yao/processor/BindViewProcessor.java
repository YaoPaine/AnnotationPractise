package com.yao.processor;

import com.google.auto.service.AutoService;
import com.yao.annotation.BindView;
import com.yao.processor.model.AnnotatedClass;
import com.yao.processor.model.BindViewFiled;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.yao.annotation.BindView")
public class BindViewProcessor extends AbstractProcessor {
    private String TAG = "BindViewProcessor";
    /**
     * 文件相关的辅助类
     */
    Filer mFiler;
    /**
     * 元素相关的辅助类
     */
    Elements mElementsUtils;

    /**
     * 日志相关辅助类
     */
    Messager mMessager;

    /**
     * 解析的目标注解集合
     */
    Map<String, AnnotatedClass> mAnnotatedClassMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mElementsUtils = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(BindView.class.getCanonicalName());//返回该注解处理器支持的注解集合
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mAnnotatedClassMap.clear();
        try {
            processView(roundEnv);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            error(e.getMessage());
            return true;
        }
        try {
            Collection<AnnotatedClass> annotatedClasses = mAnnotatedClassMap.values();
            for (AnnotatedClass annotatedClass : annotatedClasses) {
                Logger.getLogger(TAG).log(Level.WARNING, String.format("generating file for %s", annotatedClass.getFullClassName()));
                annotatedClass.outputFile().writeTo(mFiler);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Logger.getLogger(TAG).log(Level.SEVERE, String.format("generating file failed, reason is :  %s", e.getMessage()));
        }
        return true;
    }

    private void processView(RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(BindView.class);
        for (Element element : elements) {
            AnnotatedClass annotatedClass = getAnnotatedClass(element);
            BindViewFiled bindViewFiled = new BindViewFiled(element);
            annotatedClass.addFiled(bindViewFiled);
        }
    }

    /**
     * @param element
     * @return 如果存在map中直接取，不存在则new一个后存入map中
     */
    private AnnotatedClass getAnnotatedClass(Element element) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        String qualifiedName = enclosingElement.getQualifiedName().toString();
        AnnotatedClass annotatedClass = mAnnotatedClassMap.get(qualifiedName);
        if (annotatedClass == null) {
            annotatedClass = new AnnotatedClass(enclosingElement, mElementsUtils);
            mAnnotatedClassMap.put(qualifiedName, annotatedClass);
        }
        return annotatedClass;
    }

    private void error(String error) {

    }
}

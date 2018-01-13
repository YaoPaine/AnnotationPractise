package com.yao.processor.model;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.List;

public class AnnotatedClass {
    /**
     * 类名
     */
    private TypeElement mClassElement;
    /**
     * 成员变量
     */
    private List<BindViewFiled> mFiled;

    private Elements mElementsUtils;

    public AnnotatedClass(TypeElement classElement, Elements elementsUtils) {
        this.mClassElement = classElement;
        this.mElementsUtils = elementsUtils;
        this.mFiled = new ArrayList<>();
    }

    /**
     * @return 获取当前类的全路径类名
     */
    public String getFullClassName() {
        return mClassElement.getQualifiedName().toString();
    }

    /**
     * 添加一个成员
     *
     * @param filed
     */
    public void addFiled(BindViewFiled filed) {
        this.mFiled.add(filed);
    }

    /**
     * @return 输出Java file
     */
    public JavaFile outputFile() throws ClassNotFoundException {
        MethodSpec.Builder bindMethod = MethodSpec.methodBuilder("bind")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(Class.forName("com.yao.viewbind.Finder"), "finder")
                .addParameter(TypeName.get(mClassElement.asType()), "target", Modifier.FINAL)
                .addParameter(TypeName.OBJECT, "source");

        MethodSpec.Builder unbindMethod = MethodSpec.methodBuilder("unbind")
                .addParameter(TypeName.get(mClassElement.asType()), "target")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID);

        for (BindViewFiled field : mFiled) {
            bindMethod.addStatement("target.$N=($T)finder.findView(target,$L)"
                    , field.getFieldName(), ClassName.get(field.getFieldType()), field.getResId());
            unbindMethod.addStatement("target.$N=null", field.getFieldName());
        }
        String packageName = getPackageName(mClassElement);
        String className = getClassName(mClassElement, packageName);
        ClassName buildClass = ClassName.get(packageName, className);
        System.out.println(buildClass.toString());
        TypeSpec typeSpec = TypeSpec.classBuilder(buildClass.simpleName() + "$$Injector")
                .addMethod(bindMethod.build())
                .addMethod(unbindMethod.build())
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get("com.yao.viewbind", "ViewBinder"), TypeName.get(mClassElement.asType())))
                .addModifiers(Modifier.PUBLIC)
                .build();
        return JavaFile.builder(packageName, typeSpec).build();
    }

    /**
     * @param typeElement
     * @return 包名
     */
    public String getPackageName(TypeElement typeElement) {
        return mElementsUtils.getPackageOf(typeElement).getQualifiedName().toString();
    }

    /**
     * @param typeElement
     * @param packageName
     * @return 类名
     */
    public String getClassName(TypeElement typeElement, String packageName) {
        int packLength = packageName.length() + 1;
        return typeElement.getQualifiedName().toString().substring(packLength).replace(".", "&&");
    }
}

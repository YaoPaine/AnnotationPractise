package com.yao.processor.model;

import com.yao.annotation.BindView;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class BindViewFiled {

    private VariableElement mFiledElement;

    private int mResId;

    public BindViewFiled(Element element) {
        if (element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(String.format("Only field can be annotated with @%s", BindView.class.getSimpleName()));
        }
        mFiledElement = (VariableElement) element;
        BindView bindView = mFiledElement.getAnnotation(BindView.class);
        mResId = bindView.value();
        if (mResId < 0) {
            throw new IllegalArgumentException(String.format("value() in %s for field %s is not valid",
                    BindView.class.getSimpleName(), mFiledElement.getSimpleName()));
        }
    }

    public Name getFieldName() {
        return mFiledElement.getSimpleName();
    }

    public int getResId() {
        return mResId;
    }

    public TypeMirror getFieldType() {
        return mFiledElement.asType();
    }

}

package com.yao.processor.internal;

public interface Injector<T> {
    void inject(T host, Object source, Finder finder);
}

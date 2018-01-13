package com.yao.viewbind;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

public class ViewInjector {

    private final static Map<Class<?>, ViewBinder<Object>> BINDERS = new HashMap<>();

    public static void bind(Activity target) {
        Class<? extends Activity> clazz = target.getClass();
        ViewBinder<Object> viewBinder = BINDERS.get(clazz);
        if (viewBinder == null) {
            String name = clazz.getName();
            try {
                Class<?> clas = Class.forName(name + "$$Injector");
                viewBinder = (ViewBinder<Object>) clas.newInstance();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("class not found  " + name);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Unable to bind views for " + name);
            } catch (InstantiationException e) {
                throw new RuntimeException("Unable to bind views for " + name);
            }
        }
        BINDERS.put(clazz, viewBinder);
        viewBinder.bind(Finder.ACTIVITY, target, target);
    }
}
